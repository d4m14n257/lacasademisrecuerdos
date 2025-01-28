package com.client.service_client.util;

import java.util.Locale;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class Language {
    private final LocaleResolver localeResolver;

    public Language (LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    public String requestLanguage (HttpServletRequest request) {
        Locale clientLocale = localeResolver.resolveLocale(request);
        return clientLocale.getLanguage();
    }
}
