package com.bibliotheque.repository;

import com.bibliotheque.entity.Etudiant;
import com.bibliotheque.entity.Penalite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PenaliteRepository extends JpaRepository<Penalite, Long> {
    
    List<Penalite> findByEtudiant(Etudiant etudiant);
    
    List<Penalite> findByEtudiantAndDateFinAfter(Etudiant etudiant, java.time.LocalDate date);
}