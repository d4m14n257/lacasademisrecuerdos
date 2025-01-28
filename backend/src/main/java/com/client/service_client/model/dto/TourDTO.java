package com.client.service_client.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TourDTO {

    @NotBlank(message = "Name is require")
    @Size(max = 128, message = "The name cannot be longer than 128 characters")
    private String name;

    @NotBlank(message = "Description is require")
    @Size(max = 2048, message = "The name cannot be longer than 2048 characters")
    private String description_es;

    @NotBlank(message = "Summary is require")
    @Size(max = 512, message = "The name cannot be longer than 512 characters")
    private String summary_es;

    @NotBlank(message = "Description is require")
    @Size(max = 2048, message = "The name cannot be longer than 2048 characters")
    private String description_en;

    @NotBlank(message = "Summary is require")
    @Size(max = 512, message = "The name cannot be longer than 512 characters")
    private String summary_en;

    @NotBlank(message = "Url is require")
    @Size(max = 128, message = "The name cannot be longer than 128 characters")
    private String url;

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

    public String getSummary_es() {
        return summary_es;
    }

    public void setSummary_es(String summary_es) {
        this.summary_es = summary_es;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getSummary_en() {
        return summary_en;
    }

    public void setSummary_en(String summary_en) {
        this.summary_en = summary_en;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
