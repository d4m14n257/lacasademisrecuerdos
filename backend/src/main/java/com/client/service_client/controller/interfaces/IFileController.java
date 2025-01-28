package com.client.service_client.controller.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.model.dto.FileIdDTO;
import com.client.service_client.model.dto.IdDTO;
import com.client.service_client.model.dto.SourceDTO;

import jakarta.validation.Valid;

public interface IFileController {

    @PostMapping("/admin/file/{name}")
    @Transactional
    public ResponseEntity<?> setMainFile (@Valid @PathVariable String name, @RequestPart("data") FileIdDTO entity, @RequestPart("file") MultipartFile file);

    @PutMapping("/admin/file/add/{name}")
    @Transactional
    public ResponseEntity<?> addFile(@Valid @RequestPart("data") IdDTO entity, @RequestPart("files") List<MultipartFile> files, @PathVariable String name);

    @PutMapping("/admin/file/main/{name}")
    @Transactional
    public ResponseEntity<?> changeMainFile (@Valid @RequestBody FileIdDTO entity, @PathVariable String name);

    @DeleteMapping("/admin/file")
    @Transactional
    public ResponseEntity<?> deleteFile (@Valid @RequestBody List<SourceDTO> files);
}
