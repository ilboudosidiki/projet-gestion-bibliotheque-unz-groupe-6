package com.bibliotheque.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class EtudiantTest {

    @Test
    void estSuspendu_NonSuspendu() {
        Etudiant etudiant = new Etudiant();
        etudiant.setDateFinSuspension(null);

        assertFalse(etudiant.estSuspendu());
    }

    @Test
    void estSuspendu_DatePassee() {
        Etudiant etudiant = new Etudiant();
        etudiant.setDateFinSuspension(LocalDate.now().minusDays(5));

        assertFalse(etudiant.estSuspendu());
    }

    @Test
    void estSuspendu_Suspendu() {
        Etudiant etudiant = new Etudiant();
        etudiant.setDateFinSuspension(LocalDate.now().plusDays(5));

        assertTrue(etudiant.estSuspendu());
    }

    @Test
    void peutEmprunter_NonSuspendu() {
        Etudiant etudiant = new Etudiant();
        etudiant.setDateFinSuspension(null);

        assertTrue(etudiant.peutEmprunter());
    }

    @Test
    void peutEmprunter_Suspendu() {
        Etudiant etudiant = new Etudiant();
        etudiant.setDateFinSuspension(LocalDate.now().plusDays(5));

        assertFalse(etudiant.peutEmprunter());
    }
}