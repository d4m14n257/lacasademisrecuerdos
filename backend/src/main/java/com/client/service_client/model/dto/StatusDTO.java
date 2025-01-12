package com.client.service_client.model.dto;

import com.client.service_client.model.validation.interfaces.ValidStatus;

public class StatusDTO<T> extends IdDTO {
    @ValidStatus
    private T status;

    public T getStatus() {
        return status;
    }

    public void setStatus(T status) {
        this.status = status;
    }
}
