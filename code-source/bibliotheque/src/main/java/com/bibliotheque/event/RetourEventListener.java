package com.bibliotheque.event;

import com.bibliotheque.entity.Emprunt;
import com.bibliotheque.entity.Exemplaire;
import com.bibliotheque.enums.StatutEmprunt;
import com.bibliotheque.enums.StatutExemplaire;
import com.bibliotheque.repository.EmpruntRepository;
import com.bibliotheque.repository.ExemplaireRepository;
import com.bibliotheque.service.interfaces.INotificationService;
import com.bibliotheque.service.interfaces.IPenaliteService;
import com.bibliotheque.service.interfaces.IReservationService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RetourEventListener {

    private final IPenaliteService penaliteService;
    private final IReservationService reservationService;
    private final INotificationService notificationService;
    private final EmpruntRepository empruntRepository;
    private final ExemplaireRepository exemplaireRepository;

    public RetourEventListener(IPenaliteService penaliteService,
                                IReservationService reservationService,
                                INotificationService notificationService,
                                EmpruntRepository empruntRepository,
                                ExemplaireRepository exemplaireRepository) {
        this.penaliteService = penaliteService;
        this.reservationService = reservationService;
        this.notificationService = notificationService;
        this.empruntRepository = empruntRepository;
        this.exemplaireRepository = exemplaireRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handleRetourEffectue(RetourEffectueEvent event) {
        Exemplaire exemplaire = event.getExemplaire();
        
        Emprunt emprunt = empruntRepository
            .findByExemplaireAndStatut(exemplaire, StatutEmprunt.RETOURNE)
            .orElse(null);

        if (emprunt != null) {
            int retard = emprunt.calculerRetard();
            if (retard > 0) {
                penaliteService.appliquerPenalite(emprunt, retard);
                notificationService.envoyerNotificationSuspension(
                    event.getEtudiant().getEmail(),
                    event.getEtudiant().getDateFinSuspension() != null ? 
                        event.getEtudiant().getDateFinSuspension().toString() : "");
            }
        }

        reservationService.traiterRetourOuvrage(event.getOuvrage().getId());

        if (reservationService.getFileAttente(event.getOuvrage().getId()).isEmpty()) {
            exemplaire.setStatut(StatutExemplaire.DISPONIBLE);
        } else {
            exemplaire.setStatut(StatutExemplaire.RESERVE);
        }
        exemplaireRepository.save(exemplaire);
    }
}