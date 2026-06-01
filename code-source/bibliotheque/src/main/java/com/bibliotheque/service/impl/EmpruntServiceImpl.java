package com.bibliotheque.service.impl;

import com.bibliotheque.entity.Emprunt;
import com.bibliotheque.entity.Etudiant;
import com.bibliotheque.entity.Exemplaire;
import com.bibliotheque.entity.Ouvrage;
import com.bibliotheque.entity.Reservation;
import com.bibliotheque.enums.StatutEmprunt;
import com.bibliotheque.enums.StatutExemplaire;
import com.bibliotheque.enums.StatutReservation;
import com.bibliotheque.event.RetourEffectueEvent;
import com.bibliotheque.exception.CompteSuspenduException;
import com.bibliotheque.exception.OuvrageIndisponibleException;
import com.bibliotheque.exception.QuotaAtteintException;
import com.bibliotheque.repository.EmpruntRepository;
import com.bibliotheque.repository.EtudiantRepository;
import com.bibliotheque.repository.ExemplaireRepository;
import com.bibliotheque.repository.OuvrageRepository;
import com.bibliotheque.repository.ReservationRepository;
import com.bibliotheque.service.interfaces.IEmpruntService;
import com.bibliotheque.service.interfaces.INotificationService;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpruntServiceImpl implements IEmpruntService {
    
    private final EmpruntRepository empruntRepository;
    private final EtudiantRepository etudiantRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final OuvrageRepository ouvrageRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ReservationRepository reservationRepository;
    private final INotificationService notificationService;

    public EmpruntServiceImpl(EmpruntRepository empruntRepository,
                            EtudiantRepository etudiantRepository,
                            ExemplaireRepository exemplaireRepository,
                            OuvrageRepository ouvrageRepository,
                            ApplicationEventPublisher eventPublisher,
                            ReservationRepository reservationRepository,
                            INotificationService notificationService) {
        this.empruntRepository = empruntRepository;
        this.etudiantRepository = etudiantRepository;
        this.exemplaireRepository = exemplaireRepository;
        this.ouvrageRepository = ouvrageRepository;
        this.eventPublisher = eventPublisher;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }
    
    @Override
    @Transactional
    public Emprunt creerEmprunt(Long etudiantId, Long exemplaireId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        Exemplaire exemplaire = exemplaireRepository.findById(exemplaireId)
            .orElseThrow(() -> new RuntimeException("Exemplaire non trouvé"));
        
        if (etudiant.estSuspendu()) {
            throw new CompteSuspenduException(
                "Compte suspendu jusqu'au " + etudiant.getDateFinSuspension());
        }
        
        if (!peutEmprunter(etudiantId)) {
            throw new QuotaAtteintException("Limite maximale de 3 emprunts atteinte");
        }
        
        if (!exemplaire.estEmpruntable()) {
            throw new OuvrageIndisponibleException("Cet exemplaire n'est pas disponible");
        }
        
        Emprunt emprunt = new Emprunt();
        emprunt.setEtudiant(etudiant);
        emprunt.setExemplaire(exemplaire);
        emprunt.setDateRetourPrevue(LocalDate.now().plusDays(14));
        emprunt.setStatut(StatutEmprunt.DEMANDE);
        
        exemplaire.changerStatut(StatutExemplaire.EMPRUNTE);
        exemplaireRepository.save(exemplaire);
        
        return empruntRepository.save(emprunt);
    }

    @Transactional
    public Emprunt creerEmpruntParOuvrage(Long etudiantId, Long ouvrageId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        
        Ouvrage ouvrage = ouvrageRepository.findById(ouvrageId)
            .orElseThrow(() -> new RuntimeException("Ouvrage non trouvé"));
        
        List<Exemplaire> disponibles = exemplaireRepository
            .findByOuvrageAndStatut(ouvrage, StatutExemplaire.DISPONIBLE);
        
        if (disponibles.isEmpty()) {
            throw new OuvrageIndisponibleException("Aucun exemplaire disponible pour cet ouvrage");
        }
        
        Exemplaire exemplaire = disponibles.get(0);
        
        if (etudiant.estSuspendu()) {
            throw new CompteSuspenduException(
                "Compte suspendu jusqu'au " + etudiant.getDateFinSuspension());
        }
        
        if (!peutEmprunter(etudiantId)) {
            throw new QuotaAtteintException("Limite maximale de 3 emprunts atteinte");
        }
        
        Emprunt emprunt = new Emprunt();
        emprunt.setEtudiant(etudiant);
        emprunt.setExemplaire(exemplaire);
        emprunt.setDateRetourPrevue(LocalDate.now().plusDays(14));
        emprunt.setStatut(StatutEmprunt.DEMANDE);
        
        exemplaire.changerStatut(StatutExemplaire.EMPRUNTE);
        exemplaireRepository.save(exemplaire);
        
        return empruntRepository.save(emprunt);
    }
    
    @Override
    @Transactional
    public Emprunt validerEmprunt(Long empruntId) {
        Emprunt emprunt = empruntRepository.findById(empruntId)
            .orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));
        emprunt.setStatut(StatutEmprunt.EN_COURS);
        return empruntRepository.save(emprunt);
    }
    
    @Override
    @Transactional
    public void enregistrerRetour(Long exemplaireId) {
        Exemplaire exemplaire = exemplaireRepository.findById(exemplaireId)
            .orElseThrow(() -> new RuntimeException("Exemplaire non trouvé"));
        
        Emprunt emprunt = empruntRepository
            .findByExemplaireAndStatut(exemplaire, StatutEmprunt.EN_COURS)
            .orElseThrow(() -> new RuntimeException("Aucun emprunt en cours trouvé"));
        
        emprunt.setDateRetourEffective(LocalDate.now());
        emprunt.setStatut(StatutEmprunt.RETOURNE);
        empruntRepository.save(emprunt);
        
        Ouvrage ouvrage = exemplaire.getOuvrage();
        List<Reservation> fileAttente = reservationRepository
            .findByOuvrageAndStatutOrderByDateReservationAsc(ouvrage, StatutReservation.EN_ATTENTE);
        
        if (!fileAttente.isEmpty()) {
            Reservation prochaine = fileAttente.get(0);
            prochaine.setStatut(StatutReservation.PRETE_A_RECUPERER);
            prochaine.setDateExpiration(LocalDateTime.now().plusHours(48));
            reservationRepository.save(prochaine);
        }
        
        exemplaire.setStatut(StatutExemplaire.DISPONIBLE);
        exemplaireRepository.save(exemplaire);
        
        Etudiant etudiant = emprunt.getEtudiant();
        eventPublisher.publishEvent(new RetourEffectueEvent(
            this, exemplaire, etudiant, ouvrage));
    }
    
    @Override
    public List<Emprunt> getEmpruntsEnCours(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        return empruntRepository.findByEtudiantAndStatut(etudiant, StatutEmprunt.EN_COURS);
    }
    
    @Override
    public List<Emprunt> getHistoriqueEmprunts(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        return empruntRepository.findByEtudiant(etudiant);
    }
    
    @Override
    @Transactional
    public Emprunt prolongerEmprunt(Long empruntId) {
        Emprunt emprunt = empruntRepository.findById(empruntId)
            .orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));
        
        if (!emprunt.peutEtreProlonge()) {
            throw new RuntimeException("Cet emprunt ne peut pas être prolongé");
        }
        
        emprunt.setDateRetourPrevue(emprunt.getDateRetourPrevue().plusDays(7));
        return empruntRepository.save(emprunt);
    }
    
    @Override
    public boolean peutEmprunter(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        int nbEmprunts = empruntRepository.countEmpruntsEnCours(etudiant);
        return nbEmprunts < 3 && !etudiant.estSuspendu();
    }
}