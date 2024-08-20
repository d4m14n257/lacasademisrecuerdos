package com.client.service_client.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.controller.interfaces.IFileController;
import com.client.service_client.model.File;
import com.client.service_client.model.dto.IdDTO;
import com.client.service_client.model.dto.SourceDTO;
import com.client.service_client.model.response.ResponseOnlyMessage;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.FileService;
import com.client.service_client.storage.StorageService;

@RestController
public class FileController implements IFileController{

    private FileService fileService;
    private StorageService storageService;
    private final String hotelDestination = "/hotel";
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
                fileService.deleteFileById(file.getId());
                storageService.deleteFile(file.getSource());
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
    public ResponseEntity<?> setFileHotel(IdDTO hotel, MultipartFile file) {
        try {
            if(file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseOnlyMessage("File is required"));
            }

            String mimeType = file.getContentType();

            if(mimeType == null) {
                return ResponseEntity.unprocessableEntity().body("Unexpected error");
            }

            if(mimeType.compareTo("image/jpeg") != 0 && mimeType.compareTo("image/png") != 0) {
                return ResponseEntity.badRequest().body(new ResponseOnlyMessage("Formated file is not correct: " + mimeType));
            }

            source = storageService.store(file, hotelDestination);

            File fileSave = new File();
            fileSave.setName(file.getName());
            fileSave.setMime(mimeType);
            fileSave.setSize(file.getSize());
            fileSave.setMain(true);

            fileService.save(fileSave);
            fileService.saveFileHotel(hotel.getId(), fileSave.getId());

            return ResponseEntity.ok().body(new ResponseOnlyMessage("File saved"));
        }
        catch (IllegalArgumentException e) {
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
