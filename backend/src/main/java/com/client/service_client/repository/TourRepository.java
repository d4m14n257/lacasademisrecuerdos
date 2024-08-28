package com.client.service_client.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.client.service_client.model.Tour;

public interface TourRepository extends JpaRepository<Tour, String>{
    @Query(value = 
        "SELECT t.id, t.name, t.summary, t.url, f.source, f.name as file_name " + 
        "FROM Tour t " + 
        "JOIN File f ON t.id = f.tour_id " +
        "WHERE f.main = true " +
        "AND t.status = 'used'", nativeQuery = true)
    List<Object[]> findAllTours();
}
