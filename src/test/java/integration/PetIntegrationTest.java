package integration;

import com.company.ProyectoFinalApplication;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ProyectoFinalApplication.class)
public class PetIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;



    @Test
    public void getPetsRecommendation() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/pets/recommendation/1?limit=3"))

                





    @Test
    public void getPetsByOwner() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/pets/owner/1"))

                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);


        assertTrue(jsonNode.isArray() && jsonNode.size() > 0);
    }


    @Test
    public void getPetsByLocation() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/pets/locations/1"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);


        assertTrue(jsonNode.isArray() && jsonNode.size() > 0);
    }



}
