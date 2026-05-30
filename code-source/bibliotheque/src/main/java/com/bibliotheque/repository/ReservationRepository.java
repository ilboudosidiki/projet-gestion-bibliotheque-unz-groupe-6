package com.bibliotheque.repository;

import com.bibliotheque.entity.Etudiant;
import com.bibliotheque.entity.Ouvrage;
import com.bibliotheque.entity.Reservation;
import com.bibliotheque.enums.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    List<Reservation> findByOuvrageAndStatutOrderByDateReservationAsc(
        Ouvrage ouvrage, StatutReservation statut);
    
    Optional<Reservation> findByEtudiantAndOuvrageAndStatut(
        Etudiant etudiant, Ouvrage ouvrage, StatutReservation statut);
    
    boolean existsByEtudiantAndOuvrageAndStatut(
        Etudiant etudiant, Ouvrage ouvrage, StatutReservation statut);
    
    List<Reservation> findByStatutAndDateExpirationBefore(
        StatutReservation statut, java.time.LocalDateTime date);
}