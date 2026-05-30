package com.bibliotheque.event;

import com.bibliotheque.entity.Etudiant;
import com.bibliotheque.entity.Exemplaire;
import com.bibliotheque.entity.Ouvrage;
import org.springframework.context.ApplicationEvent;

public class RetourEffectueEvent extends ApplicationEvent {
    
    private final Exemplaire exemplaire;
    private final Etudiant etudiant;
    private final Ouvrage ouvrage;
    
    public RetourEffectueEvent(Object source, Exemplaire exemplaire, 
                                Etudiant etudiant, Ouvrage ouvrage) {
        super(source);
        this.exemplaire = exemplaire;
        this.etudiant = etudiant;
        this.ouvrage = ouvrage;
    }
    
    public Exemplaire getExemplaire() { return exemplaire; }
    public Etudiant getEtudiant() { return etudiant; }
    public Ouvrage getOuvrage() { return ouvrage; }
}