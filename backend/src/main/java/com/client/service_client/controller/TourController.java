package com.client.service_client.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.controller.interfaces.ITourController;
import com.client.service_client.model.File;
import com.client.service_client.model.Tour;
import com.client.service_client.model.dto.TourDTO;
import com.client.service_client.model.dto.TourUpdateDTO;
import com.client.service_client.model.record.TourClient;
import com.client.service_client.model.record.TourResponse;
import com.client.service_client.model.response.ResponseOnlyMessage;
import com.client.service_client.model.response.ResponseWithData;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.TourService;
import com.client.service_client.storage.StorageService;
import com.client.service_client.util.Constants;
import com.client.service_client.util.FileValidator;
import com.client.service_client.util.ImageResize;
import com.client.service_client.util.Language;

import io.github.mojtabaJ.cwebp.WebpConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class TourController implements ITourController{

    private final TourService tourService;
    private final StorageService storageService;
    private final Language language;
    private final String destination = "/tour";
    private String source;

    public TourController(TourService tourService, StorageService storageService, Language language) {
        this.tourService = tourService;
        this.storageService = storageService;
        this.language = language;

        source = null;
    }

    @Override
    public ResponseEntity<?> createTour(@Valid TourDTO entity, MultipartFile file) {
        try {
            ResponseEntity<?> validationResponse = FileValidator.validateFile(file);

            if (validationResponse != null) {
                return validationResponse;
            }

            Tour tour = new Tour();
            File fileSave = new File();

            tour.setName(entity.getName());
            tour.setDescription_es(entity.getDescription_es());
            tour.setDescription_en(entity.getDescription_en());
            tour.setSummary_es(entity.getSummary_es());
            tour.setSummary_en(entity.getSummary_en());
            tour.setUrl(entity.getUrl());

            fileSave.setName(file.getOriginalFilename());
            fileSave.setMime(file.getContentType());
            fileSave.setSize(file.getSize());
            fileSave.setMain(true);
            fileSave.setTour(tour);

            this.source = storageService.store(file, this.destination);
            fileSave.setSource(this.source);

            tourService.save(tour);
            source = null;

            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseOnlyMessage("New tour created"));
        }
        catch (IllegalArgumentException e) {
            if(source != null) {
                storageService.deleteFile(source);
            }

            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            if(source != null) {
                storageService.deleteFile(source);
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> editTour(TourUpdateDTO entity) {
        try {
            Tour tour = new Tour(entity.getId());
            tour.setName(entity.getName());
            tour.setDescription_es(entity.getDescription_es());
            tour.setDescription_en(entity.getDescription_en());
            tour.setSummary_es(entity.getSummary_es());
            tour.setSummary_en(entity.getSummary_en());
            tour.setUrl(entity.getUrl());
            tour.setStatus(entity.getStatus());

            tourService.save(tour);
            return ResponseEntity.ok().body(new ResponseOnlyMessage("Tour updated"));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        } 
    }

    @Override
    public ResponseEntity<?> getAllTours(HttpServletRequest request) {
        try {
            String language = this.language.requestLanguage(request);
            List<TourClient> tours = tourService.findAll(language);

            if (tours.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<TourResponse> toursResponse = new ArrayList<>();

            for(TourClient tour : tours) {
                byte[] fileContent = WebpConverter.imageByteToWebpByte(ImageResize.resizeImageToBytes(tour.source(), Constants.thumbnailsGeneral), Constants.quality);

                TourResponse tourResponse = new TourResponse(
                    tour.id(),
                    tour.name(),
                    tour.summary(),
                    tour.url(),
                    tour.file_name(),
                    fileContent);
                    toursResponse.add(tourResponse);
            }

            return ResponseEntity.ok().body(new ResponseWithData<List<TourResponse>>("Request successful", toursResponse));
        } 
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> getTourByAdmin(String id) {
        try {
            Optional<Tour> tour = tourService.findById(id);

            if(!tour.isPresent()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(new ResponseWithData<Tour>("Request successful", tour.get()));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    // @Override
    // public ResponseEntity<?> getTourById(HttpServletRequest request, String id) {
    //     try {
    //         String language = this.language.requestLanguage(request);
    //         Optional<Tour> tourResult = tourService.findById(id, language);

    //         if(!tourResult.isPresent()) {
    //             return ResponseEntity.noContent().build();
    //         }

    //         Tour tour = tourResult.get();

    //         if(tour.getStatus() == TourStatus.used) {
    //             return ResponseEntity.noContent().build();
    //         }

    //         List<FilesBytes> files = new ArrayList<>();

    //         for(File file : tour.getFiles()) {
    //             Resource resource = storageService.loadAsResource(file.getSource());
    //             byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());

    //             files.add(new FilesBytes(file.getName(), file.getMain(), fileContent));
    //         }

    //         return ResponseEntity.ok().body(new ResponseWithData<TourWithFile>("Request successful", 
    //             new TourWithFile(
    //                 tour.getId(), 
    //                 tour.getName(), 
    //                 tour.getDescription(), 
    //                 tour.getSummary(), 
    //                 tour.getUrl(), 
    //                 files)
    //         ));
    //     }
    //     catch (IllegalArgumentException e) {
    //         return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
    //     } 
    //     catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
    //     }
    // }
    
}
