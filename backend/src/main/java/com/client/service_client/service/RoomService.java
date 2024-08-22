package com.client.service_client.service;

import com.client.service_client.repository.RoomRepository;
import com.client.service_client.model.Room;
import com.client.service_client.model.record.RoomClient;
import com.client.service_client.model.record.RoomList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<RoomClient> findAll() {
        List<Object[]> results = roomRepository.findAllRooms();
        List<RoomClient> rooms = results.stream()
            .map(result -> new RoomClient(
                (String) result[0],
                (String) result[1],
                (String) result[2],
                (String) result[3],
                (String) result[4]))
            .collect(Collectors.toList());
        
        return rooms;
    }

    public Optional<Room> findByIdWithFiles(String id) {
        return roomRepository.findById(id);
    } 

    public List<RoomList> findRoomList() {
        return roomRepository.findRoomList();
    }

    public void deleteById (String id) {
        roomRepository.deleteById(id);
    }

    public void save (Room entity) {
        roomRepository.save(entity);
    }
}
