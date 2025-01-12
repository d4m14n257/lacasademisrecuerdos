package com.client.service_client.model.record;

public record FileAdminBytes(String id, String name, String source, String mime, Boolean main, byte[] file) { }
