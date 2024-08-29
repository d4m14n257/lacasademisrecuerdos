package com.client.service_client.controller;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.controller.interfaces.IHotelController;
import com.client.service_client.model.File;
import com.client.service_client.model.Hotel;
import com.client.service_client.model.dto.HotelDTO;
import com.client.service_client.model.dto.HotelUpdateDTO;
import com.client.service_client.model.record.FilesBytes;
import com.client.service_client.model.record.HotelWithFile;
import com.client.service_client.model.response.ResponseOnlyMessage;
import com.client.service_client.model.response.ResponseWithData;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.HotelService;
import com.client.service_client.storage.StorageService;
import com.client.service_client.util.FileValidator;

@RestController
public class HotelController implements IHotelController {

    private HotelService hotelService;
    private StorageService storageService;
    private final String destination = "/hotel";
    private String source;

    public HotelController (HotelService hotelService, StorageService storageService) {
        this.hotelService = hotelService;
        this.storageService = storageService;
        this.source = null;
    }

    @Override
    public ResponseEntity<?> createHotel(HotelDTO entity, MultipartFile file) {
        try {
            ResponseEntity<?> validationResponse = FileValidator.validateFile(file);

            if (validationResponse != null) {
                return validationResponse;
            }

            Hotel hotel = new Hotel();
            File fileSave = new File();

            hotel.setHotel_name(entity.getHotel_name());
            hotel.setStreet_name(entity.getStreet_name());
            hotel.setNeighborhood(entity.getNeighborhood());
            hotel.setStreet_number(entity.getStreet_number());
            hotel.setPostal_code(entity.getPostal_code());
            hotel.setPhone_number(entity.getPhone_number());
            hotel.setEmail(entity.getEmail());
            hotel.setLatitude(entity.getLatitude());
            hotel.setLongitude(entity.getLongitude());
            hotel.setUrl(entity.getUrl());
            hotel.setFile(fileSave);

            fileSave.setName(file.getOriginalFilename());
            fileSave.setMime(file.getContentType());
            fileSave.setSize(file.getSize());
            fileSave.setMain(true);
            fileSave.setHotel(hotel);

            this.source = storageService.store(file, this.destination);
            fileSave.setSource(this.source);

            hotelService.save(hotel);

            source = null;
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseOnlyMessage("New hotel created"));

        }
        catch (IllegalArgumentException e) {
            if(source != null) {
                storageService.deleteFile(source);
            }

            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch(Exception e) {
            if(source != null) {
                storageService.deleteFile(source);
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        } 
    }

    @Override
    public ResponseEntity<?> editHotel(HotelUpdateDTO entity) {
        try {
            Hotel hotel = new Hotel(entity.getId());
            hotel.setHotel_name(entity.getHotel_name());
            hotel.setStreet_name(entity.getStreet_name());
            hotel.setNeighborhood(entity.getNeighborhood());
            hotel.setStreet_number(entity.getStreet_number());
            hotel.setPostal_code(entity.getPostal_code());
            hotel.setPhone_number(entity.getPhone_number());
            hotel.setEmail(entity.getEmail());
            hotel.setLatitude(entity.getLatitude());
            hotel.setLongitude(entity.getLongitude());
            hotel.setUrl(entity.getUrl());
            hotel.setCreated_at(entity.getCreated_at());

            hotelService.save(hotel);
            return ResponseEntity.ok().body(new ResponseOnlyMessage("Hotel updated"));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> getAllLocations() {
        try {
            List<Hotel> hoteles = hotelService.getAllHoteles();

            if(hoteles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<HotelWithFile> hotelesWithFile = new ArrayList<>();

            for(Hotel hotelWithoutFile : hoteles) {
                Resource resource = storageService.loadAsResource(hotelWithoutFile.getFile().getSource());

                byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
                FilesBytes file = new FilesBytes(hotelWithoutFile.getFile().getName(), hotelWithoutFile.getFile().getMain(), fileContent);

                hotelesWithFile.add(new HotelWithFile(
                    hotelWithoutFile.getId(), 
                    hotelWithoutFile.getHotel_name(), 
                    hotelWithoutFile.getStreet_name(), 
                    hotelWithoutFile.getNeighborhood(), 
                    hotelWithoutFile.getStreet_number(), 
                    hotelWithoutFile.getPostal_code(), 
                    hotelWithoutFile.getPhone_number(), 
                    hotelWithoutFile.getEmail(), 
                    hotelWithoutFile.getLatitude(), 
                    hotelWithoutFile.getLongitude(), 
                    hotelWithoutFile.getUrl(), 
                    file));
            }

            return ResponseEntity.ok().body(new ResponseWithData<List<HotelWithFile>>("Request successful", hotelesWithFile));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }    
}
