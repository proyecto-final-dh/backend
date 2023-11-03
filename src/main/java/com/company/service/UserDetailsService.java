package com.company.service;

import com.company.exceptions.BadRequestException;
import com.company.exceptions.ResourceNotFoundException;
import com.company.model.dto.SaveUserDetailsDto;
import com.company.model.entity.Location;
import com.company.model.entity.UserDetails;
import com.company.repository.IUserDetailsRepository;

import com.company.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserDetailsService  {
    private final IUserDetailsRepository repository;
    private final LocationRepository locationRepository;

    public UserDetailsService(IUserDetailsRepository repository, LocationRepository locationRepository) {
        this.repository = repository;
        this.locationRepository = locationRepository;
    }

    public List<UserDetails> findAll(){
        return repository.findAll();
    }

    public UserDetails findById(Long id) throws ResourceNotFoundException {
        Optional<UserDetails> item = repository.findById(Math.toIntExact(id));
        if(item.isEmpty()){
            throw new ResourceNotFoundException("Usuario con ID " + id + " no existe.");
        }
        return item.get();
    }

    public UserDetails findByUserId(String id) throws ResourceNotFoundException {
        Optional<UserDetails> item = repository.findByUserId(id);
        if(item.isEmpty()){
            throw new ResourceNotFoundException("Usuario con ID " + id + " no existe.");
        }
        return item.get();
    }

    public UserDetails save(SaveUserDetailsDto item) throws ResourceNotFoundException, BadRequestException {
        boolean exists = repository.existsByUserId(item.getUserId());
        if(exists){
            throw new BadRequestException("El usuario con el ID " + item.getUserId() + " ya existe.");
        }
        Optional<Location> location = locationRepository.findById(item.getLocationId());
        if(location.isEmpty()){
            throw new ResourceNotFoundException("Ubicación con el ID " + item.getLocationId() + " no existe.");
        }
        UserDetails newItem = new UserDetails(item.getUserId(), item.getCellphone(), location.get());
        return repository.save(newItem);
    }

    public UserDetails update(Long id, SaveUserDetailsDto item) throws ResourceNotFoundException, BadRequestException {
        if(id == null) throw new BadRequestException("Hace falta el ID del usuario.");

        boolean exists = repository.existsById(Math.toIntExact(id));
        if(!exists){
            throw new ResourceNotFoundException("Usuario con ID " + id + " no existe.");
        }

        Optional<Location> location = locationRepository.findById(item.getLocationId());
        if(location.isEmpty()){
            throw new ResourceNotFoundException("Ubicación con el ID " + item.getLocationId() + " no existe.");
        }
        UserDetails newItem = new UserDetails(Math.toIntExact(id), item.getUserId(), item.getCellphone(), location.get());

        return repository.save(newItem);
    }

    public void deleteById(Long id) throws ResourceNotFoundException, BadRequestException {
        if(id == null) throw new BadRequestException("Hace falta el ID del usuario.");
        boolean exists = repository.existsById(Math.toIntExact(id));
        if(!exists){
            throw new ResourceNotFoundException("Usuario con ID " + id + " no existe.");
        }
        repository.deleteById(Math.toIntExact(id));
    }

}