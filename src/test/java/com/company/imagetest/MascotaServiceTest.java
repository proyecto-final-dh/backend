package com.company.imagetest;

import com.company.model.entity.Pets;
import com.company.model.entity.UserDetails;
import com.company.repository.IPetsRepository;
import com.company.repository.IUserDetailsRepository;
import com.company.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MascotaServiceTest {

    @MockBean
    private IPetsRepository petsRepository;

    @MockBean
    private IUserDetailsRepository userRepository;

    @Autowired
    private PetService petService;

    @Test
    public void testGuardarMascotaSinUserReal() {
        // Crear una instancia de Mascota
        Pets petTest = new Pets();
        petTest.setName1("Firulais");

        // Crear una instancia simulada de User
        UserDetails userMock = mock(UserDetails.class);
        when(userMock.getId()).thenReturn(1); // Suponiendo que el ID es suficiente para tu test

        // Asociar el mock de User con la Mascota
        petTest.setUserDetails(userMock);


        // Configurar el repositorio para simular el guardado y la recuperación
        when(petsRepository.save(any(Pets.class))).thenReturn(petTest);
        when(petsRepository.findById((int) anyLong())).thenReturn(Optional.of(petTest));

        // Llamar al método que deseas probar
        Pets petSaved = null;
        try {
            petSaved = petService.save(petTest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Verificaciones y aserciones...
        assertNotNull(petSaved);
        verify(petsRepository).save(any(Pets.class));
    }
}
