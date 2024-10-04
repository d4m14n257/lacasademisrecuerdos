package com.client.service_client.controller.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.model.dto.FileDTO;
import com.client.service_client.model.dto.FileIdDTO;
import com.client.service_client.model.dto.IdDTO;
import com.client.service_client.model.dto.SourceDTO;

import jakarta.validation.Valid;

@RequestMapping("/api/file")
public interface IFileController {

    @PostMapping("/admin/{name}")
    @Transactional
    public ResponseEntity<?> setFile (@Valid @PathVariable String name, @RequestPart("data") FileDTO entity, @RequestPart("file") MultipartFile file);

    @PutMapping("/admin/add/{name}")
    @Transactional
    public ResponseEntity<?> addFile(@Valid @RequestPart("data") IdDTO entity, @RequestPart("files") List<MultipartFile> files, @PathVariable String name);

    @PutMapping("/admin/main/{name}")
    @Transactional
    public ResponseEntity<?> changeMainFile (@Valid @RequestBody FileIdDTO entity, @PathVariable String name);

    @DeleteMapping("/admin")
    @Transactional
    public ResponseEntity<?> deleteFile (@Valid @RequestBody SourceDTO[] files);
}
