package com.bibliotheque.entity;

import com.bibliotheque.enums.StatutExemplaire;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exemplaire {
    
    @Id
    @SequenceGenerator(name = "exemplaire_seq", sequenceName = "exemplaire_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exemplaire_seq")
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String codeBarres;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutExemplaire statut = StatutExemplaire.DISPONIBLE;
    
    private String rayon;
    
    private LocalDate dateAcquisition = LocalDate.now();
    
    @ManyToOne
    @JoinColumn(name = "ouvrage_id", nullable = false)
    @JsonIgnoreProperties({"exemplaires", "reservations"})
    private Ouvrage ouvrage;
    
    public boolean estEmpruntable() {
        return statut == StatutExemplaire.DISPONIBLE;
    }
    
    public void changerStatut(StatutExemplaire nouveauStatut) {
        this.statut = nouveauStatut;
    }
}