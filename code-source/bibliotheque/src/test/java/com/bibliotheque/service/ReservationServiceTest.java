package com.bibliotheque.service;

import com.bibliotheque.entity.Etudiant;
import com.bibliotheque.entity.Exemplaire;
import com.bibliotheque.entity.Ouvrage;
import com.bibliotheque.entity.Reservation;
import com.bibliotheque.enums.StatutExemplaire;
import com.bibliotheque.enums.StatutReservation;
import com.bibliotheque.exception.CompteSuspenduException;
import com.bibliotheque.exception.DejaDansFileException;
import com.bibliotheque.exception.OuvrageIndisponibleException;
import com.bibliotheque.repository.EtudiantRepository;
import com.bibliotheque.repository.ExemplaireRepository;
import com.bibliotheque.repository.OuvrageRepository;
import com.bibliotheque.repository.ReservationRepository;
import com.bibliotheque.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private OuvrageRepository ouvrageRepository;

    @Mock
    private ExemplaireRepository exemplaireRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Etudiant etudiant;
    private Ouvrage ouvrageIndisponible;
    private Ouvrage ouvrageDisponible;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setId(1L);
        etudiant.setNom("Diallo");
        etudiant.setPrenom("Moussa");
        etudiant.setEmail("moussa@email.com");
        etudiant.setMatricule("ETU002");

        ouvrageIndisponible = new Ouvrage();
        ouvrageIndisponible.setId(1L);
        ouvrageIndisponible.setTitre("Algorithmique Avancée");
        ouvrageIndisponible.setExemplaires(new ArrayList<>());

        ouvrageDisponible = new Ouvrage();
        ouvrageDisponible.setId(2L);
        ouvrageDisponible.setTitre("Java pour débutants");
        List<Exemplaire> exemplairesDisponibles = new ArrayList<>();
        Exemplaire ex1 = new Exemplaire();
        ex1.setStatut(StatutExemplaire.DISPONIBLE);
        Exemplaire ex2 = new Exemplaire();
        ex2.setStatut(StatutExemplaire.DISPONIBLE);
        exemplairesDisponibles.add(ex1);
        exemplairesDisponibles.add(ex2);
        ouvrageDisponible.setExemplaires(exemplairesDisponibles);
    }

    @Test
    void creerReservation_Succes() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(ouvrageRepository.findById(1L)).thenReturn(Optional.of(ouvrageIndisponible));
        when(reservationRepository.existsByEtudiantAndOuvrageAndStatut(
            etudiant, ouvrageIndisponible, StatutReservation.EN_ATTENTE)).thenReturn(false);
        when(reservationRepository.findByOuvrageAndStatutOrderByDateReservationAsc(
            ouvrageIndisponible, StatutReservation.EN_ATTENTE)).thenReturn(new ArrayList<>());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArguments()[0]);

        Reservation reservation = reservationService.creerReservation(1L, 1L);

        assertNotNull(reservation);
        assertEquals(StatutReservation.EN_ATTENTE, reservation.getStatut());
        assertEquals(etudiant, reservation.getEtudiant());
        assertEquals(ouvrageIndisponible, reservation.getOuvrage());
    }

    @Test
    void creerReservation_CompteSuspendu() {
        etudiant.setDateFinSuspension(LocalDate.now().plusDays(10));
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(ouvrageRepository.findById(1L)).thenReturn(Optional.of(ouvrageIndisponible));

        assertThrows(CompteSuspenduException.class, () -> {
            reservationService.creerReservation(1L, 1L);
        });
    }

    @Test
    void creerReservation_OuvrageDisponible() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(ouvrageRepository.findById(2L)).thenReturn(Optional.of(ouvrageDisponible));

        assertThrows(OuvrageIndisponibleException.class, () -> {
            reservationService.creerReservation(1L, 2L);
        });
    }

    @Test
    void creerReservation_DejaDansFile() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(ouvrageRepository.findById(1L)).thenReturn(Optional.of(ouvrageIndisponible));
        when(reservationRepository.existsByEtudiantAndOuvrageAndStatut(
            etudiant, ouvrageIndisponible, StatutReservation.EN_ATTENTE)).thenReturn(true);

        assertThrows(DejaDansFileException.class, () -> {
            reservationService.creerReservation(1L, 1L);
        });
    }

    @Test
    void annulerReservation_Succes() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatut(StatutReservation.EN_ATTENTE);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        reservationService.annulerReservation(1L);

        assertEquals(StatutReservation.ANNULEE, reservation.getStatut());
    }

    @Test
    void getFileAttente_Vide() {
        when(ouvrageRepository.findById(1L)).thenReturn(Optional.of(ouvrageIndisponible));
        when(reservationRepository.findByOuvrageAndStatutOrderByDateReservationAsc(
            ouvrageIndisponible, StatutReservation.EN_ATTENTE)).thenReturn(new ArrayList<>());

        var file = reservationService.getFileAttente(1L);

        assertTrue(file.isEmpty());
    }
}