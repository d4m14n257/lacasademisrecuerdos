package com.client.service_client.controller;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.controller.interfaces.ITourController;
import com.client.service_client.model.File;
import com.client.service_client.model.Tour;
import com.client.service_client.model.dto.SourceDTO;
import com.client.service_client.model.dto.TourDTO;
import com.client.service_client.model.dto.TourUpdateDTO;
import com.client.service_client.model.record.TourClient;
import com.client.service_client.model.record.TourResponse;
import com.client.service_client.model.response.ResponseOnlyMessage;
import com.client.service_client.model.response.ResponseWithData;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.TourService;
import com.client.service_client.storage.StorageService;
import com.client.service_client.util.FileValidator;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class TourController implements ITourController{

    private TourService tourService;
    private StorageService storageService;
    private final String destination = "/tour";
    private String source;

    public TourController(TourService tourService, StorageService storageService) {
        this.tourService = tourService;
        this.storageService = storageService;

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
            tour.setDescription(entity.getDescription());
            tour.setSummary(entity.getSummary());
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
    public ResponseEntity<?> deleteTour(@Valid @NotEmpty SourceDTO[] tours) {
        try {
            for(SourceDTO tour : tours) {
                tourService.deleteById(tour.getId());

                storageService.deleteFile(tour.getSource());
            }

            return ResponseEntity.ok().body(new ResponseOnlyMessage("Tours deleted"));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        } 
    }

    @Override
    public ResponseEntity<?> editTour(TourUpdateDTO entity) {
        try {
            Tour tour = new Tour(entity.getId());
            tour.setName(entity.getName());
            tour.setDescription(entity.getDescription());
            tour.setSummary(entity.getSummary());
            tour.setUrl(entity.getUrl());

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
    public ResponseEntity<?> getAllTours() {
        try {
            List<TourClient> tours = tourService.findAll();

            if (tours.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<TourResponse> toursResponse = new ArrayList<>();

            for(TourClient tour : tours) {
                Resource resource = storageService.loadAsResource(tour.source());

                byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<?> getTourById(String id) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
