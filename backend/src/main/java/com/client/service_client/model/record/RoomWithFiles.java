package com.client.service_client.model.record;

import java.math.BigDecimal;
import java.util.List;

public record RoomWithFiles(
    String id, 
    String name, 
    String description, 
    String summary, 
    String additional, 
    BigDecimal single_price, 
    BigDecimal double_price,
    List<FilesInfo> files) { }
