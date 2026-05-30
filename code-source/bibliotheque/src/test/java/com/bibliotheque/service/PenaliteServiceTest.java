package com.bibliotheque.service;

import com.bibliotheque.entity.Emprunt;
import com.bibliotheque.entity.Etudiant;
import com.bibliotheque.entity.Exemplaire;
import com.bibliotheque.entity.Ouvrage;
import com.bibliotheque.entity.Penalite;
import com.bibliotheque.repository.EtudiantRepository;
import com.bibliotheque.repository.PenaliteRepository;
import com.bibliotheque.service.impl.PenaliteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PenaliteServiceTest {

    @Mock
    private PenaliteRepository penaliteRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private PenaliteServiceImpl penaliteService;

    private Emprunt emprunt;
    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setId(1L);
        etudiant.setNom("Traore");
        etudiant.setPrenom("Fatou");
        etudiant.setEmail("fatou@email.com");
        etudiant.setMatricule("ETU003");

        Ouvrage ouvrage = new Ouvrage();
        ouvrage.setId(1L);
        ouvrage.setTitre("Réseaux et Télécoms");

        Exemplaire exemplaire = new Exemplaire();
        exemplaire.setId(1L);
        exemplaire.setCodeBarres("EX003");
        exemplaire.setOuvrage(ouvrage);

        emprunt = new Emprunt();
        emprunt.setId(1L);
        emprunt.setEtudiant(etudiant);
        emprunt.setExemplaire(exemplaire);
        emprunt.setDateRetourPrevue(LocalDate.now().minusDays(5));
        emprunt.setDateRetourEffective(LocalDate.now());
    }

    @Test
    void creerPenalite_Succes() {
        Penalite penalite = penaliteService.creerPenalite(emprunt, 5);

        assertNotNull(penalite);
        assertEquals(5, penalite.getDureeJours());
        assertEquals(etudiant, penalite.getEtudiant());
        assertEquals(emprunt, penalite.getEmprunt());
        assertEquals(LocalDate.now().plusDays(5), penalite.getDateFin());
        assertTrue(penalite.getMotif().contains("Retard de 5 jour(s)"));
    }

    @Test
    void creerPenalite_RetardZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            penaliteService.creerPenalite(emprunt, 0);
        });
    }

    @Test
    void creerPenalite_RetardNegatif() {
        assertThrows(IllegalArgumentException.class, () -> {
            penaliteService.creerPenalite(emprunt, -3);
        });
    }

    @Test
    void penalite_EstActive() {
        Penalite penalite = penaliteService.creerPenalite(emprunt, 3);

        assertTrue(penalite.estActive());
        assertEquals(3, penalite.getJoursRestants());
    }

    @Test
    void penalite_EstExpiree() {
        Penalite penalite = new Penalite();
        penalite.setDateDebut(LocalDate.now().minusDays(10));
        penalite.setDateFin(LocalDate.now().minusDays(1));
        penalite.setDureeJours(9);

        assertFalse(penalite.estActive());
        assertEquals(0, penalite.getJoursRestants());
    }
}