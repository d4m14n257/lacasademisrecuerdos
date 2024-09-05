package com.client.service_client.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.client.service_client.model.Contact;
import com.client.service_client.model.record.ContactList;
import com.client.service_client.repository.ContactRepository;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService (ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void save (Contact contact) {
        contactRepository.save(contact);
    }

    public ContactList getContact() {
        return new ContactList(contactRepository.getAllContactSend(), contactRepository.getAllContactError());
    }

    public Optional<Contact> findById (String id) {
        return contactRepository.findById(id);
    }
}
