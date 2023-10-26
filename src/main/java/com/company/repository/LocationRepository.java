package com.company.repository;

import com.company.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<Location, Integer> {

    Location findByCountryAndCityAndState(String country, String city, String state);


}
