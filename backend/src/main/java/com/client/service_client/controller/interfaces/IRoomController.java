package com.client.service_client.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.model.dto.RoomDTO;
import com.client.service_client.model.dto.RoomUpdateDTO;
import com.client.service_client.model.dto.StatusDTO;
import com.client.service_client.model.enums.RoomStatus;

import jakarta.validation.Valid;

@RequestMapping("/api/room")
public interface IRoomController {

    @GetMapping
    public ResponseEntity<?> getAllRoomsAvailable(); 

    @GetMapping("/client/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable String id);

    @GetMapping("/list")
    public ResponseEntity<?> getListRoom();

    @GetMapping("/admin")
    public ResponseEntity<?> getAllRooms();

    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getRoomByAdmin(@PathVariable String id);

    @PostMapping("/admin")
    @Transactional
    public ResponseEntity<?> createRoom(@Valid @RequestPart("data") RoomDTO entity, @RequestPart("file") MultipartFile file);

    @PutMapping("/admin/status")
    @Transactional
    public ResponseEntity<?> editStatus(@Valid @RequestBody StatusDTO<RoomStatus> entity);

    @PutMapping("/admin") 
    @Transactional
    public ResponseEntity<?> editRoom(@Valid @RequestBody RoomUpdateDTO entity);
}
