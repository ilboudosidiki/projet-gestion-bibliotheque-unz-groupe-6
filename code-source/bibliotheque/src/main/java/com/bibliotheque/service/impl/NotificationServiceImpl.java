package com.bibliotheque.service.impl;

import com.bibliotheque.service.interfaces.INotificationService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements INotificationService {
    
    private final JavaMailSender mailSender;
    
    public NotificationServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    @Override
    @Async
    public void envoyerRappelEcheance(String email, String titreOuvrage, String dateRetour) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Rappel : Retour de livre - Bibliothèque UNZ");
        message.setText("Bonjour,\n\n" +
            "Nous vous rappelons que l'ouvrage \"" + titreOuvrage + "\" " +
            "doit être retourné avant le " + dateRetour + ".\n\n" +
            "Cordialement,\nBibliothèque Université Norbert Zongo");
        mailSender.send(message);
    }
    
    @Override
    @Async
    public void envoyerNotificationDisponibilite(String email, String titreOuvrage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Ouvrage disponible - Bibliothèque UNZ");
        message.setText("Bonjour,\n\n" +
            "L'ouvrage \"" + titreOuvrage + "\" que vous avez réservé est disponible.\n" +
            "Vous avez 48 heures pour venir le récupérer à la bibliothèque.\n\n" +
            "Cordialement,\nBibliothèque Université Norbert Zongo");
        mailSender.send(message);
    }
    
    @Override
    @Async
    public void envoyerNotificationSuspension(String email, String dateFin) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Suspension de compte - Bibliothèque UNZ");
        message.setText("Bonjour,\n\n" +
            "Votre compte a été suspendu suite à un retard de retour.\n" +
            "La suspension est effective jusqu'au " + dateFin + ".\n" +
            "Durant cette période, vous ne pouvez ni emprunter ni réserver.\n\n" +
            "Cordialement,\nBibliothèque Université Norbert Zongo");
        mailSender.send(message);
    }
    
    @Override
    @Async
    public void envoyerNotificationAnnulationReservation(String email, String titreOuvrage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Réservation expirée - Bibliothèque UNZ");
        message.setText("Bonjour,\n\n" +
            "Votre réservation pour l'ouvrage \"" + titreOuvrage + "\" " +
            "a été annulée car vous ne l'avez pas récupéré dans le délai de 48 heures.\n\n" +
            "Cordialement,\nBibliothèque Université Norbert Zongo");
        mailSender.send(message);
    }
}