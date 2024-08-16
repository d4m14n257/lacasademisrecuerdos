package com.client.service_client.model.record;

import org.springframework.http.HttpMethod;

public record Endpoints(HttpMethod method, String url) { }
