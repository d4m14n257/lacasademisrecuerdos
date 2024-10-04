package com.client.service_client.model.record;

import java.time.LocalDateTime;
import java.util.List;

import com.client.service_client.model.enums.RoomStatus;

public record RoomWithFilesAdmin(
    String id,
    String name,
    String description,
    String summary,
    String additional,
    Double single_price,
    Double double_price,
    LocalDateTime created_at,
    RoomStatus status, 
    List<FileAdminBytes> files
) { }
