package com.bibliotheque.controller;

import com.bibliotheque.dto.OuvrageDTO;
import com.bibliotheque.entity.Exemplaire;
import com.bibliotheque.entity.Ouvrage;
import com.bibliotheque.service.interfaces.IOuvrageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ouvrages")
public class OuvrageController {

    private final IOuvrageService ouvrageService;

    public OuvrageController(IOuvrageService ouvrageService) {
        this.ouvrageService = ouvrageService;
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<OuvrageDTO>> rechercher(@RequestParam String motCle) {
        List<Ouvrage> ouvrages = ouvrageService.rechercherParMotCle(motCle);
        List<OuvrageDTO> dtos = ouvrages.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    public ResponseEntity<List<OuvrageDTO>> tousLesOuvrages() {
        List<Ouvrage> ouvrages = ouvrageService.tousLesOuvrages();
        List<OuvrageDTO> dtos = ouvrages.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OuvrageDTO> getOuvrage(@PathVariable Long id) {
        Ouvrage ouvrage = ouvrageService.trouverParId(id);
        return ResponseEntity.ok(toDTO(ouvrage));
    }

    @PostMapping
    public ResponseEntity<OuvrageDTO> ajouterOuvrage(@RequestBody Ouvrage ouvrage) {
        Ouvrage nouveau = ouvrageService.ajouterOuvrage(ouvrage);
        return ResponseEntity.ok(toDTO(nouveau));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OuvrageDTO> modifierOuvrage(@PathVariable Long id, 
                                                       @RequestBody Ouvrage ouvrage) {
        Ouvrage modifie = ouvrageService.modifierOuvrage(id, ouvrage);
        return ResponseEntity.ok(toDTO(modifie));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerOuvrage(@PathVariable Long id) {
        ouvrageService.supprimerOuvrage(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/exemplaires")
    public ResponseEntity<?> ajouterExemplaire(@PathVariable Long id, 
                                                @RequestBody Exemplaire exemplaire) {
        ouvrageService.ajouterExemplaire(id, exemplaire);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/exemplaires")
    public ResponseEntity<?> getExemplaires(@PathVariable Long id) {
        Ouvrage ouvrage = ouvrageService.trouverParId(id);
        return ResponseEntity.ok(ouvrage.getExemplaires());
    }

    private OuvrageDTO toDTO(Ouvrage ouvrage) {
        OuvrageDTO dto = new OuvrageDTO();
        dto.setId(ouvrage.getId());
        dto.setIsbn(ouvrage.getIsbn());
        dto.setTitre(ouvrage.getTitre());
        dto.setAuteur(ouvrage.getAuteur());
        dto.setResume(ouvrage.getResume());
        dto.setMotsCles(ouvrage.getMotsCles());
        dto.setDatePublication(ouvrage.getDatePublication());
        dto.setCategorie(ouvrage.getCategorie() != null ? 
                ouvrage.getCategorie().getNom() : null);
        dto.setNbExemplairesDisponibles(ouvrage.getNbExemplairesDisponibles());
        dto.setNbExemplairesTotal(ouvrage.getExemplaires() != null ? 
                ouvrage.getExemplaires().size() : 0);
        dto.setDisponible(ouvrage.estDisponible());
        return dto;
    }
}