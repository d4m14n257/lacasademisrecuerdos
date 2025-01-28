package com.client.service_client.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.client.service_client.model.dto.ContactDTO;
import com.client.service_client.model.dto.IdDTO;

import jakarta.validation.Valid;

public interface IContactController {

    @PostMapping("/client/contact")
    public ResponseEntity<?> sendClientContact(@Valid @RequestBody ContactDTO entity);

    @GetMapping("/admin/contact")
    public ResponseEntity<?> getContact();

    @PutMapping("/admin/contact")
    public ResponseEntity<?> resendClientContact(@Valid @RequestBody IdDTO[] entity);
}
