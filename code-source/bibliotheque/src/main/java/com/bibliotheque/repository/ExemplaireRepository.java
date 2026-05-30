package com.bibliotheque.repository;

import com.bibliotheque.entity.Exemplaire;
import com.bibliotheque.entity.Ouvrage;
import com.bibliotheque.enums.StatutExemplaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {
    
    List<Exemplaire> findByOuvrage(Ouvrage ouvrage);
    
    List<Exemplaire> findByOuvrageAndStatut(Ouvrage ouvrage, StatutExemplaire statut);
    
    long countByOuvrageAndStatut(Ouvrage ouvrage, StatutExemplaire statut);

    Optional<Exemplaire> findByCodeBarres(String codeBarres);
}