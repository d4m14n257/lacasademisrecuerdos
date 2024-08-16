package com.client.service_client.controller;

import org.springframework.web.bind.annotation.RestController;

import com.client.service_client.controller.interfaces.IRoomController;
import com.client.service_client.model.Room;
import com.client.service_client.model.record.RequestRoom;
import com.client.service_client.model.record.RoomRecord;
import com.client.service_client.model.response.ResponseWithData;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.RoomService;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class RoomController implements IRoomController{

    private RoomService roomService;
    
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Override
    public ResponseEntity<?> getRoomById(@PathVariable String id) {
        try {
            Optional<Room> room = roomService.findByIdWithFiles(id);

            if(!room.isPresent()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(new ResponseWithData<>("Request successful", room));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> getAllRooms() {
        try {
            Optional<RoomRecord> rooms = roomService.findAll();

            if (!rooms.isPresent()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(new ResponseWithData<>("Request successful", rooms));
        } 
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> createRoom(RequestRoom entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<?> deleteRoom(String[] ids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<?> editRoom(Room entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<?> getListRoom() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<?> getRoomByAdmin(String id) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
