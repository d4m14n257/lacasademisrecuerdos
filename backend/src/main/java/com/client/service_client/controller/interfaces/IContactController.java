package com.client.service_client.controller.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.client.service_client.model.Contact;

@RequestMapping("/api/contact")
public interface IContactController {

    @PostMapping
    public ResponseEntity<?> sendClientContact(@RequestBody Contact entity);

    @GetMapping("/admin")
    public ResponseEntity<?> getContact();

    @PutMapping("/admin")
    public ResponseEntity<?> resendClientContact(@RequestBody List<Contact> entity);

    @DeleteMapping("/admin")
    public ResponseEntity<?> deleteClientContact(@RequestBody String[] ids);

}
