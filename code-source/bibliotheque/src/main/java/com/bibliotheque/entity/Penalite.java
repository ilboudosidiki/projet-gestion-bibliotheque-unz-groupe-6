package com.bibliotheque.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Penalite {
    
    @Id
    @SequenceGenerator(name = "penalite_seq", sequenceName = "penalite_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "penalite_seq")
    private Long id;
    
    @Column(nullable = false)
    private LocalDate dateDebut;
    
    @Column(nullable = false)
    private Integer dureeJours;
    
    @Column(nullable = false)
    private String motif;
    
    @Column(nullable = false)
    private LocalDate dateFin;
    
    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;
    
    @OneToOne
    @JoinColumn(name = "emprunt_id", nullable = false)
    private Emprunt emprunt;
    
    public boolean estActive() {
        return LocalDate.now().isBefore(dateFin) || 
               LocalDate.now().isEqual(dateFin);
    }
    
    public long getJoursRestants() {
        if (!estActive()) return 0;
        return dateFin.toEpochDay() - LocalDate.now().toEpochDay();
    }
}