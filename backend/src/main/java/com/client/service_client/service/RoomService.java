package com.client.service_client.service;

import com.client.service_client.repository.RoomRepository;
import com.client.service_client.model.Room;
import com.client.service_client.model.record.FilesInfo;
import com.client.service_client.model.record.RoomClient;
import com.client.service_client.model.record.RoomList;
import com.client.service_client.model.record.RoomWithFiles;

import java.util.ArrayList;
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
                (String) result[4],
                (String) result[5]))
            .collect(Collectors.toList());
        
        return rooms;
    }

    public Optional<RoomWithFiles> findByIdWithFiles(String id) {
        List<Object[]> results = roomRepository.findByIdWithFiles(id);
        
        if (results.isEmpty()) {
            return null;
        }

        List<FilesInfo> files = new ArrayList<>();
        Object[] firstRow = results.get(0);

        String room_id = (String) firstRow[0];
        String name = (String) firstRow[1];
        String description = (String) firstRow[2];
        String summary = (String) firstRow[3];
        String additional = (String) firstRow[4];
        Double singlePrice = (Double) firstRow[5];
        Double doublePrice = (Double) firstRow[6];

        for (Object[] row : results) {
            String source = (String) row[7];
            String fileName = (String) row[8];
            files.add(new FilesInfo(source, fileName));
        }

        return Optional.of(new RoomWithFiles(room_id, name, description, summary, additional, singlePrice, doublePrice, files));
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
