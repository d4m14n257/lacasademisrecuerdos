package com.client.service_client.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.client.service_client.model.dto.IdDTO;
import com.client.service_client.model.response.ResponseOnlyMessage;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.HotelService;
import com.client.service_client.service.RoomService;
import com.client.service_client.service.TourService;
import com.client.service_client.storage.StorageService;

import jakarta.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/admin/delete")
public class DeleteController {
    
    private String source;
    private StorageService storageService;
    private HotelService hotelService;
    private RoomService roomService;
    private TourService tourService;

    public DeleteController(HotelService hotelService, RoomService roomService, TourService tourService, StorageService storageService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
        this.storageService = storageService;
        this.source = null;
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteEntity(@Validated @RequestBody @NotEmpty IdDTO[] entity, @PathVariable("name") String name) {
        try {
            switch (name) {
                case "hotel":
                    for (IdDTO hotel : entity) {
                        List<String> files = hotelService.getAllFilesHotel(hotel.getId());
                        hotelService.deleteById(hotel.getId());
                        
                        for(String source : files) {
                            storageService.deleteFile(source);
                        }
                    }

                    return ResponseEntity.ok().body(new ResponseOnlyMessage("Hoteles deleted"));
                case "room":
                    for(IdDTO room : entity) {
                        List<String> files = roomService.getAllFilesRoom(room.getId());
                        roomService.deleteById(room.getId());

                        for(String source : files) {
                            storageService.deleteFile(source);
                        }
                    }
        
                    return ResponseEntity.ok().body(new ResponseOnlyMessage("Rooms deleted"));
                case "tour":
                    for(IdDTO tour : entity) {
                        List<String> files = tourService.getAllFilesTour(tour.getId());
                        tourService.deleteById(tour.getId());

                        for(String source : files) {
                            storageService.deleteFile(source);
                        }
                    }
        
                    return ResponseEntity.ok().body(new ResponseOnlyMessage("Tours deleted"));
            
                default:
                    throw new IllegalArgumentException("Name is not valid");
            }
        }
        catch (IllegalArgumentException e) {
            if(source != null) {
                storageService.deleteFile(source);
            }

            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch(Exception e) {
            if(source != null) {
                storageService.deleteFile(source);
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        } 
    }
}
