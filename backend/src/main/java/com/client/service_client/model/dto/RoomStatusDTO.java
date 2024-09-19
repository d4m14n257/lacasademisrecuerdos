package com.client.service_client.model.dto;

import com.client.service_client.model.enums.RoomStatus;
import com.client.service_client.model.validation.interfaces.ValidRoomStatus;

public class RoomStatusDTO extends IdDTO{
    
    @ValidRoomStatus
    private RoomStatus status;

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}
