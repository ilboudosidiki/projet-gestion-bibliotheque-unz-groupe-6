package com.bibliotheque.service.interfaces;

import com.bibliotheque.entity.Reservation;
import java.util.List;

public interface IReservationService {
    Reservation creerReservation(Long etudiantId, Long ouvrageId);
    void annulerReservation(Long reservationId);
    List<Reservation> getFileAttente(Long ouvrageId);
    int getPositionDansFile(Long etudiantId, Long ouvrageId);
    void traiterRetourOuvrage(Long ouvrageId);
    void annulerReservationsExpirees();
}