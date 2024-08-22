package com.client.service_client.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.client.service_client.model.Room;
import com.client.service_client.model.record.RoomList;

public interface RoomRepository extends JpaRepository<Room, String>{ 
    @Query(value = 
        "SELECT r.name, r.summary, r.additional, f.source, f.name as file_name " +
        "FROM Room r " +
        "JOIN File f " +
        "ON r.id = f.room_id " +
        "WHERE f.main = true", nativeQuery = true)
    List<Object[]> findAllRooms();    

    @Query(value = 
        "SELECT r.id, r.name " +
        "FROM Room r", nativeQuery = true)
    List<RoomList> findRoomList();
}
