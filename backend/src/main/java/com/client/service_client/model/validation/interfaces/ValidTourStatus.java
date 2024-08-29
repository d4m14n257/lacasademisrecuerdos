package com.client.service_client.model.validation.interfaces;

import com.client.service_client.model.validation.TourStatusValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TourStatusValidator.class)

public @interface ValidTourStatus {
    String message() default "Invalid tour status";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
