package com.client.service_client.model.record;

import com.client.service_client.model.enums.UserStatus;

public record UserList(
    String id,
    String email,
    String username,
    UserStatus status,
    String first_name,
    String last_name
) { }
