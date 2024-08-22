package com.client.service_client.service;

import org.springframework.stereotype.Service;

import com.client.service_client.model.File;
import com.client.service_client.repository.FileRepository;


@Service
public class FileService {
    private FileRepository fileRepository;

    public FileService (FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void deleteFileById (String id) {
        fileRepository.deleteFileById(id);
    }

    public void save (File file) {
        fileRepository.save(file);
    }

    public void saveFileHotel (String hotel_id, String file_id) {
        fileRepository.saveFileHotel(hotel_id, file_id);
    }
}
