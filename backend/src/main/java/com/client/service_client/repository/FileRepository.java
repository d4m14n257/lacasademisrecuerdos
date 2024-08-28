package com.client.service_client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query(
        "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM Hotel h " + 
        "WHERE h.id = :hotel")
    Boolean existsHotel(@Param("hotel") String id);

    @Query(
        "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM Room r " + 
        "WHERE r.id = :room")
    Boolean existsRoom(@Param("room") String id);
}
