package com.bibliotheque.entity;

import com.bibliotheque.enums.StatutEmprunt;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emprunt {
    
    @Id
    @SequenceGenerator(name = "emprunt_seq", sequenceName = "emprunt_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emprunt_seq")
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime dateDebut = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDate dateRetourPrevue;
    
    private LocalDate dateRetourEffective;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutEmprunt statut = StatutEmprunt.DEMANDE;
    
    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    @JsonIgnoreProperties({"motDePasse", "dateCreation"})
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "exemplaire_id", nullable = false)
    @JsonIgnoreProperties("ouvrage")
    private Exemplaire exemplaire;
    
    @OneToOne(mappedBy = "emprunt", cascade = CascadeType.ALL)
    private Penalite penalite;
    
    public int calculerRetard() {
        if (dateRetourEffective == null || dateRetourPrevue == null) {
            return 0;
        }
        if (dateRetourEffective.isAfter(dateRetourPrevue)) {
            return (int) (dateRetourEffective.toEpochDay() - dateRetourPrevue.toEpochDay());
        }
        return 0;
    }
    
    public boolean peutEtreProlonge() {
        return statut == StatutEmprunt.EN_COURS && calculerRetard() == 0;
    }
}