package com.client.service_client.model.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class HotelDTO {
    
    @NotBlank(message = "Hotel name is required")
     @Size(max = 128, message = "The name cannot be longer than 128 characters")
    private String hotel_name;

    @NotBlank(message = "Street name is required")
    @Size(max = 128, message = "The street cannot be longer than 128 characters")
    private String street_name;

    @Size(max = 64, message = "The neighborhood cannot be longer than 64 characters")
    private String neighborhood;

    @Size(max = 10, message = "The street number cannot be longer than 10 characters")
    private String street_number;

    @Pattern(regexp = "^\\\\d{5}$",
            message = "The postal code is not valid.")
    @Size(max = 10, message = "The postal code cannot be longer than 10 characters")
    private String postal_code;

    @Pattern(
        regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$",
        message = "The phone number is not valid")
    @Size(max = 16, message = "The phone number cannot be longer than 16 characters")
    private String phone_number;

    @Email
    @Pattern(
        regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
        message = "The email entered is not valid")
    @Size(max = 64, message = "The email cannot be longer than 64 characters")
    private String email;

    @NotNull(message = "Latitude cannot be null")
    @Digits(integer = 3, fraction = 7)
    private Double latitude;

    @NotNull(message = "Longitude cannot be null")
    @Digits(integer = 3, fraction = 7)
    private Double longitude;

    @URL
    @Size(max = 128, message = "The url cannot be longer than 128 characters")
    private String url;

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
