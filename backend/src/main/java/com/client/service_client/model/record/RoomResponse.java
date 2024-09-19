package com.client.service_client.model.record;

import com.client.service_client.model.enums.RoomStatus;

public record RoomResponse(String id, String name, String summary, String additional, RoomStatus status, String file_name, byte[] file) { }
