package com.client.service_client.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.client.service_client.model.dto.IdDTO;
import com.client.service_client.model.dto.SourceDTO;

import jakarta.validation.Valid;

@RequestMapping("/api/file")
public interface IFileController {

    @PostMapping("/admin/hotel")
    @Transactional
    public ResponseEntity<?> setFileHotel (@Valid @RequestPart("data") IdDTO hotel, @RequestPart("file") MultipartFile file);

    @DeleteMapping("/admin")
    @Transactional
    public ResponseEntity<?> deleteFile (@Valid @RequestBody SourceDTO[] files);
}
