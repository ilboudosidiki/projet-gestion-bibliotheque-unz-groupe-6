package com.bibliotheque.service;

import com.bibliotheque.entity.Emprunt;
import com.bibliotheque.entity.Etudiant;
import com.bibliotheque.entity.Exemplaire;
import com.bibliotheque.entity.Ouvrage;
import com.bibliotheque.enums.StatutEmprunt;
import com.bibliotheque.enums.StatutExemplaire;
import com.bibliotheque.exception.CompteSuspenduException;
import com.bibliotheque.exception.QuotaAtteintException;
import com.bibliotheque.repository.EmpruntRepository;
import com.bibliotheque.repository.EtudiantRepository;
import com.bibliotheque.repository.ExemplaireRepository;
import com.bibliotheque.service.impl.EmpruntServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpruntServiceTest {

    @Mock
    private EmpruntRepository empruntRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ExemplaireRepository exemplaireRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private EmpruntServiceImpl empruntService;

    private Etudiant etudiant;
    private Exemplaire exemplaire;
    private Ouvrage ouvrage;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setId(1L);
        etudiant.setNom("Konate");
        etudiant.setPrenom("Aminata");
        etudiant.setEmail("aminata@email.com");
        etudiant.setMatricule("ETU001");
        etudiant.setDateFinSuspension(null);

        ouvrage = new Ouvrage();
        ouvrage.setId(1L);
        ouvrage.setTitre("Spring Boot pour les nuls");
        ouvrage.setIsbn("978-2-212-12345-6");

        exemplaire = new Exemplaire();
        exemplaire.setId(1L);
        exemplaire.setCodeBarres("EX001");
        exemplaire.setStatut(StatutExemplaire.DISPONIBLE);
        exemplaire.setOuvrage(ouvrage);
    }

    @Test
    void creerEmprunt_Succes() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(exemplaireRepository.findById(1L)).thenReturn(Optional.of(exemplaire));
        when(empruntRepository.countEmpruntsEnCours(etudiant)).thenReturn(0);
        when(empruntRepository.save(any(Emprunt.class))).thenAnswer(i -> i.getArguments()[0]);

        Emprunt emprunt = empruntService.creerEmprunt(1L, 1L);

        assertNotNull(emprunt);
        assertEquals(StatutEmprunt.DEMANDE, emprunt.getStatut());
        assertEquals(LocalDate.now().plusDays(14), emprunt.getDateRetourPrevue());
        assertEquals(etudiant, emprunt.getEtudiant());
        assertEquals(exemplaire, emprunt.getExemplaire());
        verify(exemplaireRepository).save(exemplaire);
        verify(empruntRepository).save(any(Emprunt.class));
    }

    @Test
    void creerEmprunt_QuotaAtteint() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(exemplaireRepository.findById(1L)).thenReturn(Optional.of(exemplaire));
        when(empruntRepository.countEmpruntsEnCours(etudiant)).thenReturn(3);

        assertThrows(QuotaAtteintException.class, () -> {
            empruntService.creerEmprunt(1L, 1L);
        });
    }

    @Test
    void creerEmprunt_CompteSuspendu() {
        etudiant.setDateFinSuspension(LocalDate.now().plusDays(5));
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(exemplaireRepository.findById(1L)).thenReturn(Optional.of(exemplaire));

        assertThrows(CompteSuspenduException.class, () -> {
            empruntService.creerEmprunt(1L, 1L);
        });
    }

    @Test
    void peutEmprunter_True() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(empruntRepository.countEmpruntsEnCours(etudiant)).thenReturn(1);

        boolean resultat = empruntService.peutEmprunter(1L);

        assertTrue(resultat);
    }

    @Test
    void peutEmprunter_False_Quota() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(empruntRepository.countEmpruntsEnCours(etudiant)).thenReturn(3);

        boolean resultat = empruntService.peutEmprunter(1L);

        assertFalse(resultat);
    }

    @Test
    void prolongerEmprunt_Succes() {
        Emprunt emprunt = new Emprunt();
        emprunt.setId(1L);
        emprunt.setStatut(StatutEmprunt.EN_COURS);
        emprunt.setDateRetourPrevue(LocalDate.now().plusDays(3));

        when(empruntRepository.findById(1L)).thenReturn(Optional.of(emprunt));
        when(empruntRepository.save(any(Emprunt.class))).thenReturn(emprunt);

        Emprunt resultat = empruntService.prolongerEmprunt(1L);

        assertEquals(LocalDate.now().plusDays(10), resultat.getDateRetourPrevue());
    }

    @Test
    void calculerRetard_SansRetard() {
        Emprunt emprunt = new Emprunt();
        emprunt.setDateRetourPrevue(LocalDate.now().plusDays(5));
        emprunt.setDateRetourEffective(LocalDate.now());

        int retard = emprunt.calculerRetard();

        assertEquals(0, retard);
    }

    @Test
    void calculerRetard_AvecRetard() {
        Emprunt emprunt = new Emprunt();
        emprunt.setDateRetourPrevue(LocalDate.now().minusDays(5));
        emprunt.setDateRetourEffective(LocalDate.now());

        int retard = emprunt.calculerRetard();

        assertEquals(5, retard);
    }
}