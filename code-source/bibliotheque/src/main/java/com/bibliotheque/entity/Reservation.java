package com.bibliotheque.entity;

import com.bibliotheque.enums.StatutReservation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    
    @Id
    @SequenceGenerator(name = "reservation_seq", sequenceName = "reservation_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_seq")
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime dateReservation = LocalDateTime.now();
    
    private LocalDateTime dateExpiration;
    
    private Integer positionFile;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReservation statut = StatutReservation.EN_ATTENTE;
    
    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    @JsonIgnoreProperties({"motDePasse", "dateCreation", "role"})
    private Etudiant etudiant;
    
    @ManyToOne
    @JoinColumn(name = "ouvrage_id", nullable = false)
    private Ouvrage ouvrage;
    
    public boolean estExpiree() {
        return dateExpiration != null && 
               LocalDateTime.now().isAfter(dateExpiration);
    }
}