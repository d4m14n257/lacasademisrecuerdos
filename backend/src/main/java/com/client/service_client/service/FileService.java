package com.client.service_client.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.client.service_client.model.File;
import com.client.service_client.repository.FileRepository;
import com.client.service_client.repository.HotelRepository;
import com.client.service_client.repository.RoomRepository;
import com.client.service_client.repository.TourRepository;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final TourRepository tourRepository;

    public FileService (RoomRepository roomRepository, HotelRepository hotelRepository, TourRepository tourRepository, FileRepository fileRepository) {
        this.fileRepository = fileRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.tourRepository = tourRepository;
    }

    public void deleteFileById(String id) {
        fileRepository.deleteFileById(id);
    }

    public void save(File file) {
        fileRepository.save(file);
    }

    public void saveAllFiles(List<File> files, String name, String id) {
        for(File file : files) {
            if(name.equals("room")) {
                fileRepository.saveFilesRoom(file.getId(), file.getName(), file.getSource(), file.getMime(), file.getSize(), file.getMain(), id);
            }
            else if(name.equals("tour")) {
                fileRepository.saveFilesTour(file.getId(), file.getName(), file.getSource(), file.getMime(), file.getSize(), file.getMain(), id);
            }
            else {
                throw new RuntimeException("Name is not valid or incorrent");
            }
        }
    }

    public void changeMain(String name, String id) {
        if(name.equals("room")) {
            fileRepository.changeMainRoom(id);
        }
        else if(name.equals("tour")) {
            fileRepository.changeMainTour(id);
        }
        else
            throw new RuntimeException("Name is not valid or incorrent");
    }

    public void setMain(String id) {
        fileRepository.setMain(id);
    }

    public void saveFileHotel(String hotel_id, String file_id) {
        if(hotelRepository.existsHotel(hotel_id))
            fileRepository.saveFileHotel(hotel_id, file_id);
        else
            throw new RuntimeException("Hotel not found");
    }

    public void saveFileRoom(String room_id, String file_id) {
        if(roomRepository.existsRoom(room_id))
            fileRepository.saveFileRoom(room_id, file_id);
        else
            throw new RuntimeException("Room not found");
    }

    public void saveFileTour(String tour_id, String file_id) {
        if(tourRepository.existsTour(tour_id))
            fileRepository.saveFileTour(tour_id, file_id);
        else
            throw new RuntimeException("Tour not found");
    }
}
