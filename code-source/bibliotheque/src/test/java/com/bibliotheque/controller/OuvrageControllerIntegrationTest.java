package com.bibliotheque.controller;

import com.bibliotheque.entity.Ouvrage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class OuvrageControllerIntegrationTest {

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
    void recherche_SansAuthentification() throws Exception {
        mockMvc.perform(get("/api/ouvrages/recherche")
                .param("motCle", "Java"))
                .andExpect(status().isOk());
    }

    @Test
    void recherche_AvecResultats() throws Exception {
        mockMvc.perform(get("/api/ouvrages/recherche")
                .param("motCle", "Spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void recherche_SansResultats() throws Exception {
        mockMvc.perform(get("/api/ouvrages/recherche")
                .param("motCle", "xyznonexistent123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void tousLesOuvrages() throws Exception {
        mockMvc.perform(get("/api/ouvrages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getOuvrage_Inexistant() throws Exception {
        mockMvc.perform(get("/api/ouvrages/99999"))
                .andExpect(status().isNotFound());
    }
}