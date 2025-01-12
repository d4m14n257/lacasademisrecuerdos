package com.client.service_client.model.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RoomDTO {
    
    @NotBlank(message = "Name is required")
    @Size(max = 32, message = "The name cannot be longer than 32 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 2048, message = "The description cannot be longer than 2048 characters")
    private String description;

    @NotBlank(message = "Summary is required")
    @Size(max = 512, message = "The summary cannot be longer than 512 characters")
    private String summary;

    @NotBlank(message = "Additional is required")
    @Size(max = 128, message = "The additional cannot be longer than 128 characters")
    private String additional;

    @NotNull(message = "Single price is required")
    @Digits(integer = 10, fraction = 2)
    private Double single_price;

    @Digits(integer = 10, fraction = 2)
    private Double double_price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public Double getSingle_price() {
        return single_price;
    }

    public void setSingle_price(Double single_price) {
        this.single_price = single_price;
    }

    public Double getDouble_price() {
        return double_price;
    }

    public void setDouble_price(Double double_price) {
        this.double_price = double_price;
    }
}
