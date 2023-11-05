package com.company.BucketImageTest;

import com.company.service.BucketImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

// ... otros imports ...

public class ImageServiceTest {

    @Mock
    private S3Client mockS3Client;

    @InjectMocks
    private BucketImageService imageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deleteImageThrowsExceptionWhenS3Fails() {
        // Simular que S3 lanza una excepción al intentar eliminar el objeto
        doThrow(SdkException.builder().message("S3 error").build()).when(mockS3Client).deleteObject(any(DeleteObjectRequest.class));

        // Asumir que la configuración del bucket y el nombre del archivo son correctos
        String fileName = "image.jpg";

        // Ejecutar el método deleteImage y verificar que se lanza la excepción esperada
        assertThrows(ResponseStatusException.class, () -> imageService.deleteImage(fileName));
    }
}

