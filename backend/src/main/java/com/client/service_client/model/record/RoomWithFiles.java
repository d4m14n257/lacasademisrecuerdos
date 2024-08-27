package com.client.service_client.model.record;

import java.util.List;

public record RoomWithFiles(
    String id, 
    String name, 
    String description, 
    String summary, 
    String additional, 
    Double single_price, 
    Double double_price,
    List<FilesInfo> files) { }
