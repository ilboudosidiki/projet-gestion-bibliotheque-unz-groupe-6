# FICHES DESCRIPTIVES DES CAS D'UTILISATION
## Application de Gestion de Bibliothèque Universitaire

---

## UC04 - Emprunter un ouvrage

| Champ | Description |
|-------|-------------|
| **ID** | UC04 |
| **Acteur(s)** | Étudiant, Bibliothécaire |
| **Préconditions** | L'étudiant est authentifié, le livre est disponible (stock > 0) |
| **Déclencheur** | L'étudiant clique sur "Emprunter" pour un ouvrage disponible |
| **Scénario nominal** | 1. L'étudiant sélectionne un livre disponible<br>2. Le système vérifie le quota (< 3 emprunts en cours)<br>3. Le système vérifie l'absence de suspension<br>4. Le système crée une demande d'emprunt<br>5. Le bibliothécaire valide la sortie physique<br>6. Le système calcule la date de retour (J+14)<br>7. Le stock disponible diminue de 1 |
| **Exceptions** | 2a. Quota atteint → Message "Limite maximale de 3 emprunts atteinte"<br>3a. Compte suspendu → Message avec date de fin de suspension |
| **Postconditions** | Emprunt enregistré, stock mis à jour |

---

## UC07 - Valider emprunts et retours

| Champ | Description |
|-------|-------------|
| **ID** | UC07 |
| **Acteur(s)** | Bibliothécaire |
| **Préconditions** | Le bibliothécaire est authentifié, une demande d'emprunt existe (pour validation) ou un emprunt est en cours (pour retour) |
| **Déclencheur** | Le bibliothécaire scanne le code-barres du livre et l'ID étudiant |
| **Scénario nominal** | 1. Le bibliothécaire accède au module de validation<br>2. Il saisit/scanne l'ID du livre et l'ID de l'étudiant<br>3. Le système passe le statut de l'emprunt à "En cours" (ou "Retourné" pour un retour)<br>4. Pour un retour : le système calcule l'éventuel retard<br>5. Si pas de réservation → Stock +1, statut "Disponible"<br>6. Si réservation existe → Stock reste à 0, statut "Réservé" |
| **Exceptions** | 3a. Livre non trouvé → Message d'erreur<br>3b. Étudiant non trouvé → Message d'erreur<br>4a. Retard détecté → Calcul automatique des jours de suspension |
| **Postconditions** | Emprunt validé ou retour enregistré, stock et statuts mis à jour |

---

## UC08 - Réserver un ouvrage indisponible

| Champ | Description |
|-------|-------------|
| **ID** | UC08 |
| **Acteur(s)** | Étudiant |
| **Préconditions** | L'étudiant est authentifié, l'ouvrage est indisponible (stock = 0) |
| **Déclencheur** | L'étudiant clique sur "Réserver" |
| **Scénario nominal** | 1. L'étudiant consulte un ouvrage indisponible<br>2. Le bouton "Réserver" est visible<br>3. L'étudiant clique sur "Réserver"<br>4. Le système vérifie que l'étudiant n'a pas déjà ce livre en prêt<br>5. Le système vérifie que l'étudiant n'est pas déjà dans la file<br>6. La réservation est horodatée et ajoutée en fin de file (FIFO)<br>7. La position dans la file est affichée<br>8. L'étudiant reçoit un email de confirmation avec la position dans la file |
| **Scénario alternatif (Récupération)** | 1. Quand le livre se libère et l'étudiant est 1er dans la file<br>2. Le statut du livre passe à "PRÊT_À_RÉCUPÉRER"<br>3. L'étudiant reçoit une notification email "Votre réservation est prête - 48h pour récupérer"<br>4. Un minuteur de 48h démarre<br>5. Si l'étudiant vient récupérer : réservation → COMPLÉTÉE, livre emprunté<br>6. Si 48h expirent : réservation → EXPIRED, notification envoyée, livre passe au 2e de la file |
| **Scénario alternatif (Expiration auto)** | 1. Un job planifié vérifie chaque heure les réservations "PRÊT_À_RÉCUPÉRER"<br>2. Si 48h ont écoulé depuis l'horodatage : statut → "EXPIRED"<br>3. Notification email envoyée : "Réservation expirée - le livre a été proposé au suivant"<br>4. Le système bascule automatiquement au 2e de la file s'il existe |
| **Exceptions** | 4a. Déjà en prêt → Message d'erreur<br>5a. Déjà dans la file → Message "Vous avez déjà réservé cet ouvrage"<br>2a. Stock > 0 → Le bouton "Emprunter" remplace "Réserver"<br>6a. Erreur envoi email → Notification stockée, retry chaque heure |
| **Postconditions** | Réservation enregistrée dans la file d'attente avec horodatage, position visible, email de confirmation envoyé |

---

## UC11 - Gérer les pénalités (Calcul automatique)

| Champ | Description |
|-------|-------------|
| **ID** | UC11 |
| **Acteur(s)** | Bibliothécaire (Déclencheur indirect), Système (Automatique) |
| **Préconditions** | Un emprunt est enregistré avec le statut "En cours" et la date limite est dépassée |
| **Déclencheur** | Le bibliothécaire valide le retour tardif d'un ouvrage (via UC07) |
| **Scénario nominal** | 1. Le système récupère la date effective de retour et la date limite<br>2. Le système calcule le nombre de jours de retard<br>3. Le système applique la formule : `Jours de suspension = Date retour - Date limite`<br>4. Le système associe cette pénalité au profil de l'étudiant<br>5. Le système inclut automatiquement le blocage des droits (<<include>> UC12) |
| **Exceptions** | 2a. Date de retour ≤ date limite → Le cas d'utilisation prend fin sans pénalité |
| **Postconditions** | La pénalité est calculée, enregistrée dans l'historique, et le statut passe à "En suspension" |

