package com.bibliotheque.service.interfaces;

import com.bibliotheque.entity.Emprunt;
import com.bibliotheque.entity.Penalite;
import java.util.List;

public interface IPenaliteService {
    Penalite creerPenalite(Emprunt emprunt, int joursRetard);
    Penalite appliquerPenalite(Emprunt emprunt, int joursRetard);
    List<Penalite> getPenalitesActives(Long etudiantId);
    void leverSuspension(Long etudiantId, String motif);
    boolean estSuspendu(Long etudiantId);
}