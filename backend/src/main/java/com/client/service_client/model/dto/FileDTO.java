package com.client.service_client.model.dto;

import jakarta.validation.constraints.NotNull;

public class FileDTO extends IdDTO {
    @NotNull(message = "Main cannot be null")
    boolean main;

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }
}
