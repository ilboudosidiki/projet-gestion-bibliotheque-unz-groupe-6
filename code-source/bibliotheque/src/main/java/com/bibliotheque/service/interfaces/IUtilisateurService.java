package com.bibliotheque.service.interfaces;

import com.bibliotheque.entity.Utilisateur;
import java.util.List;
import java.util.Optional;

public interface IUtilisateurService {
    Utilisateur creerUtilisateur(Utilisateur utilisateur);
    Optional<Utilisateur> trouverParEmail(String email);
    Optional<Utilisateur> trouverParId(Long id);
    List<Utilisateur> tousLesUtilisateurs();
    Utilisateur modifierUtilisateur(Long id, Utilisateur utilisateur);
    void supprimerUtilisateur(Long id);
    boolean emailExiste(String email);
}