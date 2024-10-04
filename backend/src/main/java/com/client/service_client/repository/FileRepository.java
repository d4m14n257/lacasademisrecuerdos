package com.client.service_client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.client.service_client.model.File;

public interface FileRepository extends JpaRepository<File, String>{
    
    @Modifying
    @Query(value = 
        "DELETE FROM File " +
        "WHERE id = :id", nativeQuery = true)
    void deleteFileById(String id);

    @Modifying
    @Query(value =
        "UPDATE File f " +
        "SET f.hotel_id = :hotel " +
        "WHERE f.id = :file", nativeQuery = true)
    void saveFileHotel(String hotel, String file);

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

    @Modifying
    @Query(value = 
        "UPDATE File f " +
        "SET f.main = false " +
        "WHERE f.main = true " +
        "AND f.room_id = :id", nativeQuery = true)
    void changeMainRoom(String id);

    @Modifying
    @Query(value = 
        "UPDATE File f " +
        "SET f.main = false " +
        "WHERE f.main = true " +
        "AND f.tour_id = :id", nativeQuery = true)
    void changeMainTour(String id);

    @Modifying
    @Query(value =
        "UPDATE File f " +
        "SET t.main = true " + 
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
}
