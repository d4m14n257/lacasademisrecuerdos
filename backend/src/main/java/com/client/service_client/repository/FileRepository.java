package com.client.service_client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.client.service_client.model.File;

public interface FileRepository extends JpaRepository<File, String>{
    @Query(value =
        "UPDATE File " +
        "SET f.hotel_id = :hotel_id " +
        "WHERE f.id = :file_id", nativeQuery = true)
    Void saveFileHotel(String hotel_id, String file_id);
}
