# USERS STORIES - Application de Gestion de Bibliothèque Universitaire
## Université Norbert Zongo

---

## Catalogue et Recherche

### US01 - Recherche simple par mots-clés

| Champ | Valeur |
|-------|--------|
| **ID** | US01 |
| **En tant que** | Visiteur |
| **Je veux** | Rechercher un ouvrage par mots-clés (titre, auteur, thème) depuis une barre de recherche unique |
| **Afin de** | Trouver rapidement un document sans avoir à me connecter |
| **Priorité** | Haute |

---

### US02 - Recherche avancée avec filtres

| Champ | Valeur |
|-------|--------|
| **ID** | US02 |
| **En tant que** | Étudiant |
| **Je veux** | Effectuer une recherche avancée avec filtres (titre exact, auteur, ISBN, thème) |
| **Afin de** | Affiner mes résultats pour des recherches académiques précises |
| **Priorité** | Moyenne |

---

### US03 - Voir statut et emplacement

| Champ | Valeur |
|-------|--------|
| **ID** | US03 |
| **En tant que** | Visiteur/Étudiant |
| **Je veux** | Voir le statut d'un ouvrage (Disponible/Indisponible) et son emplacement (rayon) |
| **Afin de** | Savoir immédiatement si je peux obtenir le livre et où le trouver |
| **Priorité** | Haute |

---

## Emprunts

### US04 - Emprunter un livre disponible

| Champ | Valeur |
|-------|--------|
| **ID** | US04 |
| **En tant que** | Étudiant |
| **Je veux** | Emprunter un livre disponible (dans la limite de 3 emprunts simultanés) pour 14 jours |
| **Afin de** | Accéder aux ressources documentaires nécessaires à mes études |
| **Priorité** | Haute |

---

### US05 - Prolonger un emprunt

| Champ | Valeur |
|-------|--------|
| **ID** | US05 |
| **En tant que** | Étudiant |
| **Je veux** | Prolonger un emprunt de 7 jours supplémentaires depuis mon espace personnel |
| **Afin de** | Garder le livre plus longtemps si j'en ai besoin et qu'il n'est pas réservé |
| **Priorité** | Moyenne |

---

### US06 - Consulter historique d'emprunts

| Champ | Valeur |
|-------|--------|
| **ID** | US06 |
| **En tant que** | Étudiant |
| **Je veux** | Consulter mon historique d'emprunts et mes emprunts en cours |
| **Afin de** | Suivre mes activités et connaître mes dates de retour |
| **Priorité** | Haute |

---

### US07 - Valider emprunts et retours (Bibliothécaire)

| Champ | Valeur |
|-------|--------|
| **ID** | US07 |
| **En tant que** | Bibliothécaire |
| **Je veux** | Valider les emprunts et les retours des ouvrages |
| **Afin de** | Assurer le suivi physique des documents |
| **Priorité** | Haute |

---

## Réservations

### US08 - Réserver un ouvrage indisponible (FIFO)

| Champ | Valeur |
|-------|--------|
| **ID** | US08 |
| **En tant que** | Étudiant |
| **Je veux** | Réserver un ouvrage indisponible et être placé dans une file d'attente FIFO |
| **Afin de** | Obtenir le livre dès qu'il sera retourné |
| **Priorité** | Haute |

---

### US09 - Être notifié de la disponibilité d'une réservation

| Champ | Valeur |
|-------|--------|
| **ID** | US09 |
| **En tant que** | Étudiant |
| **Je veux** | Être notifié par email quand un livre réservé est disponible |
| **Afin de** | Venir le récupérer rapidement |
| **Priorité** | Haute |

---

### US10 - Annulation automatique des réservations expirées

| Champ | Valeur |
|-------|--------|
| **ID** | US10 |
| **En tant que** | Système |
| **Je veux** | Annuler automatiquement une réservation non récupérée sous 48h et passer au suivant dans la file |
| **Afin de** | Optimiser la rotation des livres |
| **Priorité** | Moyenne |

---

## Pénalités

