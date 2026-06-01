package com.bibliotheque.controller;

import com.bibliotheque.dto.EmpruntDTO;
import com.bibliotheque.entity.Emprunt;
import com.bibliotheque.entity.Exemplaire;
import com.bibliotheque.enums.StatutEmprunt;
import com.bibliotheque.enums.StatutExemplaire;
import com.bibliotheque.repository.ExemplaireRepository;
import com.bibliotheque.repository.EmpruntRepository;
import com.bibliotheque.service.impl.EmpruntServiceImpl;
import com.bibliotheque.service.interfaces.IEmpruntService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emprunts")
public class EmpruntController {

    private final IEmpruntService empruntService;
    private final ExemplaireRepository exemplaireRepository;
    private final EmpruntRepository empruntRepository;

    public EmpruntController(IEmpruntService empruntService,
                            ExemplaireRepository exemplaireRepository,
                            EmpruntRepository empruntRepository) {
        this.empruntService = empruntService;
        this.exemplaireRepository = exemplaireRepository;
        this.empruntRepository = empruntRepository;
    }

    @PostMapping
    public ResponseEntity<?> emprunter(@RequestBody Map<String, Long> request) {
        Long etudiantId = request.get("etudiantId");
        Long exemplaireId = request.get("exemplaireId");

        Emprunt emprunt = empruntService.creerEmprunt(etudiantId, exemplaireId);
        return ResponseEntity.ok(toDTO(emprunt));
    }

    @PostMapping("/ouvrage")
    public ResponseEntity<?> emprunterParOuvrage(@RequestBody Map<String, Long> request) {
        Long etudiantId = request.get("etudiantId");
        Long ouvrageId = request.get("ouvrageId");
        Emprunt emprunt = ((EmpruntServiceImpl) empruntService).creerEmpruntParOuvrage(etudiantId, ouvrageId);
        return ResponseEntity.ok(toDTO(emprunt));
    }

    @PutMapping("/{id}/valider")
    public ResponseEntity<?> validerEmprunt(@PathVariable Long id) {
        Emprunt emprunt = empruntService.validerEmprunt(id);
        return ResponseEntity.ok(toDTO(emprunt));
    }

    @PostMapping("/retour/{exemplaireId}")
    public ResponseEntity<?> retournerLivre(@PathVariable Long exemplaireId) {
        empruntService.enregistrerRetour(exemplaireId);
        return ResponseEntity.ok(Map.of("message", "Retour enregistré avec succès"));
    }

    @GetMapping("/etudiant/{etudiantId}/en-cours")
    public ResponseEntity<List<EmpruntDTO>> getEmpruntsEnCours(@PathVariable Long etudiantId) {
        List<Emprunt> emprunts = empruntService.getEmpruntsEnCours(etudiantId);
        List<EmpruntDTO> dtos = emprunts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/etudiant/{etudiantId}/historique")
    public ResponseEntity<List<EmpruntDTO>> getHistorique(@PathVariable Long etudiantId) {
        List<Emprunt> emprunts = empruntService.getHistoriqueEmprunts(etudiantId);
        List<EmpruntDTO> dtos = emprunts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/prolonger")
    public ResponseEntity<?> prolongerEmprunt(@PathVariable Long id) {
        Emprunt emprunt = empruntService.prolongerEmprunt(id);
        return ResponseEntity.ok(toDTO(emprunt));
    }

    @GetMapping("/verifier/{etudiantId}")
    public ResponseEntity<?> peutEmprunter(@PathVariable Long etudiantId) {
        boolean peutEmprunter = empruntService.peutEmprunter(etudiantId);
        return ResponseEntity.ok(Map.of("peutEmprunter", peutEmprunter));
    }

    @PostMapping("/retour/code/{codeBarres}")
    public ResponseEntity<?> retournerLivreParCode(@PathVariable String codeBarres) {
        Exemplaire exemplaire = exemplaireRepository.findByCodeBarres(codeBarres)
            .orElseThrow(() -> new RuntimeException("Aucun exemplaire trouvé avec ce code-barres"));
        
        // Chercher d'abord EN_COURS, sinon RETOURNE, sinon erreur
        Emprunt emprunt = empruntRepository
            .findByExemplaireAndStatut(exemplaire, StatutEmprunt.EN_COURS)
            .orElseGet(() -> empruntRepository
                .findByExemplaireAndStatut(exemplaire, StatutEmprunt.RETOURNE)
                .orElse(null));
        
        if (emprunt == null) {
            // Si pas d'emprunt, on remet juste l'exemplaire en disponible
            exemplaire.setStatut(StatutExemplaire.DISPONIBLE);
            exemplaireRepository.save(exemplaire);
            return ResponseEntity.ok(Map.of("message", "Exemplaire remis en disponible"));
        }
        
        // Si l'emprunt est déjà RETOURNE, on remet juste l'exemplaire en disponible
        if (emprunt.getStatut() == StatutEmprunt.RETOURNE) {
            exemplaire.setStatut(StatutExemplaire.DISPONIBLE);
            exemplaireRepository.save(exemplaire);
            return ResponseEntity.ok(Map.of("message", "Exemplaire remis en disponible (déjà retourné)"));
        }
        
        empruntService.enregistrerRetour(exemplaire.getId());
        return ResponseEntity.ok(Map.of("message", "Retour enregistré avec succès"));
    }

    @GetMapping("/tous/en-cours")
    public ResponseEntity<List<EmpruntDTO>> getTousEmpruntsEnCours() {
        List<Emprunt> emprunts = empruntRepository.findByStatut(StatutEmprunt.EN_COURS);
        List<EmpruntDTO> dtos = emprunts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/tous/demandes")
    public ResponseEntity<List<EmpruntDTO>> getToutesDemandes() {
        List<Emprunt> emprunts = empruntRepository.findByStatut(StatutEmprunt.DEMANDE);
        List<EmpruntDTO> dtos = emprunts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private EmpruntDTO toDTO(Emprunt emprunt) {
        EmpruntDTO dto = new EmpruntDTO();
        dto.setId(emprunt.getId());
        dto.setNomEtudiant(emprunt.getEtudiant().getNomComplet());
        dto.setMatriculeEtudiant(emprunt.getEtudiant().getMatricule());
        dto.setTitreOuvrage(emprunt.getExemplaire().getOuvrage().getTitre());
        dto.setCodeBarresExemplaire(emprunt.getExemplaire().getCodeBarres());
        dto.setRayon(emprunt.getExemplaire().getRayon());
        dto.setDateDebut(emprunt.getDateDebut());
        dto.setDateRetourPrevue(emprunt.getDateRetourPrevue());
        dto.setDateRetourEffective(emprunt.getDateRetourEffective());
        dto.setStatut(emprunt.getStatut().name());
        dto.setRetard(emprunt.calculerRetard());
        return dto;
    }
}