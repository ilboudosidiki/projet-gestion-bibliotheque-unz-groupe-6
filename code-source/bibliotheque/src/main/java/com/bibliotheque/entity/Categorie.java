package com.bibliotheque.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categorie {
    
    @Id
    @SequenceGenerator(name = "categorie_seq", sequenceName = "categorie_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categorie_seq")
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nom;
    
    private String description;
    
    @OneToMany(mappedBy = "categorie")
    private List<Ouvrage> ouvrages;
}