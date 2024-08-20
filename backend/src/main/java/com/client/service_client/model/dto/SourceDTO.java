package com.client.service_client.model.dto;

import jakarta.validation.constraints.NotBlank;

public class SourceDTO extends IdDTO{
    @NotBlank(message = "Source is required")
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
