package com.client.service_client.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.controller.interfaces.IRoomController;
import com.client.service_client.model.File;
import com.client.service_client.model.Room;
import com.client.service_client.model.dto.RoomDTO;
import com.client.service_client.model.dto.RoomUpdateDTO;
import com.client.service_client.model.dto.StatusDTO;
import com.client.service_client.model.enums.RoomStatus;
import com.client.service_client.model.record.FileAdminBytes;
import com.client.service_client.model.record.FilesBytes;
import com.client.service_client.model.record.FilesInfo;
import com.client.service_client.model.record.RoomCards;
import com.client.service_client.model.record.RoomList;
import com.client.service_client.model.record.RoomResponse;
import com.client.service_client.model.record.RoomWithFiles;
import com.client.service_client.model.record.RoomWithFilesAdmin;
import com.client.service_client.model.record.RoomWithFilesBytes;
import com.client.service_client.model.response.ResponseOnlyMessage;
import com.client.service_client.model.response.ResponseWithData;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.RoomService;
import com.client.service_client.storage.StorageService;
import com.client.service_client.util.Constants;
import com.client.service_client.util.FileValidator;
import com.client.service_client.util.ImageResize;

import io.github.mojtabaJ.cwebp.WebpConverter;
import jakarta.validation.Valid;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    public ResponseEntity<?> getRoomById(String id) {
        try {
            Optional<RoomWithFiles> room = roomService.findByIdWithFiles(id);
            
            if(!room.isPresent()) {
                return ResponseEntity.noContent().build();
            }
            
            RoomWithFiles roomResults = room.get();
            List<FilesBytes> files = new ArrayList<>();

            for(FilesInfo fileWithoutByte : roomResults.files()) {
                Resource resource = storageService.loadAsResource(fileWithoutByte.source());

                byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());

                files.add(new FilesBytes(fileWithoutByte.file_name(), fileWithoutByte.main(), fileContent));
            }

            RoomWithFilesBytes roomFiles = new RoomWithFilesBytes(
                roomResults.id(), 
                roomResults.name(),
                roomResults.description(),
                roomResults.summary(),
                roomResults.additional(),
                roomResults.single_price(),
                roomResults.double_price(),
                files
            );

            return ResponseEntity.ok().body(new ResponseWithData<RoomWithFilesBytes>("Request successful", roomFiles));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> getAllRoomsAvailable() {
        try {
            List<RoomCards> rooms = roomService.findAllAvailable();

            if (rooms.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<RoomResponse> roomsResponse = new ArrayList<>();

            for(RoomCards room : rooms) {
                Resource resource = storageService.loadAsResource(room.source());

                byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());

                RoomResponse roomResponse = new RoomResponse(
                    room.id(),
                    room.name(),
                    room.summary(),
                    room.additional(),
                    room.status(),
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
    public ResponseEntity<?> createRoom(@Valid RoomDTO entity, MultipartFile file) {
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

            this.source = storageService.store(file, this.destination);
            
            room.getFiles().add(fileSave);
            fileSave.setName(file.getOriginalFilename());
            fileSave.setMime(file.getContentType());
            fileSave.setSize(file.getSize());
            fileSave.setMain(true);
            fileSave.setRoom(room);
            fileSave.setSource(this.source);

            roomService.save(room);
            source = null;

            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseOnlyMessage("New room created"));
        }
        catch (IllegalArgumentException e) {
            if(source != null) {
                storageService.deleteFile(source);

                source = null;
            }

            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            System.out.println(source);

            if(source != null) {
                storageService.deleteFile(source);

                source = null;
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> editRoom(@Valid RoomUpdateDTO entity) {
        try {
            roomService.edit(entity);
            return ResponseEntity.ok().body(new ResponseOnlyMessage("Room updated"));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
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

            List<FileAdminBytes> files = new ArrayList<>();

            for(File fileBytes : room.get().getFiles()) {

                byte[] fileContent = WebpConverter.imageByteToWebpByte(ImageResize.resizeImageToBytes(fileBytes.getSource(), Constants.thumbnails), Constants.qualityThumbnails);
                
                if(fileBytes.getMain())
                    files.addFirst(new FileAdminBytes(fileBytes.getId(), fileBytes.getName(), fileBytes.getSource(), fileBytes.getMime(), fileBytes.getMain(), fileContent));
                else
                    files.addLast(new FileAdminBytes(fileBytes.getId(), fileBytes.getName(), fileBytes.getSource(), fileBytes.getMime(), fileBytes.getMain(), fileContent));
            }

            Room getRoom = room.get();

            return ResponseEntity.ok().body(new ResponseWithData<RoomWithFilesAdmin>(
                "Request successful", 
                new RoomWithFilesAdmin(
                    getRoom.getId(), getRoom.getName(), getRoom.getDescription(), getRoom.getSummary(),
                    getRoom.getAdditional(), getRoom.getSingle_price(), getRoom.getDouble_price(),
                    getRoom.getCreated_at(), getRoom.getStatus(), files)
            ));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }  
    }

    @Override
    public ResponseEntity<?> editStatus(@Valid StatusDTO<RoomStatus> entity) {
        try {
            if(entity.getStatus() == RoomStatus.hidden || entity.getStatus() ==  RoomStatus.active) {
                roomService.updateStatus(entity.getId(), entity.getStatus());
            }
            else {
                throw new IllegalArgumentException("Status is not valid");
            }

            return ResponseEntity.ok().body(new ResponseOnlyMessage("Request successful"));
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
            List<RoomCards> rooms = roomService.findAllRooms();

            if (rooms.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<RoomResponse> roomsResponse = new ArrayList<>();

            for(RoomCards room : rooms) {

                byte[] fileContent = WebpConverter.imageByteToWebpByte(ImageResize.resizeImageToBytes(room.source(), Constants.thumbnailsGeneral), Constants.quality);

                RoomResponse roomResponse = new RoomResponse(
                    room.id(),
                    room.name(),
                    room.summary(),
                    room.additional(),
                    room.status(),
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
}
