package com.client.service_client.service;

import java.util.List;
import java.util.Optional;
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
        if(tourRepository.existsTour(id)) {
            tourRepository.deleteById(id);
        }
        else {
            throw new RuntimeException("Tour not found");
        }
    }

    public List<TourClient> findAll(String language) {
        List<Object[]> results = tourRepository.findAllTours(language);
        List<TourClient> tours = results.stream()
            .map(result -> new TourClient(
                (String) result[0], 
                (String) result[1], 
                (String) result[3], 
                (String) result[4], 
                (String) result[5], 
                (String) result[6],
                (String) result[7]))
            .collect(Collectors.toList());

        return tours;
    }

    public Optional<Tour> findById(String id) {
        return tourRepository.findById(id);
    }

    public List<String> getAllFilesTour(String id) {
        return tourRepository.getAllFilesTour(id);
    }
}
