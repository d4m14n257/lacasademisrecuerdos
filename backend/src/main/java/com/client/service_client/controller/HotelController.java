package com.client.service_client.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.client.service_client.controller.interfaces.IHotelController;
import com.client.service_client.model.Hotel;
import com.client.service_client.model.dto.HotelDTO;
import com.client.service_client.model.dto.HotelUpdateDTO;
import com.client.service_client.model.response.ResponseOnlyMessage;
import com.client.service_client.model.response.ResponseWithData;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.HotelService;

@RestController
public class HotelController implements IHotelController {

    private HotelService hotelService;

    public HotelController (HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Override
    public ResponseEntity<?> createHotel(HotelDTO entity) {
        try {
            System.out.println("Entre aqui");
            Hotel hotel = new Hotel();

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

            hotelService.save(hotel);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseOnlyMessage("New hotel created"));

        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        } 
    }

    @Override
    public ResponseEntity<?> deleteHotel(String[] ids) {
        try {
            for (String id : ids) {
                hotelService.deleteById(id);
            }

            return ResponseEntity.ok().body(new ResponseOnlyMessage("Hotel deleted"));
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
