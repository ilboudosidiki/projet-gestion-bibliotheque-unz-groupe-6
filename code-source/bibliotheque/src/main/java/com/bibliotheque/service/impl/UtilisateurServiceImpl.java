package com.bibliotheque.service.impl;

import com.bibliotheque.entity.Utilisateur;
import com.bibliotheque.repository.UtilisateurRepository;
import com.bibliotheque.service.interfaces.IUtilisateurService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurServiceImpl implements IUtilisateurService {
    
    private final UtilisateurRepository utilisateurRepository;
    
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }
    
    @Override
    public Utilisateur creerUtilisateur(Utilisateur utilisateur) {
        if (emailExiste(utilisateur.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }
        return utilisateurRepository.save(utilisateur);
    }
    
    @Override
    public Optional<Utilisateur> trouverParEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }
    
    @Override
    public Optional<Utilisateur> trouverParId(Long id) {
        return utilisateurRepository.findById(id);
    }
    
    @Override
    public List<Utilisateur> tousLesUtilisateurs() {
        return utilisateurRepository.findAll();
    }
    
    @Override
    public Utilisateur modifierUtilisateur(Long id, Utilisateur utilisateur) {
        Utilisateur existant = utilisateurRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        existant.setNom(utilisateur.getNom());
        existant.setPrenom(utilisateur.getPrenom());
        if (!existant.getEmail().equals(utilisateur.getEmail()) && 
            emailExiste(utilisateur.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }
        existant.setEmail(utilisateur.getEmail());
        return utilisateurRepository.save(existant);
    }
    
    @Override
    public void supprimerUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }
    
    @Override
    public boolean emailExiste(String email) {
        return utilisateurRepository.existsByEmail(email);
    }
}