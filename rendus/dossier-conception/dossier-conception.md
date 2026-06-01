# DOSSIER DE CONCEPTION
# Application de Gestion de Bibliothèque Universitaire
## Université Norbert Zongo

---
**Enseignant :** Dr OUEDRAOGO Moïse
**Filière :** Informatique - Licence 3 - Semestre 5
**Matière :** Génie Logiciel
**Date :** Mai 2026
---

## CAHIER DES CHARGES

### 1. Contexte du projet

L'Université Norbert Zongo souhaite moderniser sa bibliothèque universitaire 
en développant une application web complète de gestion. Cette application 
doit permettre de gérer efficacement les ressources documentaires, les emprunts, 
les réservations et les interactions entre les différents acteurs.

### 2. Objectifs

Concevoir, modéliser, développer et tester une application logicielle 
professionnelle en appliquant l'ensemble des concepts du Génie Logiciel.

### 3. Acteurs du système

**Visiteur :** Utilisateur non authentifié. Peut consulter le catalogue 
et effectuer des recherches.

**Étudiant :** Utilisateur authentifié. Peut emprunter (max 3 ouvrages, 14 jours), 
réserver, consulter son historique et recevoir des notifications.

**Bibliothécaire :** Gère le catalogue, valide les emprunts et retours, 
déclenche les pénalités.

**Administrateur :** Gère les utilisateurs, génère les rapports, 
configure le système.

### 4. Exigences fonctionnelles

**Gestion du catalogue :**
- Ajout, modification, suppression d'ouvrages et d'exemplaires
- Gestion des catégories et rayons

**Recherche :**
- Recherche simple par mots-clés (titre, auteur, catégorie)
- Recherche avancée (titre exact, auteur, ISBN, catégorie)
- Affichage du statut (Disponible/Indisponible) et emplacement

**Emprunts :**
- Limite de 3 emprunts simultanés par étudiant
- Durée standard de 14 jours
- Prolongation unique de 7 jours
- Validation physique par le bibliothécaire

**Réservations :**
- File d'attente FIFO quand stock = 0
- Délai de récupération de 48h
- Notification automatique de disponibilité

**Pénalités :**
- 1 jour de retard = 1 jour de suspension
- Application automatique au retour
- Blocage des droits d'emprunt et réservation

**Notifications :**
- Rappel 2 jours avant échéance
- Notification de disponibilité de réservation
- Notification de suspension

**Administration :**
- Gestion des comptes utilisateurs
- Rapports statistiques (ouvrages populaires, taux de retard)

### 5. Exigences non fonctionnelles

- Temps de réponse inférieur à 2 secondes
- Disponibilité supérieure ou égale à 99%
- Interface responsive (ordinateur et mobile)
- Authentification sécurisée JWT
- Hashage des mots de passe (BCrypt)
- Respect des principes SOLID
- Minimum 4 Design Patterns
- Couverture de tests supérieure ou égale à 65%
- Code versionné avec Git
- Déploiement continu via Docker sur Render

---

## USERS STORIES

### Catalogue et Recherche

US01 - En tant que Visiteur, je veux rechercher par mots-clés afin de 
trouver un document rapidement sans connexion. Priorité : Haute

US02 - En tant qu'Étudiant, je veux effectuer une recherche avancée avec 
filtres afin d'affiner mes recherches académiques. Priorité : Moyenne

US03 - En tant que Visiteur, je veux voir le statut et le rayon afin de 
savoir si le livre est disponible. Priorité : Haute

### Emprunts

US04 - En tant qu'Étudiant, je veux emprunter un livre disponible (max 3, 
14 jours) afin d'accéder aux ressources. Priorité : Haute

US05 - En tant qu'Étudiant, je veux prolonger un emprunt de 7 jours afin 
de garder le livre plus longtemps. Priorité : Moyenne

US06 - En tant qu'Étudiant, je veux consulter mon historique afin de 
suivre mes activités. Priorité : Haute

US07 - En tant que Bibliothécaire, je veux valider les emprunts et retours 
afin d'assurer le suivi physique. Priorité : Haute

### Réservations

US08 - En tant qu'Étudiant, je veux réserver un ouvrage indisponible (FIFO) 
afin de l'obtenir dès son retour. Priorité : Haute

US09 - En tant qu'Étudiant, je veux être notifié quand ma réservation est 
disponible. Priorité : Haute

US10 - En tant que Système, je veux annuler les réservations non récupérées 
sous 48h afin d'optimiser la rotation. Priorité : Moyenne

### Pénalités

US11 - En tant que Bibliothécaire, je veux déclencher le calcul automatique 
des pénalités au retour. Priorité : Haute

US12 - En tant que Système, je veux bloquer automatiquement les droits 
(1j retard = 1j suspension). Priorité : Haute

US13 - En tant qu'Étudiant, je veux recevoir un rappel 2 jours avant 
l'échéance. Priorité : Moyenne

