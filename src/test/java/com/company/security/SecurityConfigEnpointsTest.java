package com.company.security;


import com.company.model.entity.Location;
import com.company.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigEnpointsTest {

    @Autowired
    private MockMvc mockMvc;



    @Test
    public void givenPublicEndpoint_whenAccessWithoutAuth_thenSucceed() throws Exception {
        mockMvc.perform(get("/locations"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/species/"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/breeds/"))
                .andExpect(status().isOk());


    }

    @Test
    public void givenPrivateEndpoint_whenAccessWithoutAuth_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/user-details"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenProtectedEndpoint_whenPostRequestWithoutCredentials_thenForbidden() throws Exception{
        String jsonContent = "{\"name\": \"pulpo\"}";
        mockMvc.perform(post("/species/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isForbidden());

    }

    @Test
    public void givenProtectedEndpoint_whenPutRequestWithoutCredentials_thenForbidden() throws Exception {
        String jsonContent = "{\"name\": \"pajaross\"}";

        mockMvc.perform(put("/species1/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isForbidden());
    }
}