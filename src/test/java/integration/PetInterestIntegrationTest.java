package integration;

import com.company.ProyectoFinalApplication;
import com.company.model.dto.PetInterestWithOwnerInformationDto;
import com.company.repository.IUserPetInterestRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ProyectoFinalApplication.class)
public class PetInterestIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private IUserPetInterestRepository userPetInterestRepository;
    private MockMvc mockMvc;

    @BeforeAll
    public static void setupAll() {
        SecurityContextHolder.clearContext();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "b19dc58a-0836-425d-ae3e-814a816a523e")
                .build();

        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(jwtAuth);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterAll
    public static void teardownAll() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testCreatePetInterest() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        ObjectMapper objectMapper = new ObjectMapper();

        userPetInterestRepository.deleteByPetIdAndUserId(10, 5);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/interests/10"))
                .andExpect(status().isCreated())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(contentAsString);
        PetInterestWithOwnerInformationDto petInterestWithOwnerInformationDto = objectMapper.treeToValue(jsonNode.get("data"), PetInterestWithOwnerInformationDto.class);


        assertTrue(petInterestWithOwnerInformationDto.isInterested());
        assertEquals(10, petInterestWithOwnerInformationDto.getPetId());

        userPetInterestRepository.deleteByPetIdAndUserId(10, 5);
    }
}
