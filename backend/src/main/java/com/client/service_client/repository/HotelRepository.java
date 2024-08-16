package com.client.service_client.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.client.service_client.model.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, String>{
    
}
