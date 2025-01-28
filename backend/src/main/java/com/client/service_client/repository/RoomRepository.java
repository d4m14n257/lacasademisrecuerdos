package com.client.service_client.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.client.service_client.model.Room;
import com.client.service_client.model.enums.RoomStatus;

public interface RoomRepository extends JpaRepository<Room, String>{ 
    @Query(value = 
        "SELECT r.id, r.name, " + 
        "CASE WHEN :language = 'es' THEN r.summary_es WHEN :language = 'en' THEN r.summary_en " +
        "ELSE r.summary_en END as summary, f.source, f.name as file_name " +
        "FROM Room r " +
        "JOIN File f ON r.id = f.room_id " +
        "WHERE f.main = true " +
        "AND r.status = 'active'", nativeQuery = true)
    List<Object[]> findAllRoomsAvailable(String language);    

    @Query(value = 
        "SELECT r.id, r.name, r.summary_en as summary, r.status, f.source, f.name as file_name " +
        "FROM Room r " +
        "JOIN File f ON r.id = f.room_id " +
        "WHERE f.main = true " +
        "ORDER BY CASE " +
        "WHEN r.status = 'active' THEN 1 " +
        "WHEN r.status = 'hidden' THEN 2 " +
        "ELSE 3 END", nativeQuery = true)
    List<Object[]> findAllRooms();   

    @Query(value = 
        "SELECT r.id, r.name " +
        "FROM Room r", nativeQuery = true)
    List<Object[]> findRoomList();

    @Query(value = 
        "SELECT r.id, r.name, " + 
        "CASE WHEN :language = 'es' THEN r.description_es WHEN :language = 'en' THEN r.description_en " +
        "ELSE r.description_en END as description, " +
        "CASE WHEN :language = 'es' THEN r.summary_es WHEN :language = 'en' THEN r.summary_en " +
        "ELSE r.summary_en END as summary, r.single_price, r.double_price, f.source, f.name as file_name, f.main " +
        "FROM Room r " + 
        "JOIN File f ON r.id = f.room_id " +
        "WHERE r.id = :room " + 
        "AND r.status = 'active' " +
        "ORDER BY f.main DESC", nativeQuery = true)
    List<Object[]> findByIdWithFiles(String room, String language);

    @Query(
        "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM Room r " + 
        "WHERE r.id = :room")
    Boolean existsRoom(@Param("room") String id);

    @Modifying
    @Query(value =
        "UPDATE Room r " +
        "SET r.status = :status " +
        "WHERE r.id = :id")
    void updateStatus(String id, RoomStatus status);

    @Query(value = 
        "SELECT f.source " +
        "FROM Room r " +
        "JOIN File f ON r.id = f.room_id " +
        "WHERE r.id = :id", nativeQuery = true)
    List<String> getAllFilesRoom(String id);

    @Modifying
    @Query(value = 
        "UPDATE Room " +
        "SET name = :name, " +
        "description_es = :description_es, " +
        "description_en = :description_en, " +
        "summary_es = :summary_es, " +
        "summary_en = :summary_en, " +
        "single_price = :singlePrice, " +
        "double_price = :doublePrice " +
        "WHERE id = :id", nativeQuery = true)
    void edit(String id, String name, String description_es, String description_en, String summary_es, String summary_en, Double singlePrice, Double doublePrice);
}