### Utilisateurs

US14 - En tant que Visiteur, je veux créer un compte afin d'accéder aux 
fonctionnalités. Priorité : Haute

US15 - En tant qu'Utilisateur, je veux m'authentifier afin d'accéder à mon 
espace sécurisé. Priorité : Haute

US16 - En tant qu'Administrateur, je veux gérer les comptes utilisateurs. 
Priorité : Moyenne

### Administration

US17 - En tant que Bibliothécaire, je veux gérer le catalogue (CRUD). 
Priorité : Haute

US18 - En tant qu'Administrateur, je veux générer des rapports statistiques. 
Priorité : Moyenne

US19 - En tant qu'Administrateur, je veux consulter les tableaux de bord. 
Priorité : Moyenne

US20 - En tant que Système, je veux envoyer automatiquement les notifications 
email. Priorité : Haute

---

## CRITÈRES D'ACCEPTATION

### US04 - Emprunter un livre

CA1 : Blocage si quota de 3 emprunts atteint avec message explicite
CA2 : Blocage si compte suspendu avec date de fin affichée
CA3 : Calcul automatique de la date de retour à J+14
CA4 : Stock diminué de 1, changement statut si stock = 0

### US07 - Valider emprunts et retours

CA1 : Validation par scan du code-barres et ID étudiant
CA2 : Enregistrement de la date effective de retour
CA3 : Calcul automatique du retard si applicable
CA4 : Remise en circulation ou statut Réservé selon file d'attente

### US08 - Réserver un ouvrage

CA1 : Bouton visible uniquement si stock = 0
CA2 : Interdiction si déjà en possession ou déjà dans la file
CA3 : Horodatage et position FIFO affichée
CA4 : Déclenchement notification au retour

---

## DIAGRAMMES DE CONCEPTION

### Diagramme de Cas d'Utilisation

[Insérer l'image : diagrammes/images/01-cas-utilisation.png]

### Diagramme de Classes

[Insérer l'image : diagrammes/images/02-classes.png]

### Diagrammes de Séquence

**Scénario 1 - Emprunter un ouvrage :**
[Insérer l'image : diagrammes/images/03-sequence-emprunt.png]

**Scénario 2 - Retour avec pénalité :**
[Insérer l'image : diagrammes/images/04-sequence-retour.png]

**Scénario 3 - Réservation FIFO :**
[Insérer l'image : diagrammes/images/05-sequence-reservation.png]

---

## CONCEPTION ARCHITECTURALE (VUE 4+1)

### Vue Logique

Le diagramme de classes présente la structure statique avec 3 packages 
principaux : Utilisateurs (héritage), Documents (Ouvrage/Exemplaire), 
Opérations (Emprunt, Reservation, Penalite).

### Vue de Processus

Les diagrammes de séquence montrent les interactions dynamiques pour 
les 3 scénarios critiques du système.

### Vue de Développement

Architecture en 4 couches :
- Présentation : Controllers REST, DTOs
- Métier : Services, Interfaces
- Domaine : Entités JPA, Enums
- Infrastructure : Repositories, Security, Config

[Insérer l'image : diagrammes/images/06-packages-architecture.png]

### Vue Physique

Déploiement sur Render avec Web Service Spring Boot et base PostgreSQL 16.
URL de production : https://bibliotheque-unz.onrender.com

[Insérer l'image : diagrammes/images/07-deploiement.png]

### Vue Scénarios (+1)

Le diagramme de cas d'utilisation présente les 16 cas d'utilisation 
et les relations entre les 4 acteurs.

---

## DESIGN PATTERNS

6 Design Patterns implémentés (minimum exigé : 4) :

1. Repository : Abstraction de la persistance avec Spring Data JPA
2. DTO : Séparation entre entités JPA et objets exposés par l'API
3. Singleton : Beans Spring gérés par le conteneur IoC
4. Observer : Spring Events pour découpler retour et notifications
5. Template Method : Méthode peutEmprunter() abstraite dans Utilisateur
6. Factory Method : Création encapsulée des pénalités

---

## PRINCIPES SOLID

S - Responsabilité Unique : Services spécialisés par domaine
O - Ouvert/Fermé : Interfaces permettant l'extension sans modification
L - Substitution de Liskov : Sous-classes substituables partout
I - Ségrégation des Interfaces : Interfaces fines et spécifiques
D - Inversion des Dépendances : Injection par constructeur

---

## ÉQUIPE

| Membre | Rôle |
|--------|------|
| Sidiki ILBOUDO | Scrum Master |
| Ines GAIGO | Product Owner |
| Pierre DIONOU | Développeur Backend |
| Amira SAVADOGO | Développeuse Backend |
| Epiphanie SOMPOUGDOU | Développeuse Frontend |
| Zakaria DERRA | Développeur Tests |

---

Équipe projet - Université Norbert Zongo - Mai 2026