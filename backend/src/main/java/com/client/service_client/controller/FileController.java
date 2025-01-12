package com.client.service_client.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.controller.interfaces.IFileController;
import com.client.service_client.model.File;
import com.client.service_client.model.dto.FileIdDTO;
import com.client.service_client.model.dto.IdDTO;
import com.client.service_client.model.dto.SourceDTO;
import com.client.service_client.model.response.ResponseOnlyMessage;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.FileService;
import com.client.service_client.storage.StorageService;
import com.client.service_client.util.CustomIdGenerator;
import com.client.service_client.util.FileValidator;

@RestController
public class FileController implements IFileController{

    private FileService fileService;
    private StorageService storageService;

    private String source; 
    private List<String> multipleSource;

    public FileController (FileService fileService, StorageService storageService) {
        this.fileService = fileService;
        this.storageService = storageService;

        source = null;
        multipleSource = new ArrayList<>();
    }

    @Override
    public ResponseEntity<?> deleteFile(@RequestBody List<SourceDTO> files) {
        try {
            boolean main = false;

            for(SourceDTO file : files) {

                if(!file.getMain()) {
                    storageService.deleteFile(file.getSource());
                    fileService.deleteFileById(file.getId());
                }
                else {
                    main = true;
                }
            }

            return ResponseEntity.ok().body(new ResponseOnlyMessage(main ? "Files deleted. Remember can not delete main image" : "Files deleted"));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> addFile(IdDTO entity, List<MultipartFile> files, String name) {
        try {
            if(!name.equals("room") && !name.equals("tour"))
                throw new IllegalArgumentException("Name is not valid");

            for(MultipartFile file : files) {
                ResponseEntity<?> validationResponse = FileValidator.validateFile(file);

                if(validationResponse != null) {
                    return validationResponse;
                }
            }
            
            List<File> filesSave = new ArrayList<>();

            for(MultipartFile file : files) {
                String tempSource = storageService.store(file, "/" + name);
                multipleSource.add(tempSource);

                File fileSave = new File();
                fileSave.setName(file.getOriginalFilename());
                fileSave.setMime(file.getContentType());
                fileSave.setSize(file.getSize());
                fileSave.setMain(false);
                fileSave.setSource(tempSource);

                filesSave.add(fileSave);
            }

            fileService.saveAllFiles(filesSave, name, entity.getId());
            multipleSource.clear();
            return ResponseEntity.ok().body(new ResponseOnlyMessage("Files saved"));
        }
        catch (IllegalArgumentException e) {
            if(multipleSource.size() > 0) {
                for(String origin : multipleSource) {
                    storageService.deleteFile(origin);
                }

                multipleSource.clear();
            }

            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            if(multipleSource.size() > 0) {
                for(String origin : multipleSource) {
                    storageService.deleteFile(origin);
                }

                multipleSource.clear();
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> setMainFile(String name, FileIdDTO entity, MultipartFile file) {
        try {
            if(!name.equals("room") && !name.equals("hotel") && !name.equals("tour")) {
                throw new IllegalArgumentException("Name is not valid");
            }

            ResponseEntity<?> validationResponse = FileValidator.validateFile(file);

            if (validationResponse != null) {
                return validationResponse;
            }

            source = storageService.store(file, "/" + name);
            fileService.saveFile(
                CustomIdGenerator.generate(12), file.getOriginalFilename(), source, file.getContentType(), file.getSize(), true, 
                entity.getId(), name);

            if(name.equals("room") || name.equals("tour")) {
                fileService.changeMain(name, entity.getFile_id());
            }
            else {
                String sourceFile = fileService.searchSource(entity.getFile_id());
                fileService.deleteFileById(entity.getFile_id());
                storageService.deleteFile(sourceFile);
            }

            source = null;
            return ResponseEntity.ok().body(new ResponseOnlyMessage("File saved"));
        }
        catch (IllegalArgumentException e) {
            if(source != null) {
                storageService.deleteFile(source);

                source = null;
            }

            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            if(source != null) {
                storageService.deleteFile(source);

                source = null;
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> changeMainFile(FileIdDTO entity, String name) {
        try {
            if(name.equals("room") || name.equals("tour"))
                fileService.changeMain(name, entity.getId());
            else 
                throw new IllegalArgumentException("Name is not valid");

            fileService.setMain(entity.getFile_id());

            return ResponseEntity.ok().body(new ResponseOnlyMessage("Main image changed"));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }
}