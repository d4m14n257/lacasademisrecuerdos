package com.client.service_client.model.record;

public record JwtResponse(
    UserResponse user,
    String token,
    String tokenType
) { }
