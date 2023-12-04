package com.company.service;

import com.company.exceptions.ResourceNotFoundException;
import com.company.model.dto.UserInformationDTO;
import com.company.model.entity.UserDetails;
import com.company.model.entity.UserKeycloak;
import com.company.repository.UserKeycloakRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserDetailsService userDetailsService;
    private UserKeycloakRepository userKeycloakRepository;

    public UserService(UserDetailsService userDetailsService, UserKeycloakRepository userKeycloakRepository) {
        this.userDetailsService = userDetailsService;
        this.userKeycloakRepository = userKeycloakRepository;
    }

    public UserInformationDTO findById(String userId) throws ResourceNotFoundException {
        UserKeycloak userKeycloak= userKeycloakRepository.findUser(userId);
        UserDetails userDetails= userDetailsService.findByUserId(userId);

        UserInformationDTO userInformationDTO= new UserInformationDTO();
        userInformationDTO.setUserId(userId);
        userInformationDTO.setName(userKeycloak.getName());
        userInformationDTO.setLastname(userKeycloak.getLastname());
        userInformationDTO.setCellphone(userDetails.getCellphone());
        userInformationDTO.setLocation(userDetails.getLocation());
        userInformationDTO.setEmail(userKeycloak.getEmail());
        userInformationDTO.setUserDetailsId(userDetails.getId());

        return userInformationDTO;

    }
}
