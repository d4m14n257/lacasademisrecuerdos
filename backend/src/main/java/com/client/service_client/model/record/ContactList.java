package com.client.service_client.model.record;

import java.util.Optional;

import com.client.service_client.model.Contact;

public record ContactList(Optional<Contact> contactSend, Optional<Contact> contactError) { }