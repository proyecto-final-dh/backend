package com.company.UserDetailsTest;

import com.company.exceptions.BadRequestException;
import com.company.exceptions.ResourceNotFoundException;
import com.company.model.dto.SaveUserDetailsDto;
import com.company.model.entity.UserDetails;
import com.company.service.UserDetailsService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("dev")
public class UserDetailsServiceTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Order(1)
    void save() throws ResourceNotFoundException, BadRequestException {
        SaveUserDetailsDto userDetailsDto = new SaveUserDetailsDto("userId1", "1234567890", 1);
        UserDetails savedUserDetails = userDetailsService.save(userDetailsDto);
        assertTrue(savedUserDetails.getId() > 0);
        assertNotEquals(0, savedUserDetails.getId());
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
        userDetailsService.save(userDetailsDto);
        UserDetails foundUserDetails1 = userDetailsService.findById(1L);
        UserDetails foundUserDetails2 = userDetailsService.findById(2L);
        assertEquals("userId1", foundUserDetails1.getUserId());
        assertEquals("userId2", foundUserDetails2.getUserId());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Order(4)
    void findByUserId() throws ResourceNotFoundException, BadRequestException {
        UserDetails foundUserDetails1 = userDetailsService.findByUserId("userId1");
        UserDetails foundUserDetails2 = userDetailsService.findByUserId("userId2");
        assertEquals(1, foundUserDetails1.getLocation().getId());
        assertEquals(2, foundUserDetails2.getLocation().getId());
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Order(5)
    void update() throws ResourceNotFoundException, BadRequestException {
        UserDetails userDetailsToUpdate = userDetailsService.findById(1L);
        SaveUserDetailsDto newUser = new SaveUserDetailsDto(userDetailsToUpdate.getUserId(), userDetailsToUpdate.getCellphone(), userDetailsToUpdate.getLocation().getId());
        newUser.setLocationId(9);
        userDetailsService.update(1L, newUser);
        UserDetails foundedUser = userDetailsService.findByUserId("userId1");
        assertEquals(9, foundedUser.getLocation().getId());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Order(6)
    void deleteById() throws ResourceNotFoundException, BadRequestException {
        userDetailsService.deleteById(1L);
        Throwable exception1 = assertThrows(ResourceNotFoundException.class, () -> userDetailsService.findById(1L));
        userDetailsService.deleteById(2L);
        Throwable exception2 = assertThrows(ResourceNotFoundException.class, () -> userDetailsService.findById(2L));
        assertEquals("Usuario con ID " + 1 + " no existe.", exception1.getMessage());
        assertEquals("Usuario con ID " + 2 + " no existe.", exception2.getMessage());
    }
}
