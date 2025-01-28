package com.client.service_client.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.model.dto.TourDTO;
import com.client.service_client.model.dto.TourUpdateDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public interface ITourController {

    @GetMapping("/client/tour")
    public ResponseEntity<?> getAllTours(HttpServletRequest request);

    // @GetMapping("/client/tour/{id}")
    // public ResponseEntity<?> getTourById(HttpServletRequest request, @PathVariable String id);

    @GetMapping("/admin/tour/{id}")
    public ResponseEntity<?> getTourByAdmin(@PathVariable String id);

    @PostMapping("/admin/tour")
    @Transactional
    public ResponseEntity<?> createTour(@Valid @RequestParam("data") TourDTO entity, @RequestParam("file") MultipartFile file);

    @PutMapping("/admin/tour")
    @Transactional
    public ResponseEntity<?> editTour(@Valid @RequestBody TourUpdateDTO entity); 
}
