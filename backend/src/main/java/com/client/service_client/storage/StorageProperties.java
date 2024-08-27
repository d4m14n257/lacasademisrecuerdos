package com.client.service_client.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
    private final String location;

    public StorageProperties() {
        this.location = "../upload";
    }

    public String getLocation () {
        return location;
    }
}
