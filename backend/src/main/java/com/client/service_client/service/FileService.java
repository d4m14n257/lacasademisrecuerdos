package com.client.service_client.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.client.service_client.model.File;
import com.client.service_client.repository.FileRepository;


@Service
public class FileService {
    private FileRepository fileRepository;

    public FileService (FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public void deleteFileById (String id) {
        fileRepository.deleteById(id);
    }

    @Transactional
    public void save (File file) {
        fileRepository.save(file);
    }

    @Transactional
    public void saveFileHotel (String hotel_id, String file_id) {
        fileRepository.saveFileHotel(hotel_id, file_id);
    }
}
