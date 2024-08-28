package com.client.service_client.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.client.service_client.model.Tour;
import com.client.service_client.model.record.TourClient;
import com.client.service_client.repository.TourRepository;

@Service
public class TourService {
    private final TourRepository tourRepository;

    public TourService (TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public void save (Tour tour) {
        tourRepository.save(tour);
    } 

    public void deleteById(String id) {
        tourRepository.deleteById(id);
    }

    public List<TourClient> findAll() {
        List<Object[]> results = tourRepository.findAllTours();
        List<TourClient> tours = results.stream()
            .map(result -> new TourClient(
                (String) result[0], 
                (String) result[1], 
                (String) result[3], 
                (String) result[4], 
                (String) result[5], 
                (String) result[6]))
            .collect(Collectors.toList());

        return tours;
    }
}
