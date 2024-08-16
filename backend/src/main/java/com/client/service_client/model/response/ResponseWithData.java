package com.client.service_client.model.response;

public record ResponseWithData<T>(String message, T data) {}
