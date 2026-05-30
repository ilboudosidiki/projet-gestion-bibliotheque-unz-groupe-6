-- Insertion des catégories
INSERT INTO categorie (nom, description) VALUES 
('Informatique', 'Ouvrages sur la programmation, les algorithmes et les systèmes'),
('Mathématiques', 'Ouvrages de mathématiques pures et appliquées'),
('Physique', 'Ouvrages de physique théorique et expérimentale'),
('Littérature', 'Romans, poésie et essais littéraires');

-- Insertion des ouvrages
INSERT INTO ouvrage (isbn, titre, auteur, resume, mots_cles, date_publication, categorie_id) VALUES 
('978-2-212-12345-6', 'Spring Boot pour les nuls', 'Jean Dupont', 'Un guide complet pour débuter avec Spring Boot', 'Spring, Java, Boot', '2024-01-15', 1),
('978-2-212-12346-3', 'Algorithmique Avancée', 'Marie Koné', 'Théorie et pratique des algorithmes complexes', 'Algorithme, Complexité, Tri', '2023-06-20', 1),
('978-2-212-12347-0', 'Calcul Différentiel', 'Pierre Ouédraogo', 'Introduction au calcul différentiel', 'Maths, Calcul, Dérivée', '2024-03-10', 2),
('978-2-212-12348-7', 'Mécanique Quantique', 'Sophie Traoré', 'Les principes fondamentaux de la mécanique quantique', 'Physique, Quantique, Atome', '2023-09-05', 3);

-- Insertion des exemplaires
INSERT INTO exemplaire (code_barres, statut, rayon, date_acquisition, ouvrage_id) VALUES 
('EX-SB-001', 'DISPONIBLE', 'Rayon A1', '2024-02-01', 1),
('EX-SB-002', 'DISPONIBLE', 'Rayon A1', '2024-02-01', 1),
('EX-SB-003', 'EMPRUNTE', 'Rayon A1', '2024-02-01', 1),
('EX-AA-001', 'DISPONIBLE', 'Rayon A2', '2023-07-01', 2),
('EX-AA-002', 'EMPRUNTE', 'Rayon A2', '2023-07-01', 2),
('EX-CD-001', 'DISPONIBLE', 'Rayon B1', '2024-04-01', 3),
('EX-MQ-001', 'DISPONIBLE', 'Rayon C1', '2023-10-01', 4),
('EX-MQ-002', 'RESERVE', 'Rayon C1', '2023-10-01', 4);

-- Insertion utilisateurs de test (mot de passe = password123 hashé en BCrypt)
INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, est_actif, role, date_creation) VALUES 
('Test', 'User', 'test.user@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', true, 'ETUDIANT', NOW());

INSERT INTO etudiant (id, matricule) VALUES (1, 'ETU-TEST-001');