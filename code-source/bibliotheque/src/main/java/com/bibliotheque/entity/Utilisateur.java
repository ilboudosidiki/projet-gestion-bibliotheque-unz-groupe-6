package com.bibliotheque.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.bibliotheque.enums.RoleUtilisateur;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Utilisateur {
    
    @Id
    @SequenceGenerator(name = "utilisateur_seq", sequenceName = "utilisateur_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "utilisateur_seq")
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String prenom;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String motDePasse;
    
    @Column(nullable = false)
    private boolean estActif = true;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleUtilisateur role;
    
    private LocalDateTime dateCreation = LocalDateTime.now();
    
    public abstract boolean peutEmprunter();
    
    public String getNomComplet() {
        return prenom + " " + nom;
    }

}