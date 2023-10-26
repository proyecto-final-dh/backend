package com.company.controller;

import com.company.model.dto.SaveLocationDTO;
import com.company.model.entity.Location;
import com.company.service.ILocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Location> findById(@PathVariable Long id) {
        try {
            Location location = locationService.findById(id);
            return ResponseEntity.ok(location);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> update(@PathVariable Long id, @RequestBody Location location) {
        try {
            Location existLocation = locationService.findById(id);
            if (id != null && existLocation != null) {
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
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try{
            locationService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Location deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Location not found");
        }
    }
}

