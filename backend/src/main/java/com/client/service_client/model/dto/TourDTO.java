package com.client.service_client.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TourDTO {

    @NotBlank(message = "Name is require")
    @Size(max = 128, message = "The name cannot be longer than 128 characters")
    private String name;

    @NotBlank(message = "Description is require")
    @Size(max = 2048, message = "The name cannot be longer than 2048 characters")
    private String description;

    @NotBlank(message = "Summary is require")
    @Size(max = 512, message = "The name cannot be longer than 512 characters")
    private String summary;

    @NotBlank(message = "Url is require")
    @Size(max = 128, message = "The name cannot be longer than 128 characters")
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
