package com.bibliotheque.repository;

import com.bibliotheque.entity.Emprunt;
import com.bibliotheque.entity.Etudiant;
import com.bibliotheque.entity.Exemplaire;
import com.bibliotheque.enums.StatutEmprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.bibliotheque.enums.StatutEmprunt;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
    
    List<Emprunt> findByEtudiantAndStatut(Etudiant etudiant, StatutEmprunt statut);
    
    Optional<Emprunt> findByExemplaireAndStatut(Exemplaire exemplaire, StatutEmprunt statut);
    
    @Query("SELECT COUNT(e) FROM Emprunt e WHERE e.etudiant = :etudiant AND e.statut = 'EN_COURS'")
    int countEmpruntsEnCours(@Param("etudiant") Etudiant etudiant);
    
    List<Emprunt> findByEtudiant(Etudiant etudiant);

    List<Emprunt> findByStatut(StatutEmprunt statut);
}