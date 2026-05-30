package com.bibliotheque.entity;

import com.bibliotheque.enums.StatutEmprunt;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class EmpruntTest {

    @Test
    void calculerRetard_Aucun() {
        Emprunt emprunt = new Emprunt();
        emprunt.setDateRetourPrevue(LocalDate.now().plusDays(5));
        emprunt.setDateRetourEffective(LocalDate.now());

        assertEquals(0, emprunt.calculerRetard());
    }

    @Test
    void calculerRetard_5Jours() {
        Emprunt emprunt = new Emprunt();
        emprunt.setDateRetourPrevue(LocalDate.of(2026, 5, 10));
        emprunt.setDateRetourEffective(LocalDate.of(2026, 5, 15));

        assertEquals(5, emprunt.calculerRetard());
    }

    @Test
    void calculerRetard_DateEffectiveNull() {
        Emprunt emprunt = new Emprunt();
        emprunt.setDateRetourPrevue(LocalDate.now());

        assertEquals(0, emprunt.calculerRetard());
    }

    @Test
    void peutEtreProlonge_EnCoursSansRetard() {
        Emprunt emprunt = new Emprunt();
        emprunt.setStatut(StatutEmprunt.EN_COURS);
        emprunt.setDateRetourPrevue(LocalDate.now().plusDays(10));
        emprunt.setDateRetourEffective(LocalDate.now());

        assertTrue(emprunt.peutEtreProlonge());
    }

    @Test
    void peutEtreProlonge_EnRetard() {
        Emprunt emprunt = new Emprunt();
        emprunt.setStatut(StatutEmprunt.EN_COURS);
        emprunt.setDateRetourPrevue(LocalDate.now().minusDays(5));
        emprunt.setDateRetourEffective(LocalDate.now());

        assertFalse(emprunt.peutEtreProlonge());
    }

    @Test
    void peutEtreProlonge_DejaRetourne() {
        Emprunt emprunt = new Emprunt();
        emprunt.setStatut(StatutEmprunt.RETOURNE);

        assertFalse(emprunt.peutEtreProlonge());
    }
}