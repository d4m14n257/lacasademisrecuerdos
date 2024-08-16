package com.client.service_client.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.client.service_client.model.Room;
import com.client.service_client.model.record.RoomRecord;

public interface RoomRepository extends JpaRepository<Room, String>{ 
    @Query(value = "SELECT r.name, r.summary, r.additional, f.source, f.name as file_name " +
                   "FROM Room r " +
                   "JOIN Room_File rf " +
                   "ON r.id = rf.room_id " +
                   "JOIN File f " + 
                   "ON rf.file_id = f.id " +
                   "WHERE rf.main = true", nativeQuery = true)
    Optional<RoomRecord> findAllRooms();   
    
    @Query(value = "SELECT r.*, f.name as file_name, f.source " + 
                   "FROM Room r " +
                   "LEFT JOIN Room_File rf ON r.id = rf.room_id " +
                   "LEFT JOIN File f ON rf.file_id = f.id " +
                   "WHERE r.id = :id", nativeQuery = true)
    Optional<Room> findByIdWithFiles(@Param("id") String id);

    
}
