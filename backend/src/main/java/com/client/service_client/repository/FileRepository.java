package com.client.service_client.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.client.service_client.model.File;

public interface FileRepository extends JpaRepository<File, String>{
    @Query(value =
        "SELECT f.name, f.source, rf.main" +
        "FROM Room_File rf" +
        "JOIN File f ON rf.file_id = f.id" +
        "WHERE rf.room_id = :id",
        nativeQuery = true)
    List<File> findFilebyRoom(String id);
}
