package com.bibliotheque.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Bibliothecaire extends Utilisateur {
    
    @Column(unique = true)
    private String numeroEmploye;
    
    @Override
    public boolean peutEmprunter() {
        return false;
    }
}