package com.client.service_client.model.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public class HotelUpdateDTO extends HotelDTO{
    @NotBlank
    private String id;

    @NotBlank
    private LocalDate created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    } 
}
