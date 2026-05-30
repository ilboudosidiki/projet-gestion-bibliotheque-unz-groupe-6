package com.bibliotheque.service.impl;

import com.bibliotheque.entity.Emprunt;
import com.bibliotheque.entity.Etudiant;
import com.bibliotheque.entity.Penalite;
import com.bibliotheque.repository.EtudiantRepository;
import com.bibliotheque.repository.PenaliteRepository;
import com.bibliotheque.service.interfaces.IPenaliteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class PenaliteServiceImpl implements IPenaliteService {
    
    private final PenaliteRepository penaliteRepository;
    private final EtudiantRepository etudiantRepository;
    
    public PenaliteServiceImpl(PenaliteRepository penaliteRepository,
                                EtudiantRepository etudiantRepository) {
        this.penaliteRepository = penaliteRepository;
        this.etudiantRepository = etudiantRepository;
    }
    
    @Override
    public Penalite creerPenalite(Emprunt emprunt, int joursRetard) {
        if (joursRetard <= 0) {
            throw new IllegalArgumentException("Le nombre de jours de retard doit être positif");
        }
        
        Penalite penalite = new Penalite();
        penalite.setEmprunt(emprunt);
        penalite.setEtudiant(emprunt.getEtudiant());
        penalite.setDateDebut(LocalDate.now());
        penalite.setDureeJours(joursRetard);
        penalite.setDateFin(LocalDate.now().plusDays(joursRetard));
        penalite.setMotif("Retard de " + joursRetard + " jour(s) sur l'ouvrage : " +
                          emprunt.getExemplaire().getOuvrage().getTitre());
        
        return penalite;
    }
    
    @Override
    @Transactional
    public Penalite appliquerPenalite(Emprunt emprunt, int joursRetard) {
        Penalite penalite = creerPenalite(emprunt, joursRetard);
        penaliteRepository.save(penalite);
        
        Etudiant etudiant = emprunt.getEtudiant();
        etudiant.setDateFinSuspension(LocalDate.now().plusDays(joursRetard));
        etudiantRepository.save(etudiant);
        
        return penalite;
    }
    
    @Override
    public List<Penalite> getPenalitesActives(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        return penaliteRepository.findByEtudiantAndDateFinAfter(
            etudiant, LocalDate.now());
    }
    
    @Override
    @Transactional
    public void leverSuspension(Long etudiantId, String motif) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        etudiant.setDateFinSuspension(null);
        etudiantRepository.save(etudiant);
    }
    
    @Override
    public boolean estSuspendu(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
        return etudiant.estSuspendu();
    }
}