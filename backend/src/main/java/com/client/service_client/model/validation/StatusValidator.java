package com.client.service_client.model.validation;

import com.client.service_client.model.enums.RoomStatus;
import com.client.service_client.model.enums.TourStatus;
import com.client.service_client.model.enums.UserStatus;
import com.client.service_client.model.validation.interfaces.ValidStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusValidator implements ConstraintValidator<ValidStatus, Enum<?>>{
    @Override
    public boolean isValid(Enum<?> status, ConstraintValidatorContext context) {
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
