package com.client.service_client.model.record;

import java.util.List;

public record TourWithFile(
    String id,
    String name,
    String description,
    String summary,
    String url,
    List<FilesBytes> filesBytes
) { }
