package com.company.UserDetailsTest;

import com.company.exceptions.BadRequestException;
import com.company.exceptions.ResourceNotFoundException;
import com.company.model.dto.SaveUserDetailsDto;
import com.company.model.entity.UserDetails;
import com.company.service.UserDetailsService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class UserDetailsServiceTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Order(1)
    void save() throws ResourceNotFoundException, BadRequestException {
        SaveUserDetailsDto userDetailsDto = new SaveUserDetailsDto("userId1", "1234567890", 1);
        UserDetails savedUserDetails = userDetailsService.save(userDetailsDto);
        assertTrue(savedUserDetails.getId()>0);
        assertNotEquals(0, savedUserDetails.getId());

        // Delete the user
        userDetailsService.deleteById((long) savedUserDetails.getId());
    }

    @Test
    @Order(2)
    void findAll() {
        List<UserDetails> userDetailsList = userDetailsService.findAll();
        assertFalse(userDetailsList.isEmpty());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Order(3)
    void findById() throws ResourceNotFoundException, BadRequestException {
        SaveUserDetailsDto userDetailsDto = new SaveUserDetailsDto("userId2", "9876543210", 2);
        UserDetails savedUser = userDetailsService.save(userDetailsDto);
        UserDetails foundUserDetails = userDetailsService.findById((long) savedUser.getId());
        assertEquals("userId2", foundUserDetails.getUserId());
        assertEquals("9876543210", foundUserDetails.getCellphone());
        assertEquals(2, foundUserDetails.getLocation().getId());

        // Delete the user
        userDetailsService.deleteById((long) savedUser.getId());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Order(4)
    void findByUserId() throws ResourceNotFoundException, BadRequestException {
        SaveUserDetailsDto userDetailsDto = new SaveUserDetailsDto("idToFind", "9876543210", 2);
        userDetailsService.save(userDetailsDto);

        UserDetails foundUserDetails = userDetailsService.findByUserId("idToFind");

        assertEquals(userDetailsDto.getLocationId(), foundUserDetails.getLocation().getId());
        assertEquals(userDetailsDto.getCellphone(), foundUserDetails.getCellphone());

        // Delete the user
        userDetailsService.deleteById((long) foundUserDetails.getId());
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Order(5)
    void update() throws ResourceNotFoundException, BadRequestException {
        SaveUserDetailsDto userDetailsDto = new SaveUserDetailsDto("userToUpdate", "9876543210", 2);
        UserDetails savedUser = userDetailsService.save(userDetailsDto);

        UserDetails userDetailsToUpdate = userDetailsService.findById((long) savedUser.getId());

        SaveUserDetailsDto newUser = new SaveUserDetailsDto("UpdatedUser", "1234567890", 9);
        userDetailsService.update((long) savedUser.getId(), newUser);

        UserDetails foundedUser = userDetailsService.findByUserId(newUser.getUserId());

        assertEquals(newUser.getLocationId(), foundedUser.getLocation().getId());
        assertEquals(newUser.getCellphone(), foundedUser.getCellphone());
        assertEquals(newUser.getUserId(), foundedUser.getUserId());

        // Delete the user
        userDetailsService.deleteById((long) savedUser.getId());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Order(6)
    void deleteById() throws ResourceNotFoundException, BadRequestException {
        SaveUserDetailsDto userDetailsDto = new SaveUserDetailsDto("userToDelete", "1234567890", 1);
        UserDetails savedUser = userDetailsService.save(userDetailsDto);

        userDetailsService.deleteById((long) savedUser.getId());
        Throwable exception1 = assertThrows(ResourceNotFoundException.class, () -> userDetailsService.findById((long) savedUser.getId()));

        assertEquals("Usuario con ID " + savedUser.getId() + " no existe.", exception1.getMessage());
    }
}
