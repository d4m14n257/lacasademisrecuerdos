package com.client.service_client.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.client.service_client.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, String>{
    @Query(value = "SELECT * " + 
                   "FROM Contact c " +
                   "JOIN Room r ON r.id = c.room_id " +
                   "WHERE c.status = 'send'", nativeQuery = true)
    Optional<Contact> getAllContactSend();

    @Query(value = "SELECT * " + 
                   "FROM Contact c " +
                   "JOIN Room r ON r.id = c.room_id " +
                   "WHERE c.status = 'error'", nativeQuery = true)
    Optional<Contact> getAllContactError();
}
