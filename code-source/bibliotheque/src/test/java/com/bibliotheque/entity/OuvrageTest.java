package com.bibliotheque.entity;

import com.bibliotheque.enums.StatutExemplaire;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OuvrageTest {

    @Test
    void estDisponible_AvecExemplairesDisponibles() {
        Ouvrage ouvrage = new Ouvrage();
        List<Exemplaire> exemplaires = new ArrayList<>();
        
        Exemplaire ex1 = new Exemplaire();
        ex1.setStatut(StatutExemplaire.DISPONIBLE);
        exemplaires.add(ex1);
        
        Exemplaire ex2 = new Exemplaire();
        ex2.setStatut(StatutExemplaire.EMPRUNTE);
        exemplaires.add(ex2);
        
        ouvrage.setExemplaires(exemplaires);

        assertTrue(ouvrage.estDisponible());
        assertEquals(1, ouvrage.getNbExemplairesDisponibles());
    }

    @Test
    void estDisponible_TousEmpruntes() {
        Ouvrage ouvrage = new Ouvrage();
        List<Exemplaire> exemplaires = new ArrayList<>();
        
        Exemplaire ex1 = new Exemplaire();
        ex1.setStatut(StatutExemplaire.EMPRUNTE);
        exemplaires.add(ex1);
        
        Exemplaire ex2 = new Exemplaire();
        ex2.setStatut(StatutExemplaire.RESERVE);
        exemplaires.add(ex2);
        
        ouvrage.setExemplaires(exemplaires);

        assertFalse(ouvrage.estDisponible());
        assertEquals(0, ouvrage.getNbExemplairesDisponibles());
    }

    @Test
    void estDisponible_AucunExemplaire() {
        Ouvrage ouvrage = new Ouvrage();
        ouvrage.setExemplaires(new ArrayList<>());

        assertFalse(ouvrage.estDisponible());
        assertEquals(0, ouvrage.getNbExemplairesDisponibles());
    }
}