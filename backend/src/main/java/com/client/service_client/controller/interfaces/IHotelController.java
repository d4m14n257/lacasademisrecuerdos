package com.client.service_client.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.model.dto.HotelDTO;
import com.client.service_client.model.dto.HotelUpdateDTO;
import com.client.service_client.model.dto.SourceDTO;

import jakarta.validation.Valid;

@RequestMapping("/api/hotel")
public interface IHotelController {

    @GetMapping
    public ResponseEntity<?> getAllLocations();

    @PostMapping("/admin")
    @Transactional
    public ResponseEntity<?> createHotel(@Valid @RequestPart("data") HotelDTO entity, @RequestPart("file") MultipartFile file);

    @PutMapping("/admin")
    @Transactional
    public ResponseEntity<?> editHotel(@Valid @RequestBody HotelUpdateDTO entity);

    @DeleteMapping("/admin")
    @Transactional
    public ResponseEntity<?> deleteHotel(@RequestBody SourceDTO[] hoteles);
}
