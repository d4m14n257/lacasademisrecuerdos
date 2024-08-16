package com.client.service_client.util;

import java.security.SecureRandom;
import java.util.Base64;

public abstract class CustomIdGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static String generate (int length) {
        int bytesLength = (int) Math.ceil(length * 3.0 / 4.0);

        byte[] randomBytes = new byte[bytesLength];
        random.nextBytes(randomBytes);

        String base64Str = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        return base64Str.substring(0, length);
    } 
}
