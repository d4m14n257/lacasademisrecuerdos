package com.client.service_client.model.record;

import java.time.LocalDateTime;
import java.util.List;

import com.client.service_client.model.enums.RoomStatus;

public record RoomWithFilesAdmin(
    String id,
    String name,
    String description_es,
    String description_en,
    String summary_es,
    String summary_en,
    Double single_price,
    Double double_price,
    LocalDateTime created_at,
    RoomStatus status, 
    List<FileAdminBytes> files
) { }
