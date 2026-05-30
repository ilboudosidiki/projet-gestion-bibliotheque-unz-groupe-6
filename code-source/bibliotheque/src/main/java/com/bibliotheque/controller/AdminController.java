package com.bibliotheque.controller;

import com.bibliotheque.entity.Utilisateur;
import com.bibliotheque.service.interfaces.IUtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final IUtilisateurService utilisateurService;

    public AdminController(IUtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/utilisateurs")
    public ResponseEntity<List<Utilisateur>> tousLesUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.tousLesUtilisateurs());
    }

    @GetMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable Long id) {
        return utilisateurService.trouverParId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> modifierUtilisateur(@PathVariable Long id,
                                                            @RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(utilisateurService.modifierUtilisateur(id, utilisateur));
    }

    @DeleteMapping("/utilisateurs/{id}")
    public ResponseEntity<?> supprimerUtilisateur(@PathVariable Long id) {
        utilisateurService.supprimerUtilisateur(id);
        return ResponseEntity.ok(Map.of("message", "Utilisateur supprimé"));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStatistiques() {
        long totalUtilisateurs = utilisateurService.tousLesUtilisateurs().size();
        return ResponseEntity.ok(Map.of(
            "totalUtilisateurs", totalUtilisateurs,
            "message", "Statistiques en cours d'implémentation"
        ));
    }
}