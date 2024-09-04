package com.client.service_client.service;

import java.util.concurrent.ExecutionException;

public class SendHtmlEmailException extends ExecutionException{
    public SendHtmlEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
