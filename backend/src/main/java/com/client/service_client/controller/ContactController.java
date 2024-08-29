package com.client.service_client.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.client.service_client.controller.interfaces.IContactController;
import com.client.service_client.model.Contact;
import com.client.service_client.model.enums.ContactStatus;
import com.client.service_client.model.record.ContactList;
import com.client.service_client.model.response.ResponseWithData;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.ContactService;
import com.client.service_client.service.EmailService;
import com.client.service_client.util.CustomIdGenerator;

public class ContactController implements IContactController {
    
    private ContactService contactService;
    private EmailService emailService;

    public ContactController (ContactService contactService, EmailService emailService) {
        this.contactService = contactService;
        this.emailService = emailService;
    }

    @Override
    public ResponseEntity<?> getContact() {
        try {
            ContactList contactList = contactService.getContact();

            if(contactList.contactSend().isPresent() || contactList.contactError().isPresent()) {
                ResponseEntity.ok().body(new ResponseWithData<ContactList>("Request successful", contactList));
            }

            return ResponseEntity.noContent().build();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> resendClientContact(@RequestBody List<Contact> entity) {
        // try {
            
        // }
        // catch () {

        // }
        return null;
    }

    @Override
    public ResponseEntity<?> sendClientContact(Contact entity) {
        try {
            CompletableFuture<Void> future = emailService.sendEmail(entity.getEmail(), entity.getFull_name(), "Thank you for contacting us, we will get in touch soon.");
            
            future.get();

            entity.setStatus(ContactStatus.send);
            entity.setId(CustomIdGenerator.generate(12));
            contactService.save(entity);
            
            return ResponseEntity.ok().body(new ResponseWithData<>("Request successful", null));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        } 
        catch (ExecutionException e) {
            
            entity.setStatus(ContactStatus.error);
            entity.setId(CustomIdGenerator.generate(12));
            contactService.save(entity);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Error sending email, ", e.getMessage()));
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            entity.setStatus(ContactStatus.error);
            entity.setId(CustomIdGenerator.generate(12));
            contactService.save(entity);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Process interrupted", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
        
    }

    
}
