package com.company.UserPetInterestTest;

import com.company.enums.PetStatus;
import com.company.exceptions.ResourceNotFoundException;
import com.company.model.dto.PetInterestWithOwnerInformationDto;
import com.company.model.dto.UserInformationDTO;
import com.company.model.dto.UserPetInterestDto;
import com.company.model.entity.Location;
import com.company.model.entity.Pets;
import com.company.model.entity.UserDetails;
import com.company.model.entity.UserPetInterest;
import com.company.repository.IPetsRepository;
import com.company.repository.IUserDetailsRepository;
import com.company.repository.IUserPetInterestRepository;
import com.company.service.UserPetInterestService;
import com.company.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserPetInterestTest {

    @Mock
    private IUserPetInterestRepository userPetInterestRepository;
    @Mock
    private IUserDetailsRepository userDetailsRepository;
    @Mock
    private IPetsRepository petsRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private UserPetInterestService userPetInterestService;

    @Test
    public void testGetUserPetInterest_whenEverythingIsOk_thenReturnInterest() {
        // Given
        int petId = 1;
        UserDetails userDetails = new UserDetails();
        userDetails.setId(1);
        UserPetInterest userPetInterest = new UserPetInterest();
        userPetInterest.setUserId(1);
        userPetInterest.setPetId(1);

        // When
        when(userDetailsRepository.findByUserId(anyString())).thenReturn(Optional.of(userDetails));
        when(userPetInterestRepository.existsByUserIdAndPetId(1, 1)).thenReturn(true);

        // Then
        UserPetInterestDto result = userPetInterestService.getUserPetInterest(petId);

        assertNotNull(result);
        assertEquals(userPetInterest.getUserId(), result.getUserId());
        assertTrue(result.isInterested());
    }

    @Test
    public void testGetUserPetListInterests_whenEverythingIsOk_thenReturnList() throws ResourceNotFoundException {
        // Given
        int petId = 1;
        UserDetails userDetails = new UserDetails();
        userDetails.setId(1);

        Pets pet = new Pets();
        pet.setId(1);
        pet.setStatus(PetStatus.EN_ADOPCION);
        pet.setUserDetails(userDetails);

        Pets pet2 = new Pets();
        pet2.setId(1);
        pet2.setStatus(PetStatus.ADOPTADA);
        pet2.setUserDetails(userDetails);

        UserPetInterest userPetInterest = new UserPetInterest();
        userPetInterest.setUserId(1);
        userPetInterest.setPetId(2);
        userPetInterest.setPet(pet);

        UserPetInterest userPetInterest2 = new UserPetInterest();
        userPetInterest2.setUserId(1);
        userPetInterest2.setPetId(2);
        userPetInterest2.setPet(pet2);

        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setUserId("1");
        userInformationDTO.setUserDetailsId(1);
        userInformationDTO.setName("name");

        // When
        when(userDetailsRepository.findByUserId(anyString())).thenReturn(Optional.of(userDetails));
        when(userPetInterestRepository.findAllByUserId(anyInt())).thenReturn(List.of(userPetInterest, userPetInterest2));
        when(userService.findById(anyString())).thenReturn(userInformationDTO);

        // Then
        List<PetInterestWithOwnerInformationDto> result = userPetInterestService.getUserPetListInterests();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userPetInterest.getPetId(), result.get(0).getPetId());
        assertTrue(result.get(0).isInterested());
    }

    @Test
    public void testCreateUserPetInterest_whenPetHasAdoptedStatus_thenReturnException() {
        // Given
        int petId = 1;
        UserDetails userDetails = new UserDetails();
        userDetails.setId(1);

        Pets pet = new Pets();
        pet.setId(1);
        pet.setStatus(PetStatus.ADOPTADA);

        UserPetInterest userPetInterest = new UserPetInterest();
        userPetInterest.setUserId(1);
        userPetInterest.setPetId(1);
        userPetInterest.setPet(pet);

        // When
        when(userDetailsRepository.findByUserId(anyString())).thenReturn(Optional.of(userDetails));
        when(petsRepository.findById(anyInt())).thenReturn(Optional.of(pet));
        when(userPetInterestRepository.existsByUserIdAndPetId(1, 1)).thenReturn(false);

        // Then
        assertThrows(ResponseStatusException.class, () -> userPetInterestService.createUserPetInterest(petId));
    }

    @Test
    public void testCreateUserPetInterest_whenInterestIsAlreadyCreated_thenReturnException() {
        // Given
        int petId = 1;
        UserDetails userDetails = new UserDetails();
        userDetails.setId(1);

        Pets pet = new Pets();
        pet.setId(1);
        pet.setStatus(PetStatus.EN_ADOPCION);

        UserPetInterest userPetInterest = new UserPetInterest();
        userPetInterest.setUserId(1);
        userPetInterest.setPetId(1);
        userPetInterest.setPet(pet);

        // When
        when(userDetailsRepository.findByUserId(anyString())).thenReturn(Optional.of(userDetails));
        when(petsRepository.findById(anyInt())).thenReturn(Optional.of(pet));
        when(userPetInterestRepository.existsByUserIdAndPetId(1, 1)).thenReturn(true);

        // Then
        assertThrows(ResponseStatusException.class, () -> userPetInterestService.createUserPetInterest(petId));
    }
}