package com.client.service_client.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.model.dto.RoomDTO;
import com.client.service_client.model.dto.RoomUpdateDTO;
import com.client.service_client.model.dto.StatusDTO;
import com.client.service_client.model.enums.RoomStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public interface IRoomController {

    @GetMapping("/client/room")
    public ResponseEntity<?> getAllRoomsAvailable(HttpServletRequest request); 

    @GetMapping("/client/room/{id}")
    public ResponseEntity<?> getRoomById(HttpServletRequest request, @PathVariable String id);

    @GetMapping("/client/room/list")
    public ResponseEntity<?> getListRoom();

    @GetMapping("/admin/room")
    public ResponseEntity<?> getAllRooms();

    @GetMapping("/admin/room/{id}")
    public ResponseEntity<?> getRoomByAdmin(@PathVariable String id);

    @PostMapping("/admin/room")
    @Transactional
    public ResponseEntity<?> createRoom(@Valid @RequestPart("data") RoomDTO entity, @RequestPart("file") MultipartFile file);

    @PutMapping("/admin/room/status")
    @Transactional
    public ResponseEntity<?> editStatus(@Valid @RequestBody StatusDTO<RoomStatus> entity);

    @PutMapping("/admin/room") 
    @Transactional
    public ResponseEntity<?> editRoom(@Valid @RequestBody RoomUpdateDTO entity);
}
