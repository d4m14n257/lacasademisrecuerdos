package com.client.service_client.controller;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.client.service_client.controller.interfaces.IContactController;
import com.client.service_client.model.Contact;
import com.client.service_client.model.Room;
import com.client.service_client.model.dto.ContactDTO;
import com.client.service_client.model.dto.IdDTO;
import com.client.service_client.model.enums.ContactStatus;
import com.client.service_client.model.record.ContactList;
import com.client.service_client.model.response.ResponseOnlyMessage;
import com.client.service_client.model.response.ResponseWithData;
import com.client.service_client.model.response.ResponseWithInfo;
import com.client.service_client.service.ContactService;
import com.client.service_client.service.EmailService;
import com.client.service_client.service.RoomService;
import com.client.service_client.service.SendEmailException;
import com.client.service_client.service.SendHtmlEmailException;

@RestController
public class ContactController implements IContactController {
    
    private ContactService contactService;
    private EmailService emailService;
    private RoomService roomService;

    public ContactController (ContactService contactService, EmailService emailService, RoomService roomService) {
        this.contactService = contactService;
        this.emailService = emailService;
        this.roomService = roomService;
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
    public ResponseEntity<?> resendClientContact(@RequestBody IdDTO[] entity) {
        try {
            
        }
        catch () {

        }
    }

    public ResponseEntity<?> sendClientContact(ContactDTO entity) {
        try {            
            Optional<Room> room = roomService.room(entity.getId());
    
            if (!room.isPresent()) {
                throw new IllegalArgumentException("Room not exist");
            }
    
            CompletableFuture<Void> client = emailService.sendEmail(entity.getEmail(), 
                "Thank you for your message", 
                "Thank you for contacting us, we will get in touch soon.");
            client.get();
    
            saveContact(entity, room.get(), ContactStatus.send);
    
            CompletableFuture<Void> personal = emailService.sendHtmlEmail("You have a new message from a client", 
                entity.getFull_name(), 
                entity.getEmail(), 
                entity.getPhone_number(), 
                entity.getComment());
            personal.get();
    
            return ResponseEntity.ok().body(new ResponseOnlyMessage("Email sent successful"));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseWithInfo("Invalid request", e.getMessage()));
        }
        catch (SendEmailException e) {
            handleEmailError(entity, e, "Error sending email");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Error sending email", e.getMessage()));
        }
        catch (SendHtmlEmailException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseWithInfo("Email sent halfway", e.getMessage()));
        }
        catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Error sending email", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWithInfo("Internal server error", e.getMessage()));
        }
    }

    private void saveContact(ContactDTO entity, Room room, ContactStatus status) {
        Contact contact = new Contact();
        contact.setFull_name(entity.getFull_name());
        contact.setEmail(entity.getEmail());
        contact.setPhone_number(entity.getPhone_number());
        contact.setComment(entity.getComment());
        contact.setStatus(status);
        contact.setRoom(room);
        contactService.save(contact);
    }

    private void handleEmailError(ContactDTO entity, Exception e, String errorMessage) {
        Optional<Room> room = roomService.room(entity.getId());

        if (room.isPresent()) {
            saveContact(entity, room.get(), ContactStatus.error);
        }
        if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
    }
    
}
