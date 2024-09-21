package com.client.service_client.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.client.service_client.model.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, String>{ 
    @Query(
        "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM Hotel h " + 
        "WHERE h.id = :hotel")
    Boolean existsHotel(@Param("hotel") String id);

    @Query(value = 
        "SELECT f.source " +
        "FROM Hotel h " +
        "JOIN File f ON h.id = f.hotel_id " +
        "WHERE h.id = :id", nativeQuery = true)
    List<String> getAllFilesHotel(String id);
}
