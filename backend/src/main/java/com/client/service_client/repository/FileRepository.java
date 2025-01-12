package com.client.service_client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.client.service_client.model.File;

public interface FileRepository extends JpaRepository<File, String>{
    
    @Query(value = 
        "SELECT source " +
        "FROM File f " +
        "WHERE f.id = :id ", nativeQuery = true)
    String searchSource(String id);

    @Modifying
    @Query(value = 
        "DELETE FROM File " +
        "WHERE id = :id", nativeQuery = true)
    void deleteFileById(String id);

    @Modifying
    @Query(value = 
        "UPDATE File f " +
        "SET f.main = false " +
        "WHERE f.id = :id", nativeQuery = true)
    void changeMain(String id);

    @Modifying
    @Query(value =
        "UPDATE File f " +
        "SET f.main = true " + 
        "WHERE f.id = :id", nativeQuery = true)
    void setMain(String id);

    @Modifying
    @Query(value = 
        "INSERT INTO File " + 
        "(id, name, source, mime, size, main, room_id)" +
        "VALUES (:id, :name, :source, :mime, :size, :main, :room_id)", nativeQuery = true)
    void saveFilesRoom(String id, String name, String source, String mime, Long size, Boolean main, String room_id);

    @Modifying
    @Query(value = 
        "INSERT INTO File " + 
        "(id, name, source, mime, size, main, tour_id)" +
        "VALUES (:id, :name, :source, :mime, :size, :main, :tour_id)", nativeQuery = true)
    void saveFilesTour(String id, String name, String source, String mime, Long size, Boolean main, String tour_id);

    @Modifying
    @Query(value = 
        "INSERT INTO File " + 
        "(id, name, source, mime, size, main, hotel_id)" +
        "VALUES (:id, :name, :source, :mime, :size, :main, :hotel_id)", nativeQuery = true)
    void saveFileHotel(String id, String name, String source, String mime, Long size, Boolean main, String hotel_id);
}
