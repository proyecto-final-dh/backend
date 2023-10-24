package com.company.service;

import com.company.model.entity.Location;

import java.util.List;

public interface ILocationService {
    List<Location> findAll();
    Location findById(Long id) throws Exception;
    Location findByCountryAndCityAndState(String country, String city, String state);
    Location update(Long id, Location location) throws Exception;
    Location save(Location location) throws Exception;
    void deleteById(Long id) throws Exception;
}
