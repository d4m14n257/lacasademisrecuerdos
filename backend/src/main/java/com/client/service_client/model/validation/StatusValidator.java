package com.client.service_client.model.validation;

import com.client.service_client.model.enums.RoomStatus;
import com.client.service_client.model.enums.TourStatus;
import com.client.service_client.model.enums.UserStatus;
import com.client.service_client.model.validation.interfaces.ValidStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusValidator implements ConstraintValidator<ValidStatus, Object>{
    @Override
    public boolean isValid(Object status, ConstraintValidatorContext context) {
        if (status instanceof RoomStatus) {
            return status == RoomStatus.active || status == RoomStatus.hidden;
        }
        else if(status instanceof TourStatus) {
            return status == TourStatus.used || status == TourStatus.inactive; 
        }
        else if(status instanceof UserStatus) {
            return status == UserStatus.active || status == UserStatus.blocked || status == UserStatus.inactive;
        }
        
        return false;
    }
}
