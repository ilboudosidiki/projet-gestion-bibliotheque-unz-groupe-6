package com.bibliotheque.controller;

import com.bibliotheque.dto.ReservationDTO;
import com.bibliotheque.entity.Reservation;
import com.bibliotheque.repository.ReservationRepository;
import com.bibliotheque.service.interfaces.IReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final IReservationService reservationService;
    private final ReservationRepository reservationRepository;

    public ReservationController(IReservationService reservationService,
                                ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }

    @PostMapping
    public ResponseEntity<?> reserver(@RequestBody Map<String, Long> request) {
        Long etudiantId = request.get("etudiantId");
        Long ouvrageId = request.get("ouvrageId");

        Reservation reservation = reservationService.creerReservation(etudiantId, ouvrageId);
        return ResponseEntity.ok(toDTO(reservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> annulerReservation(@PathVariable Long id) {
        reservationService.annulerReservation(id);
        return ResponseEntity.ok(Map.of("message", "Réservation annulée"));
    }

    @GetMapping("/ouvrage/{ouvrageId}/file-attente")
    public ResponseEntity<List<ReservationDTO>> getFileAttente(@PathVariable Long ouvrageId) {
        List<Reservation> file = reservationService.getFileAttente(ouvrageId);
        List<ReservationDTO> dtos = file.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/position")
    public ResponseEntity<?> getPosition(@RequestParam Long etudiantId, 
                                          @RequestParam Long ouvrageId) {
        int position = reservationService.getPositionDansFile(etudiantId, ouvrageId);
        return ResponseEntity.ok(Map.of("position", position));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservation(@PathVariable Long id) {
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        return ResponseEntity.ok(toDTO(reservation));
    }

    @GetMapping("/toutes")
    public ResponseEntity<List<ReservationDTO>> getToutesReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationDTO> dtos = reservations.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private ReservationDTO toDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setNomEtudiant(reservation.getEtudiant().getNomComplet());
        dto.setMatriculeEtudiant(reservation.getEtudiant().getMatricule());
        dto.setTitreOuvrage(reservation.getOuvrage().getTitre());
        dto.setDateReservation(reservation.getDateReservation());
        dto.setDateExpiration(reservation.getDateExpiration());
        dto.setPositionFile(reservation.getPositionFile());
        dto.setStatut(reservation.getStatut().name());
        return dto;
    }
}