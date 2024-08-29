package com.client.service_client.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.client.service_client.model.Tour;

public interface TourRepository extends JpaRepository<Tour, String>{
    @Query(value = 
        "SELECT t.id, t.name, t.summary, t.url, f.source, f.name as file_name " + 
        "FROM Tour t " + 
        "JOIN File f ON t.id = f.tour_id " +
        "WHERE f.main = true " +
        "AND t.status = 'used'", nativeQuery = true)
    List<Object[]> findAllTours();

    @Query(
        "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM Tour t " + 
        "WHERE t.id = :tour")
    Boolean existsTour(@Param("tour") String id);

    @Query(value = 
        "SELECT t.id, t.name, t.description, t.summary, t.url, f.source, f.name as file_name " +
        "FROM Tour t " +
        "JOIN File f ON t.id = f.tour_id " +
        "WHERE t.status = 'used' " +
        "AND t.id = :tour")
    Optional<Object[]> findTourById(@Param("tour") String id);
}
