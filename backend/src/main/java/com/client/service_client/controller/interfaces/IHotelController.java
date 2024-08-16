package com.client.service_client.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.client.service_client.model.Hotel;
import com.client.service_client.model.dto.HotelDTO;

@RequestMapping("/api/hotel")
public interface IHotelController {

    @GetMapping
    public ResponseEntity<?> getAllLocations();

    @PostMapping("/admin")
    public ResponseEntity<?> createHotel(@RequestBody HotelDTO entity);

    @PutMapping("/admin")
    public ResponseEntity<?> editHotel(@RequestBody Hotel entity);

    @DeleteMapping("/admin")
    public ResponseEntity<?> deleteHotel(@RequestBody String[] ids);
}
