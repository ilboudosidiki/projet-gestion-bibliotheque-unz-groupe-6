# ARCHITECTURE EN COUCHES - Vue de Développement
## Application de Gestion de Bibliothèque Universitaire

---

## 1. Présentation de l'Architecture

L'application suit une architecture en 4 couches principales 
inspirée du modèle standard Spring Boot.

| Couche | Package | Responsabilité |
|--------|---------|----------------|
| Présentation | controllers, dto, mappers | Gérer les requêtes HTTP REST |
| Métier | services, interfaces | Logique métier et règles de gestion |
| Domaine | entities, enums, exceptions | Modèle métier JPA |
| Infrastructure | repositories, config, security | Persistance et sécurité |

Une cinquième couche transversale gère les préoccupations communes :

| Couche | Package | Responsabilité |
|--------|---------|----------------|
| Commun | exception, audit | Gestion globale des erreurs et logging |

---

## 2. Flux d'une Requête HTTP

Client HTTP
    |
    v
Controller (@RestController)
    |
    v
Service Interface
    |
    v
ServiceImpl (@Service)
    |
    v
Repository (@Repository)
    |
    v
Base de données MySQL

Le flux inverse (réponse) :
Base de données -> Repository -> Service -> Controller -> DTO -> Client HTTP

---

## 3. Organisation des Packages

src/main/java/com/bibliotheque/

├── controller/
│   ├── AuthController.java
│   ├── OuvrageController.java
│   ├── EmpruntController.java
│   ├── ReservationController.java
│   ├── UtilisateurController.java
│   └── AdminController.java

├── dto/
│   ├── OuvrageDTO.java
│   ├── EmpruntDTO.java
│   ├── ReservationDTO.java
│   ├── LoginRequest.java
│   └── RegisterRequest.java

├── mapper/
│   ├── OuvrageMapper.java
│   └── EmpruntMapper.java

├── service/
│   ├── interfaces/
│   │   ├── IOuvrageService.java
│   │   ├── IEmpruntService.java
│   │   ├── IReservationService.java
│   │   ├── IPenaliteService.java
│   │   └── INotificationService.java
│   │
│   └── impl/
│       ├── OuvrageServiceImpl.java
│       ├── EmpruntServiceImpl.java
│       ├── ReservationServiceImpl.java
│       ├── PenaliteServiceImpl.java
│       └── NotificationServiceImpl.java

├── entity/
│   ├── Utilisateur.java (abstraite)
│   ├── Etudiant.java
│   ├── Bibliothecaire.java
│   ├── Administrateur.java
│   ├── Ouvrage.java
│   ├── Exemplaire.java
│   ├── Categorie.java
│   ├── Emprunt.java
│   ├── Reservation.java
│   └── Penalite.java

├── enums/
│   ├── StatutExemplaire.java
│   ├── StatutEmprunt.java
│   ├── StatutReservation.java
│   └── RoleUtilisateur.java

├── exception/
│   ├── QuotaAtteintException.java
│   ├── CompteSuspenduException.java
│   ├── OuvrageIndisponibleException.java
│   └── GlobalExceptionHandler.java

├── repository/
│   ├── UtilisateurRepository.java
│   ├── OuvrageRepository.java
│   ├── ExemplaireRepository.java
│   ├── EmpruntRepository.java
│   ├── ReservationRepository.java
│   ├── PenaliteRepository.java
│   └── CategorieRepository.java

├── config/
│   ├── SecurityConfig.java
│   ├── JwtConfig.java
│   └── SwaggerConfig.java

├── security/
│   ├── JwtTokenProvider.java
│   └── JwtAuthenticationFilter.java

└── event/
    └── RetourEffectueEvent.java

---

## 4. Description des Couches

### 4.1 Couche Présentation (Web)

Rôle : Point d'entrée de l'application. Gère les requêtes HTTP.

Composants :
- Controllers : Annotés @RestController, exposent les API REST
- DTOs : Objets de transfert entre client et serveur
- Mappers : Conversion Entity <-> DTO

### 4.2 Couche Métier (Service)

Rôle : Contient la logique métier et les règles de gestion.

Règles implémentées :
- Quota maximum de 3 emprunts par étudiant
- Suspension automatique en cas de retard
- File d'attente FIFO pour les réservations
- Calcul automatique des pénalités (1 jour retard = 1 jour suspension)

### 4.3 Couche Domaine (Entity)

Rôle : Modélise les objets métier.

Héritage Utilisateur :
- Utilisateur (abstraite)
  - Etudiant
  - Bibliothecaire
  - Administrateur

### 4.4 Couche Infrastructure

Rôle : Persistance et sécurité.

Composants :
- Repositories : Spring Data JPA
- Security : Filtres JWT
- Config : Beans de configuration

### 4.5 Couche Commune

Rôle : Préoccupations transversales.

Composants :
- GlobalExceptionHandler (@ControllerAdvice)
- Logging (SLF4J)

---

## 5. Technologies Utilisées

| Composant | Technologie |
|-----------|-------------|
| Langage | Java 17 |
| Framework | Spring Boot 3.2.x |
| Base de données | MySQL 8.x |
| ORM | Spring Data JPA / Hibernate |
| Sécurité | Spring Security + JWT |
| API | REST (JSON) |
| Tests | JUnit 5 + Mockito |
| Documentation | Swagger / OpenAPI |
| Build | Maven |

---

## 6. Principes SOLID dans l'Architecture

| Couche | Principe SOLID appliqué |
|--------|------------------------|
| Services | S - Responsabilité Unique |
| Interfaces | O - Ouvert/Fermé + I - Ségrégation |
| Entity (héritage) | L - Substitution de Liskov |
| Injection | D - Inversion des Dépendances |