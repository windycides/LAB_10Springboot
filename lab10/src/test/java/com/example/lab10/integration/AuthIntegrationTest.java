package com.example.lab10.integration;

import com.example.lab10.model.User;
import com.example.lab10.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        // Clean the database before each test to avoid duplicates
        userRepository.deleteAll();
    }

    @Test
    public void testLoginFlowAndStrictLogout() throws Exception {
        // Creating a User with an ENCODED password
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("t@t.com");
        user.setPassword(passwordEncoder.encode("passwordsecure"));
        userRepository.save(user);

        //Prepare Login Data
        Map<String, String> loginData = Map.of(
                "username", "testuser",
                "password", "passwordsecure" // The raw password to check against the hash
        );

        //Perform Login and save the response
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                .andExpect(status().isOk()) // This should now be 200 OK
                .andReturn();

        // Extract the refreshToken from the response JSON
        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);

        // Check if token exists
        if (!jsonNode.has("refreshToken")) {
            throw new RuntimeException("Login response did not contain refreshToken: " + responseContent);
        }

        String realRefreshToken = jsonNode.get("refreshToken").asText();

        // Logout with token
        mockMvc.perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + realRefreshToken + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAccessDeniedWithoutToken() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCsrfDisabledForStatelessApi() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }
}