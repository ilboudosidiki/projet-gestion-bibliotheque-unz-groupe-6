# DESIGN PATTERNS UTILISÉS
## Application de Gestion de Bibliothèque Universitaire

---

Le projet respecte l'exigence d'utiliser **au moins 4 Design Patterns**. 
Nous en avons identifié et implémenté **6** dans notre architecture Spring Boot.

---

## Pattern 1 : Repository

| Propriété | Valeur |
|-----------|--------|
| **Catégorie** | Accès aux données |
| **Implémentation** | Interfaces Spring Data JPA (JpaRepository) |
| **Où ?** | Package repository/ |
| **Justification** | Abstraction complète de la couche persistance. Permet de changer de SGBD sans modifier le code métier. |

Extrait de code :
@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
    List<Emprunt> findByEtudiantAndStatut(Etudiant etudiant, StatutEmprunt statut);
    Optional<Emprunt> findByExemplaireAndStatut(Exemplaire exemplaire, StatutEmprunt statut);
}

---

## Pattern 2 : DTO (Data Transfer Object)

| Propriété | Valeur |
|-----------|--------|
| **Catégorie** | Transfert de données |
| **Implémentation** | Classes Java dédiées aux échanges API REST |
| **Où ?** | Package dto/ |
| **Justification** | Séparation stricte entre entités JPA et objets exposés par l'API. Évite les problèmes de Lazy Loading et protège les données sensibles. |

Extrait de code :
public class EmpruntDTO {
    private Long id;
    private String nomEtudiant;
    private String matriculeEtudiant;
    private String titreOuvrage;
    private String codeBarresExemplaire;
    private LocalDate dateRetourPrevue;
    private String statut;
}

---

## Pattern 3 : Singleton

| Propriété | Valeur |
|-----------|--------|
| **Catégorie** | Création |
| **Implémentation** | Beans Spring gérés par le conteneur IoC |
| **Où ?** | Tous les @Service, @Repository, @Controller |
| **Justification** | Le conteneur Spring gère une instance unique de chaque bean. Cohérence, économie mémoire. |

Extrait de code :
@Service
public class EmpruntServiceImpl implements IEmpruntService {
    private final EmpruntRepository empruntRepository;
    
    public EmpruntServiceImpl(EmpruntRepository empruntRepository) {
        this.empruntRepository = empruntRepository;
    }
}

---

## Pattern 4 : Observer (Spring Events)

| Propriété | Valeur |
|-----------|--------|
| **Catégorie** | Comportement |
| **Implémentation** | ApplicationEventPublisher + @EventListener |
| **Où ?** | Entre EmpruntServiceImpl et NotificationService |
| **Justification** | Découplage total entre services. Un événement est publié lors d'un retour, les services intéressés réagissent sans être appelés explicitement. |

Extrait de code - Événement :
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
}

Extrait de code - Émetteur :
@Service
public class EmpruntServiceImpl implements IEmpruntService {
    private final ApplicationEventPublisher eventPublisher;
    
    public void enregistrerRetour(Long exemplaireId) {
        // Logique de retour...
        eventPublisher.publishEvent(new RetourEffectueEvent(
            this, exemplaire, emprunt.getEtudiant(), exemplaire.getOuvrage()
        ));
    }
}

Extrait de code - Récepteur :
@Component
public class NotificationService {
    
    @EventListener
    @Async
    public void handleRetourEffectue(RetourEffectueEvent event) {
        // Vérifier les réservations et notifier
    }
}

---

## Pattern 5 : Template Method (corrigé pour LSP)

| Propriété | Valeur |
|-----------|--------|
| **Catégorie** | Comportement |
| **Implémentation** | Méthode concrète avec polymorphisme dans la classe mère Utilisateur |
| **Où ?** | entity/Utilisateur.java (concrète), entity/Etudiant.java (override) |
| **Justification** | La classe abstraite définit le squelette, les sous-classes peuvent surcharger sans casser le contrat. Pour les profils non-emprunteurs (Bibliothecaire, Administrateur), la méthode retourne simplement `false` sans exception. |

**Correction LSP :** La méthode `peutEmprunter()` ne doit JAMAIS lever une exception comme `UnsupportedOperationException` car cela violerait le principe de Liskov. À la place, elle retourne un booléen cohérent avec la sémantique de chaque rôle.

Extrait de code - Classe mère :
```java
public abstract class Utilisateur {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    
    // Template Method : retourne false par défaut (non-emprunteur)
    public boolean peutEmprunter() {
        return false;  // Par défaut, les utilisateurs ne peuvent pas emprunter
    }
}
```

Extrait de code - Étudiant (override) :
```java
public class Etudiant extends Utilisateur {
    private Set<Emprunt> emprunts;
    private boolean suspendu;
    
    @Override
    public boolean peutEmprunter() {
        return !suspendu && emprunts.size() < 3;  // Conditions métier
    }
}
```

Extrait de code - Bibliothécaire (héritage de base) :
```java
public class Bibliothecaire extends Utilisateur {
    // Aucun override : utilise l'implémentation par défaut
    // peutEmprunter() retourne false
}
```

Extrait de code - Administrateur (héritage de base) :
```java
public class Administrateur extends Utilisateur {
    // Aucun override : utilise l'implémentation par défaut
    // peutEmprunter() retourne false
}
```

**Avantage LSP :** Le code client peut appeler `utilisateur.peutEmprunter()` sur n'importe quel Utilisateur sans risque de rupture.

public class Etudiant extends Utilisateur {
    private String matricule;
    private LocalDate dateFinSuspension;
    
    @Override
    public boolean peutEmprunter() {
        return getNbEmpruntsEnCours() < 3 && !estSuspendu();
    }
    
    public boolean estSuspendu() {
        return dateFinSuspension != null && 
               dateFinSuspension.isAfter(LocalDate.now());
    }
}

public class Bibliothecaire extends Utilisateur {
    @Override
    public boolean peutEmprunter() {
        return false; // Le bibliothécaire n'emprunte pas
    }
}

---

## Pattern 6 : Factory Method

| Propriété | Valeur |
|-----------|--------|
| **Catégorie** | Création |
| **Implémentation** | Méthode creerPenalite() dans PenaliteServiceImpl |
| **Où ?** | service/impl/PenaliteServiceImpl.java |
| **Justification** | Encapsule la création complexe d'une pénalité avec calculs automatiques (date de fin, motif). |

Extrait de code :
@Service
public class PenaliteServiceImpl implements IPenaliteService {
    
    public Penalite creerPenalite(Emprunt emprunt, int joursRetard) {
        if (joursRetard <= 0) {
            throw new IllegalArgumentException("Retard invalide");
        }
        
        Penalite penalite = new Penalite();
        penalite.setEmprunt(emprunt);
        penalite.setEtudiant(emprunt.getEtudiant());
        penalite.setDateDebut(LocalDate.now());
        penalite.setDureeJours(joursRetard);
        penalite.setDateFin(LocalDate.now().plusDays(joursRetard));
        penalite.setMotif("Retard de " + joursRetard + " jour(s)");
        
        return penalite;
    }
}

---

## Récapitulatif des Design Patterns

| # | Pattern | Catégorie GoF | Où dans le projet | Bénéfice |
|---|---------|---------------|-------------------|----------|
| 1 | Repository | Accès données | Interfaces JPA | Abstraction persistance |
| 2 | DTO | Transfert | Classes DTO | Séparation API/Base |
| 3 | Singleton | Création | Beans Spring | Instance unique |
| 4 | Observer | Comportement | Spring Events | Découplage services |
| 5 | Template Method | Comportement | Utilisateur.peutEmprunter() | Réutilisation code |
| 6 | Factory Method | Création | PenaliteService.creerPenalite() | Création encapsulée |

---

## Validation de l'exigence

Exigence : Minimum 4 Design Patterns
Réalisé : 6 Design Patterns identifiés et justifiés
Statut : EXIGENCE SATISFAITE