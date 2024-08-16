package com.client.service_client.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.client.service_client.model.Tour;
import com.client.service_client.model.record.RequestTour;

@RequestMapping("/api/tour")
public interface ITourController {

    @GetMapping
    public ResponseEntity<?> getAllTours();

    @GetMapping("/{id}")
    public ResponseEntity<?> getTourById(@PathVariable String id);

    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getTourByAdmin(@PathVariable String id);

    @PostMapping("/admin")
    public ResponseEntity<?> createTour(@RequestBody RequestTour entity);

    @PutMapping("/admin")
    public ResponseEntity<?> editTour(@RequestBody Tour entity);

    @DeleteMapping("/admin")
    public ResponseEntity<?> deleteTour(@RequestBody String ids);    
}
