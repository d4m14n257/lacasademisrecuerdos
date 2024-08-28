package com.client.service_client.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.model.dto.SourceDTO;
import com.client.service_client.model.dto.TourDTO;
import com.client.service_client.model.dto.TourUpdateDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@RequestMapping("/api/tour")
public interface ITourController {

    @GetMapping
    public ResponseEntity<?> getAllTours();

    @GetMapping("/{id}")
    public ResponseEntity<?> getTourById(@PathVariable String id);

    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getTourByAdmin(@PathVariable String id);

    @PostMapping("/admin")
    @Transactional
    public ResponseEntity<?> createTour(@Valid @RequestPart("data") TourDTO entity, @RequestPart("file") MultipartFile file);

    @PutMapping("/admin")
    @Transactional
    public ResponseEntity<?> editTour(@Valid @RequestBody TourUpdateDTO entity);

    @DeleteMapping("/admin")
    @Transactional
    public ResponseEntity<?> deleteTour(@Valid @NotEmpty @RequestBody SourceDTO[] tours);    
}
