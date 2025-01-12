package com.client.service_client.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.client.service_client.model.File;
import com.client.service_client.repository.FileRepository;

@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService (FileRepository fileRepository) {
        this.fileRepository = fileRepository;
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
        if(name.equals("room") || name.equals("tour") ) {
            fileRepository.changeMain(id);
        }
        else
            throw new RuntimeException("Name is not valid or incorrent");
    }

    public void setMain(String id) {
        fileRepository.setMain(id);
    }

    public void saveFile(String id, String name, String source, String mime, Long size, Boolean main, String pathId, String pathName) {
        switch (pathName) {
            case "room":
                fileRepository.saveFilesRoom(id, name, source, mime, size, main, pathId);
                break;
            case "hotel":
                fileRepository.saveFileHotel(id, name, source, mime, size, main, pathId);
                break;    
            case "tour":
                fileRepository.saveFilesTour(id, name, source, mime, size, main, pathId);
                break;
            default:
                throw new RuntimeException("Name not found");
        }
    }   

    public String searchSource (String id) {
        return fileRepository.searchSource(id);
    }
}
