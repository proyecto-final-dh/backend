package com.company.service;

import com.company.model.entity.Location;
import com.company.repository.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LocationService implements ILocationService{


    private final LocationRepository locationRepository;

    public List<Location> findAll(){
        List<Location> locations = locationRepository.findAll();
        return locations;
    }

    public Location findById(Long id) throws Exception {
        try {
            Optional<Location> location = locationRepository.findById(id);
            if (location.isPresent()) {
                return location.get();
            } else {
                throw new Exception("Location with id " + id + " not found.");
            }
        } catch (Exception e) {
            throw new Exception("Location with id " + id + " not found.");
        }
    }

    public Location findByCountryAndCityAndState(String country, String city, String state){
        return locationRepository.findByCountryAndCityAndState(country, city, state);
    }

       public Location update(Long id, Location location) throws Exception{
        Boolean exists = locationRepository.existsById(id);
        try {
            if(id != null && exists) {
                {
                    location.setId(id);
                    return locationRepository.save(location);
                }
            } else {
                throw new Exception("ID missing");
            }
        } catch (Exception e) {
            throw new Exception("Location with id " + id + " not found.");
        }
    }

    public Location save(Location location) throws Exception {

        Location ifExistsLocation = locationRepository.findByCountryAndCityAndState(location.getCountry(), location.getCity(), location.getState());
        if (ifExistsLocation == null) {
            return locationRepository.save(location);
        } else {
            throw new Exception("That location already exist");
        }
    }


    public void deleteById(Long id) throws Exception {
        Boolean exists = locationRepository.existsById(id);
        try {
            if(id != null && exists) {
                {
                    locationRepository.deleteById(id);
                }
            } else {
                throw new Exception("ID missing");
            }
        } catch (Exception e) {
            throw new Exception("Location with id " + id + " not found.");
        }
    }


}
