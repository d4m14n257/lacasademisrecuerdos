package com.client.service_client.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.model.dto.HotelDTO;
import com.client.service_client.model.dto.HotelUpdateDTO;

import jakarta.validation.Valid;

public interface IHotelController {

    @GetMapping("/client/hotel")
    public ResponseEntity<?> getAllLocations();

    @PostMapping("/admin/hotel")
    @Transactional
    public ResponseEntity<?> createHotel(@Valid @RequestParam("data") HotelDTO entity, @RequestParam("file") MultipartFile file);

    @PutMapping("/admin/hotel")
    @Transactional
    public ResponseEntity<?> editHotel(@Valid @RequestBody HotelUpdateDTO entity);
}
