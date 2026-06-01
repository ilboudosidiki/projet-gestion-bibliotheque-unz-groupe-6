package com.bibliotheque.service.impl;

import com.bibliotheque.entity.Exemplaire;
import com.bibliotheque.entity.Ouvrage;
import com.bibliotheque.enums.StatutExemplaire;
import com.bibliotheque.repository.ExemplaireRepository;
import com.bibliotheque.repository.OuvrageRepository;
import com.bibliotheque.repository.ReservationRepository;
import com.bibliotheque.service.interfaces.IOuvrageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OuvrageServiceImpl implements IOuvrageService {
    
    private final OuvrageRepository ouvrageRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final ReservationRepository reservationRepository;

    public OuvrageServiceImpl(OuvrageRepository ouvrageRepository,
                            ExemplaireRepository exemplaireRepository,
                            ReservationRepository reservationRepository) {
        this.ouvrageRepository = ouvrageRepository;
        this.exemplaireRepository = exemplaireRepository;
        this.reservationRepository = reservationRepository;
    }
    
    @Override
    public Ouvrage ajouterOuvrage(Ouvrage ouvrage) {
        return ouvrageRepository.save(ouvrage);
    }
    
    @Override
    public Ouvrage modifierOuvrage(Long id, Ouvrage ouvrage) {
        Ouvrage existant = ouvrageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ouvrage non trouvé"));
        existant.setTitre(ouvrage.getTitre());
        existant.setAuteur(ouvrage.getAuteur());
        existant.setIsbn(ouvrage.getIsbn());
        existant.setResume(ouvrage.getResume());
        existant.setMotsCles(ouvrage.getMotsCles());
        existant.setDatePublication(ouvrage.getDatePublication());
        existant.setCategorie(ouvrage.getCategorie());
        return ouvrageRepository.save(existant);
    }
    
    @Override
    @Transactional
    public void supprimerOuvrage(Long id) {
        Ouvrage ouvrage = ouvrageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ouvrage non trouvé"));
        
        // Supprimer les réservations liées
        reservationRepository.deleteAll(ouvrage.getReservations());
        
        // Supprimer les exemplaires liés
        exemplaireRepository.deleteAll(ouvrage.getExemplaires());
        
        // Supprimer l'ouvrage
        ouvrageRepository.delete(ouvrage);
    }
    
    @Override
    public Ouvrage trouverParId(Long id) {
        return ouvrageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ouvrage non trouvé"));
    }
    
    @Override
    public List<Ouvrage> tousLesOuvrages() {
        return ouvrageRepository.findAll();
    }
    
    @Override
    public List<Ouvrage> rechercherParMotCle(String motCle) {
        return ouvrageRepository.rechercheParMotCle(motCle);
    }
    
    @Override
    public List<Ouvrage> rechercherParTitre(String titre) {
        return ouvrageRepository.findByTitreContainingIgnoreCase(titre);
    }
    
    @Override
    public List<Ouvrage> rechercherParAuteur(String auteur) {
        return ouvrageRepository.findByAuteurContainingIgnoreCase(auteur);
    }
    
    @Override
    public Ouvrage rechercherParIsbn(String isbn) {
        return ouvrageRepository.findByIsbn(isbn)
            .orElseThrow(() -> new RuntimeException("Aucun ouvrage trouvé avec cet ISBN"));
    }
    
    @Override
    public Exemplaire ajouterExemplaire(Long ouvrageId, Exemplaire exemplaire) {
        Ouvrage ouvrage = trouverParId(ouvrageId);
        exemplaire.setOuvrage(ouvrage);
        exemplaire.setStatut(StatutExemplaire.DISPONIBLE);
        return exemplaireRepository.save(exemplaire);
    }
    
    @Override
    public int getNbExemplairesDisponibles(Long ouvrageId) {
        Ouvrage ouvrage = trouverParId(ouvrageId);
        return (int) exemplaireRepository.countByOuvrageAndStatut(
            ouvrage, StatutExemplaire.DISPONIBLE);
    }
}