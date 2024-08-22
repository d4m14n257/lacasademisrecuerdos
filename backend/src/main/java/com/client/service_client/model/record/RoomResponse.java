package com.client.service_client.model.record;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public record RoomResponse(String name, String summary, String additional, String file_name, ResponseEntity<Resource> file) { }
