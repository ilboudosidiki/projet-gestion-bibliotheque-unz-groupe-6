# GLOSSAIRE - Application de Gestion de Bibliothèque Universitaire

| Terme | Définition |
|-------|------------|
| **Ouvrage** | Concept général d'un livre (titre, ISBN, auteur). Un ouvrage peut avoir plusieurs exemplaires physiques |
| **Exemplaire** | Objet physique unique identifié par un code-barres. C'est l'exemplaire qui est emprunté, pas l'ouvrage |
| **FIFO** | First In, First Out : premier arrivé, premier servi. Principe de gestion de la file d'attente des réservations |
| **Suspension** | Blocage temporaire des droits d'emprunt et de réservation d'un étudiant |
| **Pénalité** | Sanction appliquée en cas de retard : 1 jour de retard = 1 jour de suspension |
| **File d'attente** | Liste ordonnée chronologiquement des réservations pour un ouvrage indisponible |
| **CRUD** | Create, Read, Update, Delete : opérations de base sur les données |
| **Statut d'exemplaire** | État actuel : DISPONIBLE, EMPRUNTÉ, RÉSERVÉ, PERDU |
| **Statut d'emprunt** | État de la transaction : DEMANDÉ, EN_COURS, RETOURNÉ, ANNULÉ |
| **Statut de réservation** | État de la réservation : EN_ATTENTE, PRÊTE_À_RÉCUPÉRER, EXPIRÉE, RÉCUPÉRÉE, ANNULÉE |
| **Quota** | Limite de 3 emprunts simultanés par étudiant |
| **Prolongation** | Extension de 7 jours de la durée d'emprunt (une seule fois, si pas de réservation) |
| **Notification** | Email automatique envoyé pour : rappel avant échéance, disponibilité réservation, suspension |
| **SOLID** | Principes de conception orientée objet : Single responsibility, Open-closed, Liskov substitution, Interface segregation, Dependency inversion |
| **Scrum** | Méthodologie Agile de gestion de projet avec sprints, daily meetings, reviews et rétrospectives |