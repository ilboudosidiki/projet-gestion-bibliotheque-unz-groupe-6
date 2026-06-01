package com.bibliotheque.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.bibliotheque.enums.StatutExemplaire;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ouvrage {
    
    @Id
    @SequenceGenerator(name = "ouvrage_seq", sequenceName = "ouvrage_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ouvrage_seq")
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String isbn;
    
    @Column(nullable = false)
    private String titre;
    
    @Column(nullable = false)
    private String auteur;
    
    @Column(length = 2000)
    private String resume;
    
    private String motsCles;
    
    private LocalDate datePublication;
    
    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;
    
    @OneToMany(mappedBy = "ouvrage", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Exemplaire> exemplaires;

    @OneToMany(mappedBy = "ouvrage", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reservation> reservations;

    
    public int getNbExemplairesDisponibles() {
        if (exemplaires == null || exemplaires.isEmpty()) {
            return 0;
        }
        return (int) exemplaires.stream()
            .filter(e -> e.getStatut() == StatutExemplaire.DISPONIBLE)
            .count();
    }
    
    public boolean estDisponible() {
        return getNbExemplairesDisponibles() > 0;
    }
}