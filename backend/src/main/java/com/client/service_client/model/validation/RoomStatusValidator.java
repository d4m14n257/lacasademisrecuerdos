package com.client.service_client.model.validation;

import com.client.service_client.model.enums.RoomStatus;
import com.client.service_client.model.validation.interfaces.ValidRoomStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoomStatusValidator implements ConstraintValidator<ValidRoomStatus, RoomStatus>{
    @Override
        public void initialize(ValidRoomStatus constraintAnnotation) { }

    @Override
    public boolean isValid(RoomStatus status, ConstraintValidatorContext context) {
        if(status == null) {
            return true;
        }

        return status == RoomStatus.active || status == RoomStatus.hidden;
    }
}
