package com.client.service_client.model.validation;

import com.client.service_client.model.enums.TourStatus;
import com.client.service_client.model.validation.interfaces.ValidTourStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TourStatusValidator implements ConstraintValidator<ValidTourStatus, TourStatus>{
    @Override
        public void initialize(ValidTourStatus constraintAnnotation) { }

    @Override
    public boolean isValid(TourStatus status, ConstraintValidatorContext context) {
        if (status == null) {
            return true;
        }

        return status == TourStatus.used || status == TourStatus.inactive; 
    }
}
