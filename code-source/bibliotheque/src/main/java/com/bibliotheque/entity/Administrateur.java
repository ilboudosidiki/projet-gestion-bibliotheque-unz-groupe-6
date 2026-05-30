package com.bibliotheque.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Administrateur extends Utilisateur {
    
    private String niveauAcces;
    
    @Override
    public boolean peutEmprunter() {
        return false;
    }
}