package com.client.service_client.model.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RoomDTO {
    
    @NotBlank(message = "Name is required")
    @Size(max = 32, message = "The name cannot be longer than 32 characters")
    private String name;

    @NotBlank(message = "Description in Spanish is required")
    @Size(max = 2048, message = "The description cannot be longer than 2048 characters")
    private String description_es;

    @NotBlank(message = "Description in English is required")
    @Size(max = 2048, message = "The description cannot be longer than 2048 characters")
    private String description_en;

    @NotBlank(message = "Summary in Spanish is required")
    @Size(max = 512, message = "The summary cannot be longer than 512 characters")
    private String summary_es;

    @NotBlank(message = "Summary in English is required")
    @Size(max = 512, message = "The summary cannot be longer than 512 characters")
    private String summary_en;

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

    public String getDescription_es() {
        return description_es;
    }

    public void setDescription_es(String description_es) {
        this.description_es = description_es;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getSummary_es() {
        return summary_es;
    }

    public void setSummary_es(String summary_es) {
        this.summary_es = summary_es;
    }

    public String getSummary_en() {
        return summary_en;
    }

    public void setSummary_en(String summary_en) {
        this.summary_en = summary_en;
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
