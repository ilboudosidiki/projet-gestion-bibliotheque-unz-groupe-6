package com.bibliotheque.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ReservationControllerIntegrationTest {

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
    void reserver_OuvrageInexistant() throws Exception {
        Map<String, Long> request = new HashMap<>();
        request.put("etudiantId", 1L);
        request.put("ouvrageId", 99999L);

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getFileAttente_OuvrageInexistant() throws Exception {
        mockMvc.perform(get("/api/reservations/ouvrage/99999/file-attente"))
                .andExpect(status().isNotFound());
    }

    @Test
    void annulerReservation_Inexistante() throws Exception {
        mockMvc.perform(delete("/api/reservations/99999"))
                .andExpect(status().isNotFound());
    }
}