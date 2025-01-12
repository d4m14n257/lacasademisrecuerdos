package com.client.service_client.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SourceDTO extends IdDTO{
    @NotBlank(message = "Source is required")
    private String source;

    @NotNull(message = "Main is required")
    private Boolean main;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }
}
