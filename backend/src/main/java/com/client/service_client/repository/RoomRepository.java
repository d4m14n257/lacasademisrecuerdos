package com.client.service_client.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.client.service_client.model.Room;

public interface RoomRepository extends JpaRepository<Room, String>{ 
    @Query(value = 
        "SELECT r.id, r.name, r.summary, r.additional, f.source, f.name as file_name " +
        "FROM Room r " +
        "JOIN File f ON r.id = f.room_id " +
        "WHERE f.main = true", nativeQuery = true)
    List<Object[]> findAllRooms();    

    @Query(value = 
        "SELECT r.id, r.name " +
        "FROM Room r", nativeQuery = true)
    List<Object[]> findRoomList();

    @Query(value = 
        "SELECT r.id, r.name, r.description, r.summary, r.additional, r.single_price, r.double_price, f.source, f.name as file_name, f.main " + 
        "FROM Room r " + 
        "JOIN File f ON r.id = f.room_id " +
        "WHERE r.id = :room", nativeQuery = true)
    List<Object[]> findByIdWithFiles(String room);

    @Query(
        "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM Room r " + 
        "WHERE r.id = :room")
    Boolean existsRoom(@Param("room") String id);
}
