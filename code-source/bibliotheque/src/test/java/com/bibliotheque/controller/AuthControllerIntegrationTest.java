package com.bibliotheque.controller;

import com.bibliotheque.dto.LoginRequest;
import com.bibliotheque.dto.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AuthControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void register_Succes() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setNom("Test");
        request.setPrenom("User");
        request.setEmail("test.user@email.com");
        request.setMotDePasse("password123");
        request.setMatricule("ETU-TEST-001");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Compte créé avec succès"));
    }

    @Test
    void register_EmailExistant() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setNom("Test");
        request.setPrenom("User");
        request.setEmail("test.user@email.com");
        request.setMotDePasse("password123");
        request.setMatricule("ETU-TEST-002");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Cet email est déjà utilisé"));
    }

    @Test
    void login_Succes() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test.user@email.com");
        request.setMotDePasse("password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value("test.user@email.com"))
                .andExpect(jsonPath("$.role").value("ETUDIANT"));
    }

    @Test
    void login_MotDePasseIncorrect() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test.user@email.com");
        request.setMotDePasse("mauvaisMotDePasse");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email ou mot de passe incorrect"));
    }

    @Test
    void login_EmailInexistant() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("inexistant@email.com");
        request.setMotDePasse("password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email ou mot de passe incorrect"));
    }

    @Test
    void register_ChampsInvalides() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setNom("");
        request.setPrenom("");
        request.setEmail("email-invalide");
        request.setMotDePasse("123");
        request.setMatricule("");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}