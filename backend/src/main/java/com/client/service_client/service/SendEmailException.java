package com.client.service_client.service;

import java.util.concurrent.ExecutionException;

public class SendEmailException extends ExecutionException {
    public SendEmailException(String message, Throwable cause) {
        super(message, cause);
    } 
}
