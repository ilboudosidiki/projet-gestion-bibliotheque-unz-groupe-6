package com.bibliotheque.controller;

import com.bibliotheque.dto.AdminRegisterRequest;
import com.bibliotheque.dto.LoginRequest;
import com.bibliotheque.dto.RegisterRequest;
import com.bibliotheque.entity.Administrateur;
import com.bibliotheque.entity.Bibliothecaire;
import com.bibliotheque.entity.Etudiant;
import com.bibliotheque.entity.Utilisateur;
import com.bibliotheque.enums.RoleUtilisateur;
import com.bibliotheque.repository.EtudiantRepository;
import com.bibliotheque.repository.UtilisateurRepository;
import com.bibliotheque.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.bibliotheque.dto.AdminRegisterRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final EtudiantRepository etudiantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UtilisateurRepository utilisateurRepository,
                          EtudiantRepository etudiantRepository,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.utilisateurRepository = utilisateurRepository;
        this.etudiantRepository = etudiantRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Cet email est déjà utilisé"));
        }

        if (etudiantRepository.existsByMatricule(request.getMatricule())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Ce matricule est déjà utilisé"));
        }

        Etudiant etudiant = new Etudiant();
        etudiant.setNom(request.getNom());
        etudiant.setPrenom(request.getPrenom());
        etudiant.setEmail(request.getEmail());
        etudiant.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        etudiant.setMatricule(request.getMatricule());
        etudiant.setRole(RoleUtilisateur.ETUDIANT);
        etudiant.setEstActif(true);

        etudiantRepository.save(etudiant);

        return ResponseEntity.ok(Map.of("message", "Compte créé avec succès"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Optional<Utilisateur> utilisateurOpt = 
                utilisateurRepository.findByEmail(request.getEmail());

        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email ou mot de passe incorrect"));
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email ou mot de passe incorrect"));
        }

        if (!utilisateur.isEstActif()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Compte désactivé"));
        }

        String token = jwtTokenProvider.genererToken(
                utilisateur.getEmail(),
                utilisateur.getRole().name());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("email", utilisateur.getEmail());
        response.put("nom", utilisateur.getNomComplet());
        response.put("role", utilisateur.getRole().name());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin-register")
    public ResponseEntity<?> adminRegister(@Valid @RequestBody AdminRegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Cet email est déjà utilisé"));
        }

        Administrateur admin = new Administrateur();
        admin.setNom(request.getNom());
        admin.setPrenom(request.getPrenom());
        admin.setEmail(request.getEmail());
        admin.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        admin.setRole(RoleUtilisateur.ADMINISTRATEUR);
        admin.setNiveauAcces("SUPER_ADMIN");
        admin.setEstActif(true);

        utilisateurRepository.save(admin);

        return ResponseEntity.ok(Map.of("message", "Compte administrateur créé avec succès"));
    }

    @PostMapping("/biblio-register")
    public ResponseEntity<?> biblioRegister(@Valid @RequestBody AdminRegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Cet email est déjà utilisé"));
        }

        Bibliothecaire biblio = new Bibliothecaire();
        biblio.setNom(request.getNom());
        biblio.setPrenom(request.getPrenom());
        biblio.setEmail(request.getEmail());
        biblio.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        biblio.setRole(RoleUtilisateur.BIBLIOTHECAIRE);
        biblio.setNumeroEmploye("BIB" + System.currentTimeMillis());
        biblio.setEstActif(true);

        utilisateurRepository.save(biblio);

        return ResponseEntity.ok(Map.of("message", "Compte bibliothécaire créé avec succès"));
    }
}