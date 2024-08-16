package com.client.service_client.service;

import com.client.service_client.repository.RoomRepository;
import com.client.service_client.model.Room;
import com.client.service_client.model.record.RoomRecord;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Optional<RoomRecord> findAll() {
        Optional<RoomRecord> rooms = roomRepository.findAllRooms();

        return rooms;
    }

    public Optional<Room> findByIdWithFiles(String id) {
        return roomRepository.findByIdWithFiles(id);
    } 

    public void saveRoom (Room entity) {
        roomRepository.save(entity);
    }
}
