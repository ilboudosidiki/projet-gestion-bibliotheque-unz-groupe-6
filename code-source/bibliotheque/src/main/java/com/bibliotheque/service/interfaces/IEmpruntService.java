package com.bibliotheque.service.interfaces;

import com.bibliotheque.entity.Emprunt;
import java.util.List;

public interface IEmpruntService {
    Emprunt creerEmprunt(Long etudiantId, Long exemplaireId);
    Emprunt validerEmprunt(Long empruntId);
    void enregistrerRetour(Long exemplaireId);
    List<Emprunt> getEmpruntsEnCours(Long etudiantId);
    List<Emprunt> getHistoriqueEmprunts(Long etudiantId);
    Emprunt prolongerEmprunt(Long empruntId);
    boolean peutEmprunter(Long etudiantId);
}