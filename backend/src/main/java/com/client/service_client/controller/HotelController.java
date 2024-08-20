package com.client.service_client.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.controller.interfaces.IHotelController;
import com.client.service_client.model.File;
import com.client.service_client.model.Hotel;
import com.client.service_client.model.dto.HotelDTO;
import com.client.service_client.model.dto.HotelUpdateDTO;
import com.client.service_client.model.dto.SourceDTO;
import com.client.service_client.model.response.ResponseOnlyMessage;
import com.client.service_client.model.response.ResponseWithData;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.HotelService;
import com.client.service_client.storage.StorageService;

@RestController
public class HotelController implements IHotelController {

    private HotelService hotelService;
    private StorageService storageService;
    private final String destination = "/hotel";
    private String source;

    public HotelController (HotelService hotelService, StorageService storageService) {
        this.hotelService = hotelService;
        this.storageService = storageService;
        source = null;
    }

    @Override
    public ResponseEntity<?> createHotel(HotelDTO entity, MultipartFile file) {
        try {
            if(file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseOnlyMessage("File is required"));
            }

            String mimeType = file.getContentType();

            if(mimeType == null) {
                return ResponseEntity.unprocessableEntity().body("Unexpected error");
            }

            if(mimeType.compareTo("image/jpeg") != 0 && mimeType.compareTo("image/png") != 0) {
                return ResponseEntity.badRequest().body(new ResponseOnlyMessage("Formated file is not correct: " + mimeType));
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

            fileSave.setName(file.getName());
            fileSave.setMime(mimeType);
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
    public ResponseEntity<?> deleteHotel(SourceDTO[] hoteles) {
        try {
            for (SourceDTO hotel : hoteles) {
                hotelService.deleteById(hotel.getId());

                storageService.deleteFile(hotel.getSource());
            }

            return ResponseEntity.ok().body(new ResponseOnlyMessage("Hoteles deleted"));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        } 
    }

    @Override
    public ResponseEntity<?> editHotel(HotelUpdateDTO entity) {
        try {
            Hotel hotel = new Hotel(entity.getId());
            hotel.setHotel_name(entity.getId());
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

            return ResponseEntity.ok().body(new ResponseWithData<List<Hotel>>("Request successful", hoteles));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }    
}
