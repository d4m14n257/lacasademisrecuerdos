package com.client.service_client.model.dto;

import jakarta.validation.constraints.NotBlank;

public class RoomUpdateDTO extends RoomDTO{
    @NotBlank
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
