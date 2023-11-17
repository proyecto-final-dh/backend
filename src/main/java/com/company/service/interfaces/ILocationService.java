package com.company.service.interfaces;

import com.company.model.entity.Location;

import java.util.List;

public interface ILocationService {
    List<Location> findAll();
    Location findById(int id) throws Exception;
    Location findByCountryAndCityAndState(String country, String city, String state);
    Location update(int id, Location location) throws Exception;
    Location save(Location location) throws Exception;
    void deleteById(int id) throws Exception;
}
