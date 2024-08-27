package com.client.service_client.model.record;

public record RoomResponse(String id, String name, String summary, String additional, String file_name, byte[] file) { }