---

## UC12 - Bloquer/Débloquer compte étudiant

| Champ | Description |
|-------|-------------|
| **ID** | UC12 |
| **Acteur(s)** | Administrateur (Manuel), Système (Automatique) |
| **Préconditions** | Le compte de l'étudiant existe et est actif |
| **Déclencheur** | **Blocage** : Inclusion par UC11 suite à un retard<br>**Déblocage** : Expiration de la suspension OU action manuelle de l'Administrateur |
| **Scénario nominal (Blocage)** | 1. Le système reçoit le signal de suspension (de UC11) avec la durée<br>2. Le système modifie l'état du compte de "Actif" à "Suspendu"<br>3. Le système enregistre la date de fin de suspension<br>4. L'étudiant reçoit un email l'informant du blocage |
| **Scénario alternatif (Déblocage automatique)** | 1. Le script quotidien vérifie les comptes suspendus<br>2. Si date du jour ≥ date de fin → statut repasse à "Actif"<br>3. Les droits d'emprunt et de réservation sont rétablis |
| **Exceptions** | 1a. L'Administrateur lève manuellement la suspension avant la date prévue (avec motif) |
---

## UC17 - Gérer le catalogue (CRUD Ouvrages et Exemplaires)

| Champ | Description |
|-------|-------------|
| **ID** | UC17 |
| **Acteur(s)** | Bibliothécaire, Administrateur |
| **Préconditions** | L'utilisateur est authentifié avec le rôle Bibliothécaire ou Administrateur |
| **Déclencheur** | L'utilisateur accède au module d'administration du catalogue |
| **Scénario nominal (Création)** | 1. Le bibliothécaire saisit les informations de l'ouvrage (titre, auteur, ISBN, thème, édition)<br>2. Le système vérifie l'unicité de l'ISBN<br>3. Le système crée l'ouvrage avec le statut "Disponible"<br>4. Le bibliothécaire ajoute des exemplaires (code-barres, localisation)<br>5. Chaque exemplaire est enregistré et le stock augmente |
| **Scénario alternatif (Modification)** | 1. Le bibliothécaire sélectionne un ouvrage existant<br>2. Il modifie les attributs (titre, auteur, thème, édition)<br>3. Les exemplaires peuvent être modifiés (localisation, code-barres) ou supprimés<br>4. Le stock est recalculé automatiquement |
| **Scénario alternatif (Suppression)** | 1. Le bibliothécaire sélectionne un ouvrage<br>2. Le système vérifie qu'aucun exemplaire n'est emprunté ou réservé<br>3. L'ouvrage et ses exemplaires sont supprimés de la base de données |
| **Exceptions** | 2a. ISBN déjà existant → Message "Cet ISBN existe déjà dans le catalogue"<br>3a. Exemplaires en circulation → Message "Impossible de supprimer un ouvrage avec des exemplaires en circulation" |
| **Postconditions** | Catalogue mis à jour, stocks recalculés, statuts des ouvrages synchronisés |

---

## UC14 - Gérer les Utilisateurs (Administrateur)

| Champ | Description |
|-------|-------------|
| **ID** | UC14 |
| **Acteur(s)** | Administrateur |
| **Préconditions** | L'administrateur est authentifié |
| **Déclencheur** | L'administrateur accède au module de gestion des utilisateurs |
| **Scénario nominal (Création)** | 1. L'administrateur saisit les données d'un nouvel utilisateur (email, nom, prénom, rôle)<br>2. Le système génère un mot de passe temporaire<br>3. L'utilisateur reçoit un email avec ses identifiants et un lien de modification |
| **Scénario alternatif (Modification)** | 1. L'administrateur sélectionne un utilisateur<br>2. Il modifie les informations (email, nom, prénom, rôle)<br>3. Les modifications sont enregistrées et un email de confirmation est envoyé |
| **Scénario alternatif (Désactivation)** | 1. L'administrateur sélectionne un utilisateur<br>2. Il désactive le compte (statut "Inactif") sans suppression<br>3. L'utilisateur ne peut plus se connecter |
| **Exceptions** | 2a. Email déjà utilisé → Message "Cet email est déjà associé à un compte"<br>3a. Utilisateur avec emprunts en cours → Message "Impossible de désactiver un utilisateur avec des emprunts actifs" |
| **Postconditions** | Liste des utilisateurs mise à jour, notifications email envoyées |

---

## UC09 - Envoyer les Notifications (Système)

| Champ | Description |
|-------|-------------|
| **ID** | UC09 |
| **Acteur(s)** | Système (Automatique) |
| **Préconditions** | Des événements ont été déclenché (retour d'ouvrage réservé, rappel avant échéance, blocage de compte, réservation expirée) |
| **Déclencheur** | Publication d'un événement applicatif (emprunt validé, réservation devenant disponible, retard détecté, etc.) |
| **Scénario nominal** | 1. Le système intercepte l'événement<br>2. Il récupère l'email de l'étudiant concerné<br>3. Il construit le contenu du message selon le type d'événement<br>4. Le message est envoyé via SMTP<br>5. Un log est enregistré (succès ou échec) |
| **Scénario alternatif (Rappel avant échéance)** | 1. Un job programmé s'exécute quotidiennement à minuit<br>2. Il cherche les emprunts expirant dans 2 jours<br>3. Les emails de rappel sont envoyés aux étudiants concernés |
| **Exceptions** | 4a. Serveur SMTP indisponible → Message en queue, retry ultérieur<br>2a. Email invalide → Notification dans les logs, pas d'envoi |
| **Postconditions** | Email envoyé (ou en attente de retry), événement logué |

---