package com.client.service_client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.client.service_client.model.File;

public interface FileRepository extends JpaRepository<File, String>{

    @Modifying
    @Query(value =
        "UPDATE File f " +
        "SET f.hotel_id = :hotel " +
        "WHERE f.id = :file", nativeQuery = true)
    void saveFileHotel(String hotel, String file);

    @Modifying
    @Query(value = 
        "DELETE FROM File " +
        "WHERE id = :id", nativeQuery = true)
    void deleteFileById(String id);

    @Modifying
    @Query(value = 
        "UPDATE File f " +
        "SET f.room_id = :room " +
        "WHERE f.id = :file", nativeQuery = true)
    void saveFileRoom(String room, String file);

    @Modifying
    @Query(value = 
        "UPDATE File f " +
        "SET f.tour_id = :tour " +
        "WHERE f.id = :file", nativeQuery = true)
    void saveFileTour(String tour, String file);
}
