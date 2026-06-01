# GESTION AGILE SCRUM
## Application de Gestion de Bibliothèque Universitaire

---

## 1. Équipe Scrum

| Rôle | Membre | Responsabilité |
|------|--------|----------------|
| Scrum Master | Sidiki ILBOUDO | Facilite les cérémonies, élimine les obstacles |
| Product Owner | Ines GAIGO | Gère le Product Backlog, priorise les Users Stories |
| Développeur Backend | Pierre DIONOU | Spring Boot, API REST, sécurité JWT |
| Développeuse Backend | Amira SAVADOGO | Spring Data JPA, base de données PostgreSQL |
| Développeuse Frontend | Epiphanie SOMPOUGDOU | Interface Thymeleaf/Bootstrap, responsive design |
| Développeur Tests | Zakaria DERRA | Tests unitaires, intégration, couverture >= 65% |

---

## 2. Product Backlog

| ID | User Story | Priorité | Story Points | Sprint |
|----|------------|----------|-------------|--------|
| US15 | S'authentifier | Haute | 5 | Sprint 1 |
| US14 | Créer un compte | Haute | 5 | Sprint 1 |
| US17 | CRUD ouvrages | Haute | 8 | Sprint 1 |
| US01 | Recherche simple | Haute | 8 | Sprint 1 |
| US03 | Statut et emplacement | Haute | 3 | Sprint 1 |
| US04 | Emprunter un livre | Haute | 13 | Sprint 2 |
| US07 | Valider emprunts/retours | Haute | 8 | Sprint 2 |
| US06 | Historique emprunts | Haute | 5 | Sprint 2 |
| US11 | Calcul pénalités | Haute | 8 | Sprint 2 |
| US08 | Réserver (FIFO) | Haute | 13 | Sprint 3 |
| US09 | Notification disponibilité | Haute | 5 | Sprint 3 |
| US12 | Blocage automatique | Haute | 5 | Sprint 3 |
| US20 | Notifications email | Haute | 8 | Sprint 3 |
| US02 | Recherche avancée | Moyenne | 5 | Sprint 3 |
| US05 | Prolonger emprunt | Moyenne | 5 | Sprint 4 |
| US10 | Annulation auto réservations | Moyenne | 5 | Sprint 4 |
| US13 | Rappel avant échéance | Moyenne | 3 | Sprint 4 |
| US16 | Gérer comptes (admin) | Moyenne | 5 | Sprint 4 |
| US18 | Rapports statistiques | Moyenne | 8 | Sprint 4 |
| US19 | Tableaux de bord | Moyenne | 5 | Sprint 4 |

Total : 20 Users Stories

---

## 3. Planification des Sprints

### Sprint 1 - Fondations (Semaine 1)

Objectif : Authentification et catalogue de base

| ID | User Story | Story Points |
|----|------------|-------------|
| US15 | S'authentifier | 5 |
| US14 | Créer un compte | 5 |
| US17 | CRUD ouvrages | 8 |
| US01 | Recherche simple | 8 |
| US03 | Statut et emplacement | 3 |

Total Sprint 1 : 29 Story Points

---

### Sprint 2 - Coeur de métier (Semaine 2)

Objectif : Emprunts, retours et pénalités

| ID | User Story | Story Points |
|----|------------|-------------|
| US04 | Emprunter un livre | 13 |
| US07 | Valider emprunts/retours | 8 |
| US06 | Historique emprunts | 5 |
| US11 | Calcul pénalités | 8 |

Total Sprint 2 : 34 Story Points

---

### Sprint 3 - Réservations et notifications (Semaine 3)

Objectif : Gestion des files d'attente

| ID | User Story | Story Points |
|----|------------|-------------|
| US08 | Réserver (FIFO) | 13 |
| US09 | Notification disponibilité | 5 |
| US12 | Blocage automatique | 5 |
| US20 | Notifications email | 8 |
| US02 | Recherche avancée | 5 |

Total Sprint 3 : 36 Story Points

---

### Sprint 4 - Finalisation (Semaine 4)

Objectif : Rapports, administration et finitions

| ID | User Story | Story Points |
|----|------------|-------------|
| US05 | Prolonger emprunt | 5 |
| US10 | Annulation auto réservations | 5 |
| US13 | Rappel avant échéance | 3 |
| US16 | Gérer comptes (admin) | 5 |
| US18 | Rapports statistiques | 8 |
| US19 | Tableaux de bord | 5 |