### US11 - Calcul automatique des pénalités

| Champ | Valeur |
|-------|--------|
| **ID** | US11 |
| **En tant que** | Bibliothécaire |
| **Je veux** | Valider le retour d'un livre et déclencher le calcul automatique des pénalités |
| **Afin de** | Appliquer les règles de retard sans erreur manuelle |
| **Priorité** | Haute |

---

### US12 - Blocage automatique des droits

| Champ | Valeur |
|-------|--------|
| **ID** | US12 |
| **En tant que** | Système |
| **Je veux** | Bloquer automatiquement les droits d'emprunt/réservation d'un étudiant en retard (1 jour de blocage par jour de retard) |
| **Afin de** | Faire respecter les délais de retour |
| **Priorité** | Haute |

---

### US13 - Recevoir un rappel avant échéance

| Champ | Valeur |
|-------|--------|
| **ID** | US13 |
| **En tant que** | Étudiant |
| **Je veux** | Recevoir un email de rappel 2 jours avant la date limite de retour |
| **Afin de** | Éviter les pénalités de retard |
| **Priorité** | Moyenne |

---

## Utilisateurs

### US14 - Créer un compte (inscription)

| Champ | Valeur |
|-------|--------|
| **ID** | US14 |
| **En tant que** | Visiteur |
| **Je veux** | Créer un compte étudiant (inscription) |
| **Afin de** | Accéder aux fonctionnalités de la bibliothèque |
| **Priorité** | Haute |

---

### US15 - S'authentifier

| Champ | Valeur |
|-------|--------|
| **ID** | US15 |
| **En tant que** | Utilisateur |
| **Je veux** | M'authentifier avec email et mot de passe |
| **Afin de** | Accéder à mon espace personnel sécurisé |
| **Priorité** | Haute |

---

### US16 - Gérer les comptes utilisateurs

| Champ | Valeur |
|-------|--------|
| **ID** | US16 |
| **En tant que** | Administrateur |
| **Je veux** | Gérer les comptes utilisateurs (création, modification, désactivation) |
| **Afin de** | Contrôler l'accès au système |
| **Priorité** | Moyenne |

---

## Administration

### US17 - Gérer le catalogue (CRUD)

| Champ | Valeur |
|-------|--------|
| **ID** | US17 |
| **En tant que** | Bibliothécaire |
| **Je veux** | Ajouter, modifier, supprimer des ouvrages dans le catalogue |
| **Afin de** | Maintenir le catalogue à jour |
| **Priorité** | Haute |

---

### US18 - Générer des rapports

| Champ | Valeur |
|-------|--------|
| **ID** | US18 |
| **En tant que** | Administrateur |
| **Je veux** | Générer des rapports sur les ouvrages les plus empruntés, les retards, etc. |
| **Afin de** | Analyser l'utilisation de la bibliothèque |
| **Priorité** | Moyenne |

---

### US19 - Consulter les statistiques

| Champ | Valeur |
|-------|--------|
| **ID** | US19 |
| **En tant que** | Administrateur |
| **Je veux** | Consulter des statistiques globales (taux d'emprunt, taux de retard) |
| **Afin de** | Prendre des décisions pour améliorer le service |
| **Priorité** | Moyenne |

---

## Notifications

### US20 - Envoi automatique de notifications

| Champ | Valeur |
|-------|--------|
| **ID** | US20 |
| **En tant que** | Système |
| **Je veux** | Envoyer automatiquement des emails de notification (rappel, disponibilité réservation, suspension) |
| **Afin de** | Tenir les utilisateurs informés |
| **Priorité** | Haute |

---

## 📊 Matrice des priorités

| Priorité | Users Stories | Nombre |
|----------|---------------|--------|
| **Haute** | US01, US03, US04, US06, US07, US08, US09, US11, US12, US14, US15, US17, US20 | 13 |
| **Moyenne** | US02, US05, US10, US13, US16, US18, US19 | 7 |
| **Basse** | - | 0 |

**Total : 20 Users Stories**