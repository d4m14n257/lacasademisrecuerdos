package com.client.service_client.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.client.service_client.model.dto.HotelDTO;
import com.client.service_client.model.dto.HotelUpdateDTO;

import jakarta.validation.Valid;

@RequestMapping("/api/hotel")
public interface IHotelController {

    @GetMapping
    public ResponseEntity<?> getAllLocations();

    @PostMapping("/admin")
    public ResponseEntity<?> createHotel(@Valid @RequestBody HotelDTO entity);

    @PutMapping("/admin")
    public ResponseEntity<?> editHotel(@Valid @RequestBody HotelUpdateDTO entity);

    @DeleteMapping("/admin")
    public ResponseEntity<?> deleteHotel(@RequestBody String[] ids);
}
