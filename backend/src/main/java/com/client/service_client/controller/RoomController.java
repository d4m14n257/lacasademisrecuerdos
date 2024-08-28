package com.client.service_client.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.controller.interfaces.IRoomController;
import com.client.service_client.model.File;
import com.client.service_client.model.Room;
import com.client.service_client.model.dto.RoomDTO;
import com.client.service_client.model.dto.RoomUpdateDTO;
import com.client.service_client.model.dto.SourceDTO;
import com.client.service_client.model.record.RoomClient;
import com.client.service_client.model.record.RoomList;
import com.client.service_client.model.record.RoomResponse;
import com.client.service_client.model.record.RoomWithFiles;
import com.client.service_client.model.response.ResponseOnlyMessage;
import com.client.service_client.model.response.ResponseWithData;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.RoomService;
import com.client.service_client.storage.StorageService;
import com.client.service_client.util.FileValidator;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class RoomController implements IRoomController{

    private RoomService roomService;
    private StorageService storageService;
    private final String destination = "/room";
    private String source;
    
    public RoomController(RoomService roomService, StorageService storageService) {
        this.roomService = roomService;
        this.storageService = storageService;

        source = null;
    }

    @Override
    public ResponseEntity<?> getRoomById(@PathVariable String id) {
        try {
            Optional<RoomWithFiles> room = roomService.findByIdWithFiles(id);

            if(room == null) {
                return ResponseEntity.noContent().build();
            }

            //TODO: Convertir en binarios los archivos para lectura de cliente

            return ResponseEntity.ok().body(new ResponseWithData<RoomWithFiles>("Request successful", room.get()));
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
            List<RoomClient> rooms = roomService.findAll();

            if (rooms.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<RoomResponse> roomsResponse = new ArrayList<>();

            for(RoomClient room : rooms) {
                Resource resource = storageService.loadAsResource(room.source());

                byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());

                RoomResponse roomResponse = new RoomResponse(
                    room.id(),
                    room.name(),
                    room.summary(),
                    room.additional(),
                    room.file_name(),
                    fileContent);
                roomsResponse.add(roomResponse);
            }

            return ResponseEntity.ok().body(new ResponseWithData<List<RoomResponse>>("Request successful", roomsResponse));
        } 
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> createRoom(RoomDTO entity, MultipartFile file) {
        try{
            ResponseEntity<?> validationResponse = FileValidator.validateFile(file);

            if (validationResponse != null) {
                return validationResponse;
            }

            Room room = new Room();
            File fileSave = new File();

            room.setName(entity.getName());
            room.setDescription(entity.getDescription());
            room.setSummary(entity.getSummary());
            room.setAdditional(entity.getAdditional());
            room.setSingle_price(entity.getSingle_price());
            room.setDouble_price(entity.getDouble_price());
            room.setFiles(new HashSet<File>());
            room.getFiles().add(fileSave);

            fileSave.setName(file.getOriginalFilename());
            fileSave.setMime(file.getContentType());
            fileSave.setSize(file.getSize());
            fileSave.setMain(true);
            fileSave.setRoom(room);

            this.source = storageService.store(file, this.destination);
            fileSave.setSource(this.source);

            roomService.save(room);
            source = null;

            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseOnlyMessage("New room created"));
        }
        catch (IllegalArgumentException e) {
            if(source != null) {
                storageService.deleteFile(source);
            }

            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            if(source != null) {
                storageService.deleteFile(source);
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> deleteRoom(SourceDTO[] rooms) {
        try {
            for(SourceDTO room : rooms) {
                roomService.deleteById(room.getId());

                storageService.deleteFile(room.getSource());
            }

            return ResponseEntity.ok().body(new ResponseOnlyMessage("Rooms deleted"));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        } 
    }

    @Override
    public ResponseEntity<?> editRoom(RoomUpdateDTO entity) {
        try {
            Room room = new Room(entity.getId());
            room.setName(entity.getName());
            room.setDescription(entity.getDescription());
            room.setSummary(entity.getSummary());
            room.setAdditional(entity.getAdditional());
            room.setSingle_price(entity.getSingle_price());
            room.setDouble_price(entity.getDouble_price());

            roomService.save(room);
            return ResponseEntity.ok().body(new ResponseOnlyMessage("Room updated"));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }     
    }

    @Override
    public ResponseEntity<?> getListRoom() {
        try {
            List<RoomList> rooms = roomService.findRoomList();

            if(rooms.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(new ResponseWithData<List<RoomList>>("Request successful", rooms));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }  
    }

    @Override
    public ResponseEntity<?> getRoomByAdmin(String id) {
        try {
            Optional<Room> room = roomService.room(id);

            if(!room.isPresent()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(new ResponseWithData<Room>("Request successful", room.get()));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }  
    }
}
