package com.client.service_client.model.record;

public record UserResponse(
    String id,
    String username,
    String email,
    String first_name,
    String last_name
) { }
