package com.client.service_client.model.dto;

import com.client.service_client.model.enums.TourStatus;
import com.client.service_client.model.validation.interfaces.ValidTourStatus;

import jakarta.validation.constraints.NotBlank;

public class TourUpdateDTO extends TourDTO {
    @NotBlank(message = "Id is require")
    private String id;

    @ValidTourStatus
    private TourStatus status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TourStatus getStatus() {
        return status;
    }

    public void setStatus(TourStatus status) {
        this.status = status;
    }
}
