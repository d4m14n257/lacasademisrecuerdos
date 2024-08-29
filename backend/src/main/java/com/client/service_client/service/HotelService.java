package com.client.service_client.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.client.service_client.model.Hotel;
import com.client.service_client.repository.HotelRepository;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService (HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> getAllHoteles () {
        return hotelRepository.findAll();
    }

    public void save (Hotel hotel) {
        hotelRepository.save(hotel);
    }

    public void deleteById (String id) {
        if(hotelRepository.existsHotel(id)) {
            hotelRepository.deleteById(id);
        }
        else {
            throw new RuntimeException("Hotel not found");
        }
    }
}
