package com.bibliotheque.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EmpruntDTO {
    private Long id;
    private String nomEtudiant;
    private String matriculeEtudiant;
    private String titreOuvrage;
    private String codeBarresExemplaire;
    private String rayon;
    private LocalDateTime dateDebut;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private String statut;
    private int retard;
}