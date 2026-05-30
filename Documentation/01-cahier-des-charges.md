# 📋 CAHIER DES CHARGES

## Application de Gestion de Bibliothèque Universitaire
### Université Norbert Zongo

---

## 1. INTRODUCTION

### 1.1 Contexte du projet

L'Université Norbert Zongo souhaite moderniser sa bibliothèque universitaire en développant une application web complète de gestion. Actuellement, la gestion des ressources documentaires, des emprunts et des réservations repose sur des processus manuels ou semi-automatisés, entraînant des lenteurs, des erreurs et une expérience utilisateur insatisfaisante.

Ce projet s'inscrit dans une démarche de transformation numérique visant à :
- Automatiser les processus métiers de la bibliothèque
- Améliorer l'accès aux ressources documentaires
- Optimiser la gestion des emprunts et des retours
- Renforcer le suivi et les statistiques

### 1.2 Objectifs

L'objectif principal est de **concevoir, modéliser, développer et tester une application logicielle professionnelle** en appliquant l'ensemble des concepts du Génie Logiciel.

Objectifs spécifiques :
- Permettre aux étudiants de consulter le catalogue, rechercher, emprunter et réserver des ouvrages
- Permettre aux bibliothécaires de gérer efficacement les emprunts, retours et le catalogue
- Permettre aux administrateurs de gérer les utilisateurs et générer des rapports statistiques
- Assurer une communication automatique via des notifications (email)
- Offrir une interface responsive accessible sur ordinateur et mobile

### 1.3 Périmètre du système

| Inclus | Exclus |
|--------|--------|
| Gestion du catalogue (ouvrages, exemplaires, thèmes) | Paiement en ligne des amendes |
| Gestion des emprunts et retours | Gestion financière de la bibliothèque |
| Gestion des réservations avec file d'attente FIFO | Numérisation des ouvrages |
| Gestion des pénalités de retard | Module de suggestion d'achats |
| Notifications automatiques par email | Intégration avec d'autres universités |
| Génération de rapports et statistiques | Application mobile native |
| Gestion des utilisateurs (3 profils) | Authentification via réseaux sociaux |

---

## 2. DESCRIPTION DES ACTEURS

### 2.1 Visiteur

| Attribut | Description |
|----------|-------------|
| **Définition** | Utilisateur non authentifié accédant à l'application |
| **Fonctionnalités** | Recherche simple et avancée, consultation du catalogue, inscription |
| **Contraintes** | Ne peut ni emprunter ni réserver |

### 2.2 Étudiant

| Attribut | Description |
|----------|-------------|
| **Définition** | Utilisateur authentifié avec le rôle "ÉTUDIANT" |
| **Fonctionnalités** | Emprunt (max 3), réservation, prolongation, consultation historique, réception de notifications |
| **Contraintes** | Compte suspendu en cas de retard, 48h pour récupérer une réservation |

### 2.3 Bibliothécaire

| Attribut | Description |
|----------|-------------|
| **Définition** | Utilisateur authentifié avec le rôle "BIBLIOTHECAIRE" |
| **Fonctionnalités** | Validation des emprunts/retours, gestion du catalogue (CRUD), déclenchement des pénalités |
| **Contraintes** | Ne peut pas gérer les comptes utilisateurs ni les configurations système |

### 2.4 Administrateur

| Attribut | Description |
|----------|-------------|
| **Définition** | Utilisateur authentifié avec le rôle "ADMINISTRATEUR" |
| **Fonctionnalités** | Gestion des utilisateurs, rapports statistiques, configuration système, levée manuelle de suspension |
| **Contraintes** | Accès complet au système (super-utilisateur) |

---

## 3. EXIGENCES FONCTIONNELLES

### 3.1 Gestion du catalogue

| ID | Exigence | Priorité |
|----|----------|----------|
| EF01 | L'application doit permettre l'ajout, la modification et la suppression d'ouvrages | Haute |
| EF02 | L'application doit permettre l'ajout, la modification et la suppression d'exemplaires | Haute |
| EF03 | L'application doit permettre la gestion des thèmes et rayons | Moyenne |
| EF04 | Chaque exemplaire doit avoir un code-barres unique et un statut (Disponible, Emprunté, Réservé) | Haute |

### 3.2 Recherche d'ouvrages

| ID | Exigence | Priorité |
|----|----------|----------|
| EF05 | Une barre de recherche simple (mots-clés) doit être accessible sans authentification | Haute |
| EF06 | La recherche simple doit porter sur le titre, l'auteur et le thème | Haute |
| EF07 | Une recherche avancée doit permettre de filtrer par titre exact, auteur, ISBN, thème | Moyenne |
| EF08 | Les résultats doivent afficher la disponibilité et l'emplacement (rayon) de chaque ouvrage | Haute |

### 3.3 Gestion des emprunts

| ID | Exigence | Priorité |
|----|----------|----------|
| EF09 | Un étudiant peut emprunter jusqu'à 3 ouvrages simultanément | Haute |
| EF10 | La durée d'emprunt standard est de 14 jours | Haute |
| EF11 | L'étudiant peut prolonger un emprunt une seule fois de 7 jours (si pas de réservation) | Moyenne |
| EF12 | Le bibliothécaire doit valider physiquement la sortie et le retour des ouvrages | Haute |
| EF13 | Lors du retour, le système calcule automatiquement les éventuels retards | Haute |

### 3.4 Gestion des réservations

| ID | Exigence | Priorité |
|----|----------|----------|
| EF14 | Le bouton "Réserver" apparaît uniquement si le stock disponible = 0 | Haute |
| EF15 | La file d'attente suit l'ordre chronologique (FIFO) | Haute |
| EF16 | L'étudiant a 48h pour récupérer un ouvrage réservé, sinon la réservation est annulée | Moyenne |
| EF17 | L'étudiant ne peut pas réserver un ouvrage qu'il a déjà en sa possession | Haute |
| EF18 | L'étudiant ne peut pas être inscrit plusieurs fois dans la même file d'attente | Haute |

### 3.5 Gestion des pénalités

| ID | Exigence | Priorité |
|----|----------|----------|
| EF19 | 1 jour de retard = 1 jour de suspension des droits d'emprunt et de réservation | Haute |
| EF20 | La suspension est appliquée automatiquement lors du retour tardif | Haute |
| EF21 | La suspension est levée automatiquement à la date de fin | Haute |
| EF22 | L'administrateur peut lever manuellement une suspension avec motif | Basse |

### 3.6 Notifications

| ID | Exigence | Priorité |
|----|----------|----------|
| EF23 | Un email de rappel est envoyé 2 jours avant la date limite de retour | Moyenne |
| EF24 | Un email est envoyé lorsqu'un ouvrage réservé devient disponible | Haute |
| EF25 | Un email est envoyé lors du blocage du compte pour retard | Haute |

### 3.7 Gestion des utilisateurs

| ID | Exigence | Priorité |
|----|----------|----------|
| EF26 | Un visiteur peut s'inscrire en créant un compte étudiant | Haute |
| EF27 | L'authentification se fait par email et mot de passe | Haute |
| EF28 | L'administrateur peut créer, modifier et désactiver des comptes | Moyenne |

### 3.8 Rapports et statistiques

| ID | Exigence | Priorité |
|----|----------|----------|
| EF29 | Génération de rapports sur les ouvrages les plus empruntés | Moyenne |
| EF30 | Génération de rapports sur le taux de retard par période | Moyenne |
| EF31 | Statistiques sur le nombre d'emprunts par thème | Moyenne |

---

## 4. USERS STORIES

### 4.1 Catalogue et Recherche

| ID | En tant que... | Je veux... | Afin de... | Priorité |
|----|----------------|------------|------------|----------|
| US01 | Visiteur | Rechercher par mots-clés (titre, auteur, thème) | Trouver un document rapidement sans connexion | Haute |
| US02 | Étudiant | Effectuer une recherche avancée avec filtres (titre exact, auteur, ISBN, thème) | Affiner mes recherches académiques | Moyenne |
| US03 | Visiteur | Voir le statut (Disponible/Indisponible) et le rayon | Savoir si je peux obtenir le livre et où | Haute |

### 4.2 Emprunts

| ID | En tant que... | Je veux... | Afin de... | Priorité |
|----|----------------|------------|------------|----------|
| US04 | Étudiant | Emprunter un livre disponible (max 3, 14 jours) | Accéder aux ressources pour mes études | Haute |
| US05 | Étudiant | Prolonger un emprunt de 7 jours si éligible | Garder le livre plus longtemps | Moyenne |
| US06 | Étudiant | Consulter mon historique et mes emprunts en cours | Suivre mes activités | Haute |
| US07 | Bibliothécaire | Valider les emprunts et les retours physiques | Assurer le suivi documentaire | Haute |

### 4.3 Réservations

| ID | En tant que... | Je veux... | Afin de... | Priorité |
|----|----------------|------------|------------|----------|
| US08 | Étudiant | Réserver un ouvrage indisponible (file FIFO) | Obtenir le livre dès son retour | Haute |
| US09 | Étudiant | Être notifié quand ma réservation est disponible | Venir le récupérer rapidement | Haute |
| US10 | Système | Annuler les réservations non récupérées sous 48h | Optimiser la rotation des livres | Moyenne |

### 4.4 Pénalités

| ID | En tant que... | Je veux... | Afin de... | Priorité |
|----|----------------|------------|------------|----------|
| US11 | Bibliothécaire | Valider le retour et déclencher le calcul automatique | Appliquer les règles sans erreur | Haute |
| US12 | Système | Bloquer automatiquement les droits (1j retard = 1j suspension) | Faire respecter les délais | Haute |
| US13 | Étudiant | Recevoir un rappel 2 jours avant l'échéance | Éviter les pénalités | Moyenne |

### 4.5 Utilisateurs

| ID | En tant que... | Je veux... | Afin de... | Priorité |
|----|----------------|------------|------------|----------|
| US14 | Visiteur | Créer un compte étudiant | Accéder aux fonctionnalités | Haute |
| US15 | Utilisateur | M'authentifier (email + mot de passe) | Accéder à mon espace sécurisé | Haute |
| US16 | Administrateur | Gérer les comptes utilisateurs | Contrôler les accès | Moyenne |

### 4.6 Administration

| ID | En tant que... | Je veux... | Afin de... | Priorité |
|----|----------------|------------|------------|----------|
| US17 | Bibliothécaire | Gérer le catalogue (CRUD ouvrages) | Maintenir le catalogue à jour | Haute |
| US18 | Administrateur | Générer des rapports statistiques | Analyser l'utilisation | Moyenne |
| US19 | Administrateur | Consulter les tableaux de bord | Piloter la bibliothèque | Moyenne |
| US20 | Système | Envoyer automatiquement les notifications email | Informer les utilisateurs | Haute |

**Total : 20 Users Stories**

---

## 5. CRITÈRES D'ACCEPTATION (extraits)

### US04 - Emprunter un ouvrage

| CA | Description |
|----|-------------|
| CA1 | Le système bloque l'emprunt si l'étudiant a déjà 3 emprunts en cours. Message : "Limite maximale de 3 emprunts atteinte" |
| CA2 | Le système bloque l'emprunt si le compte est suspendu. Message avec date de fin de suspension |
| CA3 | Date de retour calculée automatiquement à J+14 |
| CA4 | Stock disponible diminue de 1. Si stock = 0, statut passe à "Indisponible" |

### US07 - Valider emprunts et retours

| CA | Description |
|----|-------------|
| CA1 | Le bibliothécaire scanne/saisit l'ID exemplaire et l'ID étudiant. Statut passe de "Demandé" à "En cours" |
| CA2 | Lors du retour, la date effective est enregistrée et le statut passe à "Retourné" |
| CA3 | Si retard : `Jours de suspension = Date retour - Date limite` |
| CA4 | Si pas de réservation : stock +1, statut "Disponible". Si réservation : statut "Réservé" |

### US08 - Réserver un ouvrage

| CA | Description |
|----|-------------|
| CA1 | Bouton "Réserver" visible uniquement si stock = 0 |
| CA2 | Interdiction si déjà en possession de l'ouvrage ou déjà dans la file |
| CA3 | Réservation horodatée, position FIFO affichée (ex: "Position 2/4") |
| CA4 | Au retour, si file non vide : statut "Réservé - En attente", notification au 1er de la file |

---

## 6. EXIGENCES NON FONCTIONNELLES

### 6.1 Performance

| ID | Exigence |
|----|----------|
| ENF01 | Temps de réponse < 2 secondes pour les opérations courantes (recherche, emprunt, retour) |
| ENF02 | L'application doit supporter jusqu'à 100 utilisateurs simultanés |

### 6.2 Sécurité

| ID | Exigence |
|----|----------|
| ENF03 | Authentification obligatoire pour les actions d'emprunt, réservation et gestion |
| ENF04 | Mots de passe hashés (BCrypt) en base de données |
| ENF05 | Protection des données personnelles (email, nom, prénom) |
| ENF06 | Gestion des rôles : VISITEUR, ÉTUDIANT, BIBLIOTHECAIRE, ADMINISTRATEUR |

### 6.3 Ergonomie et Interface

| ID | Exigence |
|----|----------|
| ENF07 | Interface responsive (accessible sur ordinateur et mobile) |
| ENF08 | Barre de recherche visible dès la page d'accueil |
| ENF09 | Messages d'erreur explicites pour l'utilisateur |

### 6.4 Disponibilité et Fiabilité

| ID | Exigence |
|----|----------|
| ENF10 | Disponibilité ≥ 99% |
| ENF11 | Sauvegarde régulière de la base de données |

### 6.5 Qualité du Code

| ID | Exigence |
|----|----------|
| ENF12 | Respect des principes SOLID |
| ENF13 | Utilisation d'au moins 4 Design Patterns |
| ENF14 | Code versionné avec Git |
| ENF15 | Couverture de tests ≥ 65% |

### 6.6 Contraintes Techniques

| ID | Exigence |
|----|----------|
| ENF16 | Backend : Java avec Spring Boot |
| ENF17 | Base de données : MySQL |
| ENF18 | Versionnement : Git + GitHub |
| ENF19 | Hébergement : Render (ou équivalent gratuit) |

---

## 7. GLOSSAIRE

| Terme | Définition |
|-------|------------|
| **Ouvrage** | Concept général d'un livre (titre, ISBN, auteur) |
| **Exemplaire** | Objet physique unique identifié par un code-barres |
| **FIFO** | First In, First Out : premier arrivé, premier servi |
| **Suspension** | Blocage temporaire des droits d'emprunt et de réservation |
| **Pénalité** | Sanction appliquée en cas de retard (1 jour de retard = 1 jour de suspension) |
| **File d'attente** | Liste ordonnée chronologiquement des réservations pour un ouvrage |

---

