package com.company.repository;

import com.company.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Location findByCountryAndCityAndState(String country, String city, String state);


}
