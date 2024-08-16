package com.client.service_client.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
    private String location;

    public StorageProperties(String location) {
        this.location = "upload/" + location;
    }

    public String getLocation () {
        return location;
    }
}
