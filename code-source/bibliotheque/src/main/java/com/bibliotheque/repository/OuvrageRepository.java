package com.bibliotheque.repository;

import com.bibliotheque.entity.Ouvrage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OuvrageRepository extends JpaRepository<Ouvrage, Long> {
    
    List<Ouvrage> findByTitreContainingIgnoreCase(String titre);
    
    List<Ouvrage> findByAuteurContainingIgnoreCase(String auteur);
    
    Optional<Ouvrage> findByIsbn(String isbn);
    
    @Query("SELECT o FROM Ouvrage o WHERE " +
           "LOWER(o.titre) LIKE LOWER(CONCAT('%', :motCle, '%')) OR " +
           "LOWER(o.auteur) LIKE LOWER(CONCAT('%', :motCle, '%')) OR " +
           "LOWER(o.motsCles) LIKE LOWER(CONCAT('%', :motCle, '%'))")
    List<Ouvrage> rechercheParMotCle(@Param("motCle") String motCle);
}