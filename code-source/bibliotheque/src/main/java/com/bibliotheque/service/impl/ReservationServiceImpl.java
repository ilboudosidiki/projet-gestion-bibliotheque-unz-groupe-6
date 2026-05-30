package com.bibliotheque.service.impl;

import com.bibliotheque.entity.Etudiant;
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
import com.bibliotheque.service.interfaces.IReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bibliotheque.service.interfaces.INotificationService;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements IReservationService {
    
    private final ReservationRepository reservationRepository;
    private final EtudiantRepository etudiantRepository;
    private final OuvrageRepository ouvrageRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final INotificationService notificationService;
    
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                EtudiantRepository etudiantRepository,
                                OuvrageRepository ouvrageRepository,
                                ExemplaireRepository exemplaireRepository,
                                INotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.etudiantRepository = etudiantRepository;
        this.ouvrageRepository = ouvrageRepository;
        this.exemplaireRepository = exemplaireRepository;
        this.notificationService = notificationService;
    }
    
    @Override
    @Transactional
    public Reservation creerReservation(Long etudiantId, Long ouvrageId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        Ouvrage ouvrage = ouvrageRepository.findById(ouvrageId)
            .orElseThrow(() -> new RuntimeException("Ouvrage non trouvé"));
        
        if (etudiant.estSuspendu()) {
            throw new CompteSuspenduException(
                "Compte suspendu jusqu'au " + etudiant.getDateFinSuspension());
        }
        
        if (ouvrage.estDisponible()) {
            throw new OuvrageIndisponibleException(
                "Cet ouvrage est disponible, vous pouvez l'emprunter directement");
        }
        
        if (reservationRepository.existsByEtudiantAndOuvrageAndStatut(
            etudiant, ouvrage, StatutReservation.EN_ATTENTE)) {
            throw new DejaDansFileException("Vous avez déjà réservé cet ouvrage");
        }
        
        Reservation reservation = new Reservation();
        reservation.setEtudiant(etudiant);
        reservation.setOuvrage(ouvrage);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        
        reservationRepository.save(reservation);
        
        int position = getPositionDansFile(etudiantId, ouvrageId);
        reservation.setPositionFile(position);
        
        return reservationRepository.save(reservation);
    }
    
    @Override
    @Transactional
    public void annulerReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        reservation.setStatut(StatutReservation.ANNULEE);
        reservationRepository.save(reservation);
    }
    
    @Override
    public List<Reservation> getFileAttente(Long ouvrageId) {
        Ouvrage ouvrage = ouvrageRepository.findById(ouvrageId)
            .orElseThrow(() -> new RuntimeException("Ouvrage non trouvé"));
        return reservationRepository
            .findByOuvrageAndStatutOrderByDateReservationAsc(
                ouvrage, StatutReservation.EN_ATTENTE);
    }
    
    @Override
    public int getPositionDansFile(Long etudiantId, Long ouvrageId) {
        List<Reservation> file = getFileAttente(ouvrageId);
        for (int i = 0; i < file.size(); i++) {
            if (file.get(i).getEtudiant().getId().equals(etudiantId)) {
                return i + 1;
            }
        }
        return -1;
    }
    
    @Override
    @Transactional
    public void traiterRetourOuvrage(Long ouvrageId) {
        Ouvrage ouvrage = ouvrageRepository.findById(ouvrageId)
            .orElseThrow(() -> new RuntimeException("Ouvrage non trouvé"));
        
        List<Reservation> fileAttente = getFileAttente(ouvrageId);
        
        if (!fileAttente.isEmpty()) {
            Reservation prochaine = fileAttente.get(0);
            prochaine.setStatut(StatutReservation.PRETE_A_RECUPERER);
            prochaine.setDateExpiration(LocalDateTime.now().plusHours(48));
            reservationRepository.save(prochaine);
            
            // Envoyer notification
            notificationService.envoyerNotificationDisponibilite(
                prochaine.getEtudiant().getEmail(),
                ouvrage.getTitre());
        }
    }
    
    @Override
    @Transactional
    public void annulerReservationsExpirees() {
        List<Reservation> expirees = reservationRepository
            .findByStatutAndDateExpirationBefore(
                StatutReservation.PRETE_A_RECUPERER, LocalDateTime.now());
        
        for (Reservation r : expirees) {
            r.setStatut(StatutReservation.EXPIREE);
            reservationRepository.save(r);
        }
    }
}