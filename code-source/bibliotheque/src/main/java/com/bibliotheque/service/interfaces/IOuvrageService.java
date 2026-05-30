package com.bibliotheque.service.interfaces;

import com.bibliotheque.entity.Ouvrage;
import com.bibliotheque.entity.Exemplaire;
import java.util.List;

public interface IOuvrageService {
    Ouvrage ajouterOuvrage(Ouvrage ouvrage);
    Ouvrage modifierOuvrage(Long id, Ouvrage ouvrage);
    void supprimerOuvrage(Long id);
    Ouvrage trouverParId(Long id);
    List<Ouvrage> tousLesOuvrages();
    List<Ouvrage> rechercherParMotCle(String motCle);
    List<Ouvrage> rechercherParTitre(String titre);
    List<Ouvrage> rechercherParAuteur(String auteur);
    Ouvrage rechercherParIsbn(String isbn);
    Exemplaire ajouterExemplaire(Long ouvrageId, Exemplaire exemplaire);
    int getNbExemplairesDisponibles(Long ouvrageId);
}