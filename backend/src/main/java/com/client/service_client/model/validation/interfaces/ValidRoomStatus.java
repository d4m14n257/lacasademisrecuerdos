package com.client.service_client.model.validation.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.client.service_client.model.validation.RoomStatusValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoomStatusValidator.class)

public @interface ValidRoomStatus {
    String message() default "Invalid room status";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