Total Sprint 4 : 31 Story Points

---

## 4. Cérémonies Scrum

| Cérémonie | Fréquence | Durée | Participants |
|-----------|-----------|-------|-------------|
| Daily Scrum | Quotidien | 15 min | Équipe de développement |
| Sprint Planning | Début de sprint | 2h | Toute l'équipe |
| Sprint Review | Fin de sprint | 1h | Toute l'équipe + Product Owner |
| Sprint Retrospective | Fin de sprint | 1h | Équipe de développement |

---

## 5. Definition of Done (DoD)

Une User Story est terminée quand :

- Code développé et commenté
- Tests unitaires passés (couverture >= 65%)
- Tests d'intégration passés
- Revue de code effectuée
- Documentation API mise à jour (Swagger)
- Branch mergée dans main
- Déployée sur l'environnement de test

---

## 6. Outils Utilisés

| Outil | Usage |
|-------|-------|
| GitHub | Versionnement du code source |
| GitHub Projects | Gestion du Product Backlog et Sprints |
| VS Code | Environnement de développement |
| Swagger | Documentation API |
| JUnit + Mockito | Tests unitaires et intégration |

---

## 7. Suivi de Projet (Daily Scrum)

### Sprint 1 - Daily Scrum

| Jour | Membre | Travail effectué | Blocages | Résolution |
|------|--------|-----------------|----------|-----------|
| Lundi | Équipe | Sprint Planning - Sélection des 5 US, estimation story points | Aucun | N/A |
| Mardi | Pierre (Backend) | Implémentation authentification JWT + structure Entity Utilisateur | Confusion sur JWT Bearer vs Basic - documentation consultée | Clearance validée |
| Mardi | Sidiki (BD) | Création schéma MySQL, tables Utilisateur, Ouvrage, Exemplaire | Normalisation ISBN vs code-barres | Design revu |
| Mardi | Ines (Frontend) | Layout page de connexion avec Thymeleaf | Aucun | Avance nominale |
| Mercredi | Pierre (Backend) | Finition authentication, création UserDetailsService | Intégration BCrypt - dépendance manquante | Maven pom.xml mis à jour |
| Mercredi | Sidiki (BD) | Tests données de test (10 ouvrages, 3 utilisateurs) | Aucun | 100% couverture OK |
| Mercredi | Ines (Frontend) | Intégration page d'accueil et barre de recherche simple | CSS responsive sur mobile | Bootstrap grid appliquée |
| Jeudi | Pierre (Backend) | Implémentation endpoint GET /api/ouvrages + filtrage titre/auteur | Performance lente sur 1000 résultats | Ajout pagination (page=0&size=20) |
| Jeudi | Sidiki (BD) | Indexation des colonnes titre, auteur, ISBN | Aucun | Index créés sur 3 colonnes |
| Jeudi | Ines (Frontend) | Tests d'intégrité des recherches avec backend | Encodage UTF-8 sur accents (é, è) | Headers Content-Type corrigés |
| Vendredi | Équipe | Sprint Review - démo des 5 US terminées (4/4 en cours + 1 prêt) | Aucun | Rétrospective: améliorer estimation, 2 US en retard |

---

### Sprint 2 - Daily Scrum

| Jour | Membre | Travail effectué | Blocages | Résolution |
|------|--------|-----------------|----------|-----------|
| Lundi | Équipe | Sprint Planning - Sélection US04, US07, US06, US11 (34 story points) | Aucun | N/A |
| Mardi | Pierre (Backend) | Implémentation EmpruntService + logique vérification quota | Relation @ManyToOne entre Etudiant et Emprunt complexe | Diagramme ER redessiné, implémentation réussie |
| Mardi | Sidiki (BD) | Table Emprunt avec statuts enum (DEMANDÉ, EN_COURS, RETOURNÉ) | Aucun | Migrations Flyway créées |
| Mardi | Ines (Frontend) | Écran de sélection d'ouvrage + bouton "Emprunter" | Aucun | Design conforme maquette |
| Mercredi | Pierre (Backend) | Finition gestion du retour, calcul pénalités (US11) | Format date ISO 8601 vs DATE MySQL | Utilisation java.time.LocalDate |
| Mercredi | Sidiki (BD) | Table Penalite avec calcul jours_suspension | Index sur date_retour_effective pour les requêtes | Perfomance requête 50ms ✓ |
| Mercredi | Ines (Frontend) | Création page "Historique emprunts" + statuts | Aucun | Tests d'affichage OK |
| Jeudi | Pierre (Backend) | Tests unitaires EmpruntService (15 cas) | Mockito + données de test | Couverture 92% atteinte |
| Jeudi | Sidiki (BD) | Validation données - cohérence stock vs exemplaires | Quelques doublocons détectés | Correction base et script de nettoyage |
| Jeudi | Ines (Frontend) | Tests d'intégration API emprunt avec Postman | Aucun | API documentée dans Swagger |
| Vendredi | Équipe | Sprint Review - 4/4 US complètes | Aucun | Rétrospective: améliorer communication backend-frontend |

