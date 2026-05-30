package com.bibliotheque.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Etudiant extends Utilisateur {
    
    @Column(unique = true, nullable = false)
    private String matricule;
    
    private LocalDate dateFinSuspension;
    
    @Override
    public boolean peutEmprunter() {
        return !estSuspendu();
    }
    
    public boolean estSuspendu() {
        return dateFinSuspension != null && 
               dateFinSuspension.isAfter(LocalDate.now());
    }
}