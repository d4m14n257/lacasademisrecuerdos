package com.client.service_client.service;

import com.client.service_client.repository.RoomRepository;
import com.client.service_client.model.Room;
import com.client.service_client.model.enums.RoomStatus;
import com.client.service_client.model.record.FilesInfo;
import com.client.service_client.model.record.RoomCards;
import com.client.service_client.model.record.RoomList;
import com.client.service_client.model.record.RoomWithFiles;

import java.math.BigDecimal;
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

    public List<RoomCards> findAllAvailable() {
        List<Object[]> results = roomRepository.findAllRoomsAvailable();
        List<RoomCards> rooms = results.stream()
            .map(result -> new RoomCards(
                (String) result[0],
                (String) result[1],
                (String) result[2],
                (String) result[3],
                RoomStatus.valueOf((String) result[4]),
                (String) result[5],
                (String) result[6]))
            .collect(Collectors.toList());
        
        return rooms;
    }

    public List<RoomCards> findAllRooms() {
        List<Object[]> results = roomRepository.findAllRooms();
        List<RoomCards> rooms = results.stream()
            .map(result -> new RoomCards(
                (String) result[0],
                (String) result[1],
                (String) result[2],
                (String) result[3],
                RoomStatus.valueOf((String) result[4]),
                (String) result[5],
                (String) result[6]))
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
        BigDecimal singlePrice = (BigDecimal) firstRow[5];
        BigDecimal doublePrice = (BigDecimal) firstRow[6];

        for (Object[] row : results) {
            String fileName = (String) row[7];
            String source = (String) row[8];
            Boolean main = (Boolean) row[9];
            files.add(new FilesInfo(source, fileName, main));
        }

        return Optional.of(new RoomWithFiles(room_id, name, description, summary, additional, singlePrice, doublePrice, files));
    } 

    public List<RoomList> findRoomList() {
        List<Object[]> results = roomRepository.findRoomList();
        List<RoomList> rooms = new ArrayList<>();

        for(Object[] row : results) {
            rooms.add(new RoomList((String) row[0], (String)row[1]));
        }

        return rooms;
    }

    public void deleteById (String id) {
        if(roomRepository.existsRoom(id)) {
            roomRepository.deleteById(id);
        }
        else {
            throw new RuntimeException("Room not found");
        }
    }

    public void save (Room entity) {
        roomRepository.save(entity);
    }

    public Optional<Room> room(String id) {
        return roomRepository.findById(id);
    }

    public void updateStatus(String id, RoomStatus status) {
        roomRepository.updateStatus(id, status);
    }

    public List<String> getAllFilesRoom(String id) {
        return roomRepository.getAllFilesRoom(id);
    }
}
