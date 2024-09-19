package com.client.service_client.model.record;

import com.client.service_client.model.enums.RoomStatus;

public record RoomCards(String id, String name, String summary, String additional, RoomStatus status, String source, String file_name) {}