---

### Sprint 3 - Daily Scrum

| Jour | Membre | Travail effectué | Blocages | Résolution |
|------|--------|-----------------|----------|-----------|
| Lundi | Équipe | Sprint Planning - Sélection US08 (FIFO), US09, US12, US20, US02 (36 story points) | Aucun | N/A |
| Mardi | Pierre (Backend) | Implémentation ReservationService + gestion file FIFO | Asynchronie pour notifications email | Pattern Spring Events + @EventListener décidé |
| Mardi | Sidiki (BD) | Table Reservation avec position_dans_file + horodatage | Aucun | Clé étrangère vers Ouvrage OK |
| Mardi | Ines (Frontend) | Écran affichage réservations + position dans file | Refresh en temps réel ? | Polling 10s implémenté |
| Mercredi | Pierre (Backend) | Implémentation NotificationService + sender email SMTP | Serveur SMTP : port 587 vs 465, TLS vs SSL | Configuration Java Mail finaliseé |
| Mercredi | Sidiki (BD) | Trigger BD pour expiration auto après 48h | Complexity trigger MySQL 5.7 | Script planifié chaque heure (less overhead) |
| Mercredi | Ines (Frontend) | Notifications toast pour réservations disponibles | Aucun | Bootstrap toast implémenté |
| Jeudi | Pierre (Backend) | Tests bout-en-bout : réservation → disponibilité → notification | Timing des événements asynchrones complexe | Thread.sleep() en test acceptable |
| Jeudi | Sidiki (BD) | Script de nettoyage des réservations expirées (cron) | Aucun | Planification OK |
| Jeudi | Ines (Frontend) | Intégration notifications email dans profil étudiant | Aucun | Affichage email reçus OK |
| Vendredi | Équipe | Sprint Review - 5/5 US complètes (dont réservation FIFO!) | Aucun | Rétrospective: gestion de l'asynchrone très bien maîtrisée |

---

### Sprint 4 - Daily Scrum

| Jour | Membre | Travail effectué | Blocages | Résolution |
|------|--------|-----------------|----------|-----------|
| Lundi | Équipe | Sprint Planning - Sélection US05, US10, US13, US16, US18, US19 (31 story points) | Aucun | N/A |
| Mardi | Pierre (Backend) | Implémentation prolongation emprunt (US05) + conditions | Vérification "pas de réservation" complexe | Requête JPA corrigée |
| Mardi | Sidiki (BD) | Table RappelEcheance pour les jobs planifiés | Aucun | Quartz Scheduler intégré |
| Mardi | Ines (Frontend) | Dashboard administrateur : gestion utilisateurs (US16) | Aucun | Tableau avec actions CRUD |
| Mercredi | Pierre (Backend) | Implémentation rapports statistiques (US18, US19) | Requêtes complexes GROUP BY, COUNT, agrégations | Native SQL + @Query finalisées, perf ~200ms |
| Mercredi | Sidiki (BD) | Vues BD pour les rapports (ouvrages les plus empruntés, taux de retard) | Aucun | 3 vues créées, indexées |
| Mercredi | Ines (Frontend) | Tableaux de bord statistiques (charts avec Chart.js) | Aucun | Graphs animés OK |
| Jeudi | Pierre (Backend) | Tests complets couverture >= 65% | Mocking de EmailService complexe | PowerMock utilisé avec succès |
| Jeudi | Sidiki (BD) | Scripts de migration + données de test massives (1000 ouvrages) | Aucun | Performance migration 30s ✓ |
| Jeudi | Ines (Frontend) | Tests E2E Selenium sur flux critiques | Timing flakiness sur assertions | Waits explicites ajoutés |
| Vendredi | Équipe | Sprint Review FINAL - 6/6 US + travail de rétrospection + doc API complète | Aucun | Projet livré, déploiement Render effectué |