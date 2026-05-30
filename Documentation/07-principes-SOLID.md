# PRINCIPES SOLID - Application
## Application de Gestion de Bibliothèque Universitaire

---

Le projet respecte les **5 principes SOLID** de conception orientée objet, 
comme exigé dans le cahier des charges.

---

## S - Single Responsibility Principle (Responsabilité Unique)

**Définition :** Une classe ne doit avoir qu'une seule raison de changer.

| Application dans le projet | Détail |
|---------------------------|--------|
| EmpruntService | Gère uniquement la logique métier des emprunts |
| PenaliteService | Gère uniquement le calcul et l'application des pénalités |
| ReservationService | Gère uniquement les files d'attente et réservations |
| NotificationService | Gère uniquement l'envoi des emails |

**Bénéfice :** Code plus lisible, plus facile à tester et à maintenir.

Exemple :
@Service
public class EmpruntServiceImpl implements IEmpruntService {
    // UNIQUEMENT des méthodes liées aux emprunts
    public Emprunt creerEmprunt(Long etudiantId, Long exemplaireId) { ... }
    public void enregistrerRetour(Long exemplaireId) { ... }
    public List<Emprunt> getEmpruntsEnCours(Long etudiantId) { ... }
    
    // NE GÈRE PAS les notifications (délégué à NotificationService)
    // NE GÈRE PAS les pénalités (délégué à PenaliteService)
}

---

## O - Open/Closed Principle (Ouvert/Fermé)

**Définition :** Une classe doit être ouverte à l'extension, 
mais fermée à la modification.

| Application dans le projet | Détail |
|---------------------------|--------|
| Interfaces de service | IEmpruntService permet d'ajouter une nouvelle implémentation sans toucher au code existant |
| Événements Spring | Ajout d'un nouveau récepteur (@EventListener) sans modifier l'émetteur |
| Strategy via Spring | Possibilité d'injecter différentes implémentations selon le profil |

**Bénéfice :** Ajout de fonctionnalités sans risque de régression.

Exemple :
// Interface fermée à la modification
public interface IEmpruntService {
    Emprunt creerEmprunt(Long etudiantId, Long exemplaireId);
    void enregistrerRetour(Long exemplaireId);
}

// Ouvert à l'extension : nouvelle implémentation possible
@Service("empruntServiceV2")
public class EmpruntServiceImplV2 implements IEmpruntService {
    // Nouvelle logique sans toucher à l'ancienne
}

---

## L - Liskov Substitution Principle (Substitution de Liskov)

**Définition :** Les classes filles doivent pouvoir remplacer 
les classes mères sans altérer le comportement du programme.

| Application dans le projet | Détail |
|---------------------------|--------|
| Héritage Utilisateur | Etudiant, Bibliothecaire, Administrateur héritent tous d'Utilisateur |
| Méthode peutEmprunter() | Retourne `false` par défaut pour tous les utilisateurs; Etudiant override avec logique métier |
| Spring Security | UserDetailsServiceImpl charge n'importe quel sous-type d'Utilisateur de façon sûre |
| Pas d'exception levée | `peutEmprunter()` retourne un boolean sans casser le contrat, même pour les non-emprunteurs |

**Bénéfice :** Polymorphisme fonctionnel fiable, pas de rupture de contrat, comportement prévisible.

**Correction apportée :** La méthode `peutEmprunter()` retourne simplement `false` pour les profils 
non-emprunteurs (Bibliothecaire, Administrateur) au lieu de lever une exception. Cela respecte 
strictement le principe LSP car le contrat (retourner un booléen) est toujours honoré.

Exemple corrigé :
```java
// Classe mère : implémentation par défaut
public abstract class Utilisateur {
    // Retourne false par défaut (non-emprunteur)
    public boolean peutEmprunter() {
        return false;
    }
}

// Étudiant : override avec logique métier
public class Etudiant extends Utilisateur {
    @Override
    public boolean peutEmprunter() {
        return !this.isSuspendu() && this.getEmpruntsCours().size() < 3;
    }
}

// Bibliothécaire : utilise l'implémentation mère (retourne false)
public class Bibliothecaire extends Utilisateur {
    // Pas d'override : peutEmprunter() retourne false
}

// Administrateur : utilise l'implémentation mère (retourne false)
public class Administrateur extends Utilisateur {
    // Pas d'override : peutEmprunter() retourne false
}

// Code client utilisant peutEmprunter()
public void traiterEmprunt(Utilisateur utilisateur, Exemplaire exemplaire) {
    if (utilisateur.peutEmprunter()) {
        // Fonctionne de manière sûre avec tous les types d'Utilisateur
        // Les non-emprunteurs retournent simplement false
    }
}
```

---

## I - Interface Segregation Principle (Ségrégation des Interfaces)

**Définition :** Les interfaces doivent être fines et spécifiques. 
Un client ne doit pas dépendre de méthodes qu'il n'utilise pas.

| Application dans le projet | Détail |
|---------------------------|--------|
| IEmpruntService | Uniquement les méthodes d'emprunt |
| IReservationService | Uniquement les méthodes de réservation |
| INotificationService | Uniquement les méthodes d'envoi de notifications |
| IPenaliteService | Uniquement les méthodes de gestion des pénalités |

**Bénéfice :** Pas de dépendances inutiles, mock facile dans les tests.

Mauvaise pratique (NE PAS FAIRE) :
public interface IBibliothequeService {
    void emprunter();
    void reserver();
    void notifier();
    void penaliser();
    void genererRapport();
    // Trop grosse interface, force à implémenter des méthodes inutiles
}

Bonne pratique (FAIRE) :
public interface IEmpruntService {
    Emprunt creerEmprunt(Long etudiantId, Long exemplaireId);
    void enregistrerRetour(Long exemplaireId);
    // Uniquement ce qui concerne les emprunts
}

public interface IReservationService {
    Reservation creerReservation(Long etudiantId, Long ouvrageId);
    void annulerReservation(Long reservationId);
    // Uniquement ce qui concerne les réservations
}

---

## D - Dependency Inversion Principle (Inversion des Dépendances)

**Définition :** Les modules de haut niveau ne doivent pas dépendre 
des modules de bas niveau. Tous deux doivent dépendre d'abstractions.

| Application dans le projet | Détail |
|---------------------------|--------|
| Injection par constructeur | Les contrôleurs dépendent des interfaces, pas des implémentations |
| Conteneur Spring IoC | Gère l'injection automatique des dépendances |
| Qualifiers | Permettent de choisir l'implémentation sans couplage fort |

**Bénéfice :** Faible couplage, changement d'implémentation facilité.

Exemple :
@RestController
public class EmpruntController {
    
    private final IEmpruntService empruntService;  // Interface, pas implémentation
    
    // Injection par constructeur (recommandée par Spring)
    public EmpruntController(IEmpruntService empruntService) {
        this.empruntService = empruntService;
    }
    
    @PostMapping("/emprunts")
    public ResponseEntity<EmpruntDTO> emprunter(@RequestBody EmpruntRequest request) {
        // Le contrôleur ne sait pas quelle implémentation est utilisée
        Emprunt emprunt = empruntService.creerEmprunt(
            request.getEtudiantId(), 
            request.getExemplaireId()
        );
        return ResponseEntity.ok(mapper.toDTO(emprunt));
    }
}

// Si on change d'implémentation, le contrôleur n'est PAS modifié
@Service("empruntServiceMongoDB")
public class EmpruntServiceMongoImpl implements IEmpruntService {
    // Nouvelle implémentation avec MongoDB
}

---

## Récapitulatif SOLID

| Principe | Appliqué dans | Bénéfice principal |
|----------|---------------|-------------------|
| S - Responsabilité Unique | Services spécialisés | Code lisible, testable, maintenable |
| O - Ouvert/Fermé | Interfaces + Spring Events | Extensions sans régressions |
| L - Substitution de Liskov | Héritage Utilisateur | Polymorphisme fiable |
| I - Ségrégation Interfaces | Interfaces fines par domaine | Pas de dépendances inutiles |
| D - Inversion Dépendances | Injection par constructeur | Faible couplage, flexibilité |

---

## Validation de l'exigence

Exigence du projet : Respect des principes SOLID
Statut : Les 5 principes sont appliqués et documentés
Bénéfice global : Architecture robuste, évolutive et testable

---

## Application combinée SOLID + Design Patterns

| Design Pattern | Principe SOLID renforcé |
|----------------|------------------------|
| Repository | S (Responsabilité Unique) + D (Inversion) |
| DTO | S (Séparation responsabilités) |
| Singleton | D (Inversion via conteneur IoC) |
| Observer | O (Ouvert/Fermé) + S (Découplage) |
| Template Method | L (Substitution) + O (Extension) |
| Factory Method | S (Création isolée) + O (Extension) |