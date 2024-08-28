package com.client.service_client.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.controller.interfaces.IFileController;
import com.client.service_client.model.File;
import com.client.service_client.model.dto.FileDTO;
import com.client.service_client.model.dto.SourceDTO;
import com.client.service_client.model.response.ResponseOnlyMessage;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.FileService;
import com.client.service_client.storage.StorageService;
import com.client.service_client.util.FileValidator;

@RestController
public class FileController implements IFileController{

    private FileService fileService;
    private StorageService storageService;

    private String source; 

    public FileController (FileService fileService, StorageService storageService) {
        this.fileService = fileService;
        this.storageService = storageService;

        source = null;
    }

    @Override
    public ResponseEntity<?> deleteFile(@RequestBody SourceDTO[] files) {
        try {
            for(SourceDTO file : files) {
                storageService.deleteFile(file.getSource());
                fileService.deleteFileById(file.getId());
            }

            return ResponseEntity.ok().body(new ResponseOnlyMessage("Files deleted"));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> setFile(String name, FileDTO entity, MultipartFile file) {
        try {
            ResponseEntity<?> validationResponse = FileValidator.validateFile(file);

            if (validationResponse != null) {
                return validationResponse;
            }

            source = storageService.store(file, "/" + name);

            File fileSave = new File();
            fileSave.setName(file.getOriginalFilename());
            fileSave.setMime(file.getContentType());
            fileSave.setSize(file.getSize());
            fileSave.setMain(entity.isMain());
            fileSave.setSource(source);

            fileService.save(fileSave);

            switch (name) {
                case "hotel":
                    fileService.saveFileHotel(entity.getId(), fileSave.getId());
                    break;
                case "room":
                    fileService.saveFileRoom(entity.getId(), fileSave.getId());
                    break;
                default:
                    throw new IllegalArgumentException("Name is not valid");
            }

            source = null;
            return ResponseEntity.ok().body(new ResponseOnlyMessage("File saved"));
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
}