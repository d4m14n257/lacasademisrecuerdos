package com.client.service_client.model.response;

public record JwtResponse(
    String id,
    String username,
    String email,
    String first_name,
    String last_name,
    String token,
    String tokentype
) { }
