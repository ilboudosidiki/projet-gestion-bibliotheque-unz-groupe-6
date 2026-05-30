package com.bibliotheque.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class OuvrageDTO {
    private Long id;
    private String isbn;
    private String titre;
    private String auteur;
    private String resume;
    private String motsCles;
    private LocalDate datePublication;
    private String categorie;
    private int nbExemplairesDisponibles;
    private int nbExemplairesTotal;
    private boolean disponible;
    private String rayon;
}