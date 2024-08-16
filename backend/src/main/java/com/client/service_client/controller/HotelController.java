package com.client.service_client.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.client.service_client.controller.interfaces.IHotelController;
import com.client.service_client.model.Hotel;
import com.client.service_client.model.dto.HotelDTO;
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
            Hotel hotel = new Hotel();
            hotel.setHotel_name(null);
            hotel.setStreet_name(null);
            hotel.setNeighborhood(null);
            hotel.setStreet_number(null);
            hotel.setPostal_code(null);
            hotel.setPhone_number(null);
            hotel.setHotel_name(null);

        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        } 
    }

    @Override
    public ResponseEntity<?> deleteHotel(String[] ids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<?> editHotel(Hotel entity) {
        // TODO Auto-generated method stub
        return null;
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
