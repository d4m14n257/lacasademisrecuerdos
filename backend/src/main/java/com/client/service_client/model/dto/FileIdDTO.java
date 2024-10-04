package com.client.service_client.model.dto;

import jakarta.validation.constraints.NotBlank;

public class FileIdDTO extends IdDTO {
    @NotBlank(message = "File id is required") 
    private String file_id;

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }
}
