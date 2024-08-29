package com.client.service_client.model.record;

public record HotelWithFile(
    String id,
    String hotel_name,
    String street_name,
    String neighborhood,
    String street_number,
    String postal_code,
    String phone_number,
    String email,
    Double latitude,
    Double longitude,
    String url,
    FilesBytes filesBytes
) { }
