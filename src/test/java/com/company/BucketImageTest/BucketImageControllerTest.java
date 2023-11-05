package com.company.BucketImageTest;

import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.company.controller.BucketImagesController;
import com.company.service.BucketImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)

public class BucketImageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BucketImageService amazonClient;

    @BeforeEach
    public void setup() {
        BucketImagesController controller = new BucketImagesController(amazonClient);
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    public void whenDeleteImage_thenThrowException() throws Exception {
        // Simular que el servicio lanza una excepción cuando se intenta eliminar una imagen
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting image from S3"))
                .when(amazonClient).deleteImage(anyString());

        // Realizar la petición de eliminación y verificar que se captura la excepción
        MockHttpServletResponse response = mockMvc.perform(delete("/storage/delete/{fileName}", "testImage.jpg"))
                .andReturn().getResponse();

        // Verificar que la respuesta es un estado HTTP 500
        assert response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
