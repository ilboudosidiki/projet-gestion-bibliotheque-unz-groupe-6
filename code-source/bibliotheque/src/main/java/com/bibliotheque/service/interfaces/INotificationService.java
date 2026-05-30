package com.bibliotheque.service.interfaces;

public interface INotificationService {
    void envoyerRappelEcheance(String email, String titreOuvrage, String dateRetour);
    void envoyerNotificationDisponibilite(String email, String titreOuvrage);
    void envoyerNotificationSuspension(String email, String dateFin);
    void envoyerNotificationAnnulationReservation(String email, String titreOuvrage);
}