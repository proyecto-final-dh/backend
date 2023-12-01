package integration;

import com.company.ProyectoFinalApplication;
import com.company.model.dto.CreatePetDto;
import com.company.model.dto.PetWithImagesDto;
import com.company.model.entity.Pets;
import com.company.repository.IPetsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.company.utils.Mapper.mapCreatePetDtoToPet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ProyectoFinalApplication.class)
public class PetIntegrationTest {

    @Autowired
    IPetsRepository petsRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {

    }

    @Test
    public void createOwnPetWithImage() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        ObjectMapper objectMapper = new ObjectMapper();

        CreatePetDto pet = createCreatePetDto();
        pet.setGender(null);
        pet.setSize(null);
        pet.setDescription(null);
        pet.setGender(null);

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new FileInputStream("src/test/resources/images/image.png").readAllBytes()
        );

        String petJson = objectMapper.writeValueAsString(pet);

        MockPart postPart = new MockPart("post", petJson.getBytes());
        postPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/pets/own-with-images")
                        .file(file)
                        .part(postPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode dataJson = getJsonNode("data", result);
        PetWithImagesDto responsePet = objectMapper.treeToValue(dataJson, PetWithImagesDto.class);

        assertEquals(pet.getName(), responsePet.getName());
        assertFalse(responsePet.getImages().isEmpty());
        assertTrue(responsePet.getId() > 0);

        // Eliminar la entidad después de la prueba
        petsRepository.deleteById(responsePet.getId());
    }

    @Test
    public void createAdoptivePetWithImage() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        ObjectMapper objectMapper = new ObjectMapper();

        CreatePetDto pet = createCreatePetDto();

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new FileInputStream("src/test/resources/images/image.png").readAllBytes()
        );

        String petJson = objectMapper.writeValueAsString(pet);

        MockPart postPart = new MockPart("post", petJson.getBytes());
        postPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/pets/adoptive-with-images")
                        .file(file)
                        .part(postPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode dataJson = getJsonNode("data", result);
        PetWithImagesDto responsePet = objectMapper.treeToValue(dataJson, PetWithImagesDto.class);

        assertEquals(pet.getName(), responsePet.getName());
        assertFalse(responsePet.getImages().isEmpty());
        assertTrue(responsePet.getId() > 0);

        // Eliminar la entidad después de la prueba
        petsRepository.deleteById(responsePet.getId());
    }

    @Test
    public void getPetsRecommendation() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        ObjectMapper objectMapper = new ObjectMapper();
        CreatePetDto pet = createCreatePetDto();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/pets/recommendation/1?limit=3"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        assertTrue(jsonNode.isArray() && jsonNode.size() > 0);
    }


    @Test
    public void getPetsByOwner() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        ObjectMapper objectMapper = new ObjectMapper();
        CreatePetDto pet = createCreatePetDto();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/pets/owner/1"))

                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        assertTrue(jsonNode.has("content") && jsonNode.get("content").isArray() && jsonNode.get("content").size() > 0);


    }

    @Test
    public void getPetsByLocation() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        ObjectMapper objectMapper = new ObjectMapper();
        CreatePetDto pet = createCreatePetDto();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/pets/locations/1?page=0&size=3"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        assertTrue(jsonNode.has("content") && jsonNode.get("content").isArray() && jsonNode.get("content").size() > 0);

    }

    @Test
    public void getPetsBySize() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        ObjectMapper objectMapper = new ObjectMapper();
        CreatePetDto pet = createCreatePetDto();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/pets/size?page=0&size=3&petSize=GRANDE"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        assertTrue(jsonNode.has("content") && jsonNode.get("content").isArray() && jsonNode.get("content").size() > 0);
    }

    @Test
    public void getPetsByGender() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        ObjectMapper objectMapper = new ObjectMapper();
        CreatePetDto pet = createCreatePetDto();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/pets/gender?page=0&size=3&gender=MACHO"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        assertTrue(jsonNode.has("content") && jsonNode.get("content").isArray() && jsonNode.get("content").size() > 0);
    }

    private CreatePetDto createCreatePetDto() {

        CreatePetDto pet = new CreatePetDto();
        pet.setName("Dog");
        pet.setGender("MACHO");
        pet.setSize("GRANDE");
        pet.setOwnerId(1);
        pet.setBreedId(1);
        pet.setDescription("Descripción");

        return pet;
    }

    private JsonNode getJsonNode(String node, MvcResult result) throws JsonProcessingException, UnsupportedEncodingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String responseString = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseString);
        return jsonNode.get(node);
    }

}

