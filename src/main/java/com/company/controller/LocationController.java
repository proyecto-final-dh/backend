package com.company.controller;

import com.company.model.dto.SaveLocationDTO;
import com.company.model.entity.Location;
import com.company.service.interfaces.ILocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private ILocationService locationService;

    @GetMapping
    public ResponseEntity<List<Location>> findAll() {
        List<Location> locations = locationService.findAll();
        return ResponseEntity.ok(locations);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> findById(@PathVariable int id) {
        try {
            Location location = locationService.findById(id);
            return ResponseEntity.ok(location);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> update(@PathVariable int id, @RequestBody Location location) {
        try {
            Location existLocation = locationService.findById(id);
            if (existLocation != null) {
                return ResponseEntity.ok(locationService.update(id, location));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Location> save(@RequestBody SaveLocationDTO location) {
        try {
            Location newLocation =  new Location(location.getCountry(), location.getCity(), location.getState());
            locationService.save(newLocation);
            return ResponseEntity.status(HttpStatus.CREATED.value()).body(newLocation);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id) {
        try{
            locationService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Location deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Location not found");
        }
    }
}
