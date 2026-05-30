package com.bibliotheque.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private Long id;
    private String nomEtudiant;
    private String matriculeEtudiant;
    private String titreOuvrage;
    private LocalDateTime dateReservation;
    private LocalDateTime dateExpiration;
    private int positionFile;
    private String statut;
}