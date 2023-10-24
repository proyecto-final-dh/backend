package com.company.service;

import com.company.model.entity.Pet;
import com.company.repository.PetRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PetService implements  IPetService{

    private PetRepository petRepository;

    /*
    public List<Pet> findAll(){
        List<Pet> pets = petRepository.findAll();
        return pets;
    }

     */

    public Page<Pet> findAll(Pageable pageable) throws Exception {
        try {
            return petRepository.findAll(pageable);
        } catch (Exception e) {
            throw new Exception("Error al recuperar las mascotas paginadas.");
        }
    }





    public Pet findById(Long id) throws Exception {
        try {
            Optional<Pet> pet = petRepository.findById(id);
            if (pet.isPresent()) {
                return pet.get();
            } else {
                throw new Exception("Pet with id " + id + " not found.");
            }
        } catch (Exception e) {
            throw new Exception("Pet with id " + id + " not found.");
        }
    }


    public Pet update(Long id, Pet pet) throws Exception{
        Boolean exists = petRepository.existsById(id);
        try {
            if(id != null && exists) {
                {
                    pet.setId(id);
                    return petRepository.save(pet);
                }
            } else {
                throw new Exception("ID missing");
            }
        } catch (Exception e) {
            throw new Exception("Pet with id " + id + " not found.");
        }
    }

    public Pet save(Pet pet) throws Exception {

        Pet ifExistsLocation = petRepository.findByNameAndStatusAndSizeAndGenderAndDescription(pet.getName(), pet.getStatus(), pet.getGender(), pet.getSize(), pet.getDescription() );
        if (ifExistsLocation == null) {
            return petRepository.save(pet);
        } else {
            throw new Exception("That pet already exist");
        }
    }

    public void deleteById(Long id) throws Exception {
        Boolean exists = petRepository.existsById(id);
        try {
            if(id != null && exists) {
                {
                    petRepository.deleteById(id);
                }
            } else {
                throw new Exception("ID missing");
            }
        } catch (Exception e) {
            throw new Exception("Pet with id " + id + " not found.");
        }
    }


}
