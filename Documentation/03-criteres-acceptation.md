# CRITÈRES D'ACCEPTATION - Application de Gestion de Bibliothèque Universitaire
## Université Norbert Zongo

---

## US04 - Emprunter un livre disponible

| ID | Critère d'Acceptation |
|----|----------------------|
| **CA1** | Le système doit bloquer la demande d'emprunt si l'étudiant possède déjà 3 emprunts en cours. Un message d'erreur explicite doit s'afficher : "Limite maximale de 3 emprunts atteinte" |
| **CA2** | Le système doit bloquer la demande d'emprunt si le compte de l'étudiant est actuellement suspendu pour cause de retard. Un message d'erreur doit indiquer la date de fin de la suspension |
| **CA3** | Lors de la confirmation, le système calcule automatiquement la date limite de retour à J+14 jours par rapport à la date du jour |
| **CA4** | Dès la validation de l'emprunt, le nombre d'exemplaires disponibles de l'ouvrage diminue de 1 dans le catalogue. Si le stock atteint 0, le statut de l'ouvrage passe automatiquement à "Indisponible" |

---

## US07 - Valider les emprunts et les retours (Bibliothécaire)

| ID | Critère d'Acceptation |
|----|----------------------|
| **CA1** | Pour confirmer un emprunt initié par un étudiant, le bibliothécaire doit pouvoir scanner le code-barres (ou saisir l'ID) du livre et l'ID de l'étudiant. Le statut de l'emprunt passe alors de "Demandé" à "En cours" |
| **CA2** | Lors du retour physique, le bibliothécaire saisit l'identifiant de l'exemplaire. Le système enregistre immédiatement la date effective du retour et passe le statut à "Retourné" |
| **CA3** | Si la date effective de retour est supérieure à la date limite prévue, le système calcule la différence en jours et applique instantanément la formule de pénalité : **Jours de suspension = Date effective de retour - Date limite de retour** |
| **CA4** | Si aucune réservation n'est en cours sur cet ouvrage, le nombre d'exemplaires disponibles augmente de 1 dans le catalogue et le statut redevient "Disponible" |

---

## US08 - Réserver un ouvrage indisponible (FIFO)

| ID | Critère d'Acceptation |
|----|----------------------|
| **CA1** | Le bouton "Réserver" ne doit être visible et cliquable que si et seulement si le statut de l'ouvrage est "Indisponible" (stock disponible = 0) |
| **CA2** | Le système doit interdire à un étudiant de réserver un ouvrage s'il a déjà un exemplaire de ce même ouvrage en cours d'emprunt ou s'il est déjà inscrit dans la file d'attente de ce livre |
| **CA3** | Chaque nouvelle réservation est horodatée et ajoutée à la fin de la liste d'attente de l'ouvrage. L'étudiant doit pouvoir visualiser sa position dans la file (ex: "Position 2/4") |
| **CA4** | Au moment où le bibliothécaire valide le retour d'un exemplaire de ce livre (via US07), si la file d'attente n'est pas vide, le livre passe au statut "Réservé - En attente de récupération" au lieu de repasser disponible dans le catalogue général |
| **CA5** | **Expiration de réservation** : L'étudiant dispose de 48h pour récupérer l'ouvrage réservé. Après 48h sans récupération, la réservation est automatiquement expirée avec le statut "EXPIRED", et le livre devient disponible pour le suivant dans la file. Une notification email l'en informe |
| **CA6** | **Traitement FIFO automatique** : Lorsqu'une réservation expire ou est annulée, le système automatise le passage de l'ouvrage au suivant dans la file, qui reçoit une notification "Votre réservation est prête - 48h pour récupérer" |

---

## US11 - Gérer les pénalités (Calcul automatique)

| ID | Critère d'Acceptation |
|----|----------------------|
| **CA1** | Le système récupère automatiquement la date effective de retour et la date limite de retour |
| **CA2** | Le système calcule le nombre de jours de retard : `Jours de suspension = Date retour - Date limite` |
| **CA3** | Le système applique la pénalité et l'associe au profil de l'étudiant |
| **CA4** | Le système inclut automatiquement le blocage des droits de l'étudiant (US12) |

---

## US12 - Bloquer/Débloquer compte étudiant

| ID | Critère d'Acceptation |
|----|----------------------|
| **CA1** | **Blocage** : Le système modifie l'état du compte de "Actif" à "Suspendu" et enregistre la date de fin de suspension |
| **CA2** | **Notification blocage** : L'étudiant reçoit un email l'informant du blocage et de la date de fin |
| **CA3** | **Déblocage automatique** : Un script quotidien vérifie les comptes suspendus. Si la date du jour ≥ date de fin, le compte repasse à "Actif" |
| **CA4** | **Déblocage manuel** : L'Administrateur peut lever manuellement une suspension avant la date prévue en saisissant un motif |