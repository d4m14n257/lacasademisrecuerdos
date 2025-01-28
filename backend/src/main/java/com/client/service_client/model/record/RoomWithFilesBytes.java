package com.client.service_client.model.record;

import java.math.BigDecimal;
import java.util.List;

public record RoomWithFilesBytes(
    String id, 
    String name, 
    String description, 
    String summary, 
    BigDecimal single_price, 
    BigDecimal double_price,
    List<FilesBytes> files
) { }
