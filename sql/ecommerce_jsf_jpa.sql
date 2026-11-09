-- ============================================================================
-- Université Abdelmalek Essaâdi - Faculté des Sciences et Techniques de Tanger
-- Département Génie Informatique - Cycle Ingénieur : LSI
-- Module JEE - Encadré par Pr. Lotfi ELAACHAK
-- Script SQL MySQL complet : Base de données `ecommerce_jsf_jpa`
-- (Note : L'application génère également ces tables et données automatiquement
--  au démarrage via JPA/EclipseLink et AppInitListener si la BDD est vide.)
-- ============================================================================

CREATE DATABASE IF NOT EXISTS `ecommerce_jsf_jpa`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `ecommerce_jsf_jpa`;

/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.8.6-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: ecommerce_jsf_jpa
-- ------------------------------------------------------
-- Server version	11.8.6-MariaDB-0+deb13u1 from Debian

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `categorie`
--

DROP TABLE IF EXISTS `categorie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorie` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  `ICONE` varchar(60) DEFAULT NULL,
  `NOM` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NOM` (`NOM`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorie`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `categorie` WRITE;
/*!40000 ALTER TABLE `categorie` DISABLE KEYS */;
INSERT INTO `categorie` VALUES
(1,'Ultrabooks, stations de travail ingénieur et PC portables haute performance.','laptop','Ordinateurs & Laptops'),
(2,'Claviers mécaniques, souris optiques, écrans 165 Hz et manettes pro.','gaming','Périphériques & Gaming'),
(3,'Derniers smartphones 5G, tablettes tactiles et accessoires mobiles.','phone','Smartphones & Tablettes'),
(4,'Casques à réduction active de bruit, écouteurs sans fil et montres connectées.','audio','Audio & Objets Connectés'),
(5,'SSD NVMe PCIe 4.0, routeurs Wi-Fi 6E, barrettes RAM et hubs USB-C.','storage','Composants & Stockage');
/*!40000 ALTER TABLE `categorie` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `commande`
--

DROP TABLE IF EXISTS `commande`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `commande` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `adresse_livraison` varchar(400) DEFAULT NULL,
  `date_commande` datetime NOT NULL,
  `frais_livraison` double DEFAULT NULL,
  `mode_paiement` varchar(50) DEFAULT NULL,
  `montant_total` double NOT NULL,
  `numero_commande` varchar(50) NOT NULL,
  `STATUT` varchar(30) NOT NULL,
  `telephone_contact` varchar(30) DEFAULT NULL,
  `ville_livraison` varchar(100) DEFAULT NULL,
  `internaute_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `numero_commande` (`numero_commande`),
  KEY `FK_commande_internaute_id` (`internaute_id`),
  CONSTRAINT `FK_commande_internaute_id` FOREIGN KEY (`internaute_id`) REFERENCES `internaute` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `commande`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `commande` WRITE;
/*!40000 ALTER TABLE `commande` DISABLE KEYS */;
INSERT INTO `commande` VALUES
(1,'Boulevard Mohammed VI, Résidence Corniche N°14','2026-11-09 09:56:22',0,'CARTE_BANCAIRE',3040,'CMD-20261109-1001','LIVREE','+212 6 77 88 99 00','Tanger',2),
(2,'Boulevard Mohammed VI, Résidence Corniche N°14','2026-11-09 09:57:06',0,'CARTE_BANCAIRE',3690,'CMD-20261109-8843','VALIDEE','+212 6 77 88 99 00','Tanger',2);
/*!40000 ALTER TABLE `commande` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `internaute`
--

DROP TABLE IF EXISTS `internaute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `internaute` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ADRESSE` varchar(350) DEFAULT NULL,
  `date_inscription` datetime DEFAULT NULL,
  `EMAIL` varchar(150) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `NOM` varchar(80) NOT NULL,
  `PRENOM` varchar(80) NOT NULL,
  `ROLE` varchar(20) NOT NULL,
  `TELEPHONE` varchar(30) DEFAULT NULL,
  `VILLE` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `EMAIL` (`EMAIL`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `internaute`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `internaute` WRITE;
/*!40000 ALTER TABLE `internaute` DISABLE KEYS */;
INSERT INTO `internaute` VALUES
(1,'Ancienne Route de l\'Aéroport, Km 10, Ziaten, BP 416','2026-11-09 09:56:22','admin@fstt.ac.ma','240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9','ELAACHAK','Admin LSI','ADMIN','+212 6 61 00 11 22','Tanger'),
(2,'Boulevard Mohammed VI, Résidence Corniche N°14','2026-11-09 09:56:22','client@fstt.ac.ma','186474c1f2c2f735a54c2cf82ee8e87f2a5cd30940e280029363fecedfc5328c','BENALI','Youssef','CLIENT','+212 6 77 88 99 00','Tanger');
/*!40000 ALTER TABLE `internaute` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `ligne_commande`
--

DROP TABLE IF EXISTS `ligne_commande`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ligne_commande` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `prix_unitaire` double NOT NULL,
  `QUANTITE` int(11) NOT NULL,
  `commande_id` bigint(20) NOT NULL,
  `produit_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ligne_commande_commande_id` (`commande_id`),
  KEY `FK_ligne_commande_produit_id` (`produit_id`),
  CONSTRAINT `FK_ligne_commande_commande_id` FOREIGN KEY (`commande_id`) REFERENCES `commande` (`ID`),
  CONSTRAINT `FK_ligne_commande_produit_id` FOREIGN KEY (`produit_id`) REFERENCES `produit` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ligne_commande`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `ligne_commande` WRITE;
/*!40000 ALTER TABLE `ligne_commande` DISABLE KEYS */;
INSERT INTO `ligne_commande` VALUES
(1,1950,1,1,12),
(2,1090,1,1,9),
(3,3690,1,2,11);
/*!40000 ALTER TABLE `ligne_commande` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `ligne_panier`
--

DROP TABLE IF EXISTS `ligne_panier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `ligne_panier` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `prix_unitaire` double NOT NULL,
  `QUANTITE` int(11) NOT NULL,
  `panier_id` bigint(20) DEFAULT NULL,
  `produit_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ligne_panier_panier_id` (`panier_id`),
  KEY `FK_ligne_panier_produit_id` (`produit_id`),
  CONSTRAINT `FK_ligne_panier_panier_id` FOREIGN KEY (`panier_id`) REFERENCES `panier` (`ID`),
  CONSTRAINT `FK_ligne_panier_produit_id` FOREIGN KEY (`produit_id`) REFERENCES `produit` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ligne_panier`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `ligne_panier` WRITE;
/*!40000 ALTER TABLE `ligne_panier` DISABLE KEYS */;
/*!40000 ALTER TABLE `ligne_panier` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `panier`
--

DROP TABLE IF EXISTS `panier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `panier` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_creation` datetime DEFAULT NULL,
  `date_modification` datetime DEFAULT NULL,
  `internaute_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `internaute_id` (`internaute_id`),
  CONSTRAINT `FK_panier_internaute_id` FOREIGN KEY (`internaute_id`) REFERENCES `internaute` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `panier`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `panier` WRITE;
/*!40000 ALTER TABLE `panier` DISABLE KEYS */;
INSERT INTO `panier` VALUES
(1,'2026-11-09 09:56:22','2026-11-09 09:56:22',1),
(2,'2026-11-09 09:56:22','2026-11-09 09:57:06',2);
/*!40000 ALTER TABLE `panier` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Table structure for table `produit`
--

DROP TABLE IF EXISTS `produit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `produit` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ancien_prix` double DEFAULT NULL,
  `date_ajout` datetime DEFAULT NULL,
  `DESCRIPTION` varchar(1500) DEFAULT NULL,
  `en_vedette` tinyint(1) DEFAULT 0,
  `image_url` varchar(4000) DEFAULT NULL,
  `LIBELLE` varchar(180) NOT NULL,
  `MARQUE` varchar(100) DEFAULT NULL,
  `PRIX` double NOT NULL,
  `quantite_stock` int(11) NOT NULL,
  `REFERENCE` varchar(50) DEFAULT NULL,
  `categorie_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `REFERENCE` (`REFERENCE`),
  KEY `FK_produit_categorie_id` (`categorie_id`),
  CONSTRAINT `FK_produit_categorie_id` FOREIGN KEY (`categorie_id`) REFERENCES `categorie` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produit`
--

SET @OLD_AUTOCOMMIT=@@AUTOCOMMIT, @@AUTOCOMMIT=0;
LOCK TABLES `produit` WRITE;
/*!40000 ALTER TABLE `produit` DISABLE KEYS */;
INSERT INTO `produit` VALUES
(1,14890,'2026-11-09 09:56:22','Écran Dynamic AMOLED 2X 6,8\" QHD+ 120 Hz, capteur 200 MP, stylet S-Pen intégré, châssis en titane et fonctionnalités Galaxy AI embarquées.',1,'data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCA0MDAgMjYwJz48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9J2JnR3JhZCcgeDE9JzAlJyB5MT0nMCUnIHgyPScxMDAlJyB5Mj0nMTAwJSc+PHN0b3Agb2Zmc2V0PScwJScgc3RvcC1jb2xvcj0nIzFlMWI0YicvPjxzdG9wIG9mZnNldD0nMTAwJScgc3RvcC1jb2xvcj0nIzQzMzhjYScvPjwvbGluZWFyR3JhZGllbnQ+PGxpbmVhckdyYWRpZW50IGlkPSdzY3JlZW5HcmFkJyB4MT0nMCUnIHkxPScwJScgeDI9JzEwMCUnIHkyPScxMDAlJz48c3RvcCBvZmZzZXQ9JzAlJyBzdG9wLWNvbG9yPScjMzhiZGY4Jy8+PHN0b3Agb2Zmc2V0PScxMDAlJyBzdG9wLWNvbG9yPScjNjM2NmYxJy8+PC9saW5lYXJHcmFkaWVudD48L2RlZnM+PHJlY3Qgd2lkdGg9JzQwMCcgaGVpZ2h0PScyNjAnIGZpbGw9J3VybCgjYmdHcmFkKScvPjxjaXJjbGUgY3g9JzM0MCcgY3k9JzQwJyByPSc5MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA4KScvPjxjaXJjbGUgY3g9JzUwJyBjeT0nMjMwJyByPSc4MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA2KScvPjxyZWN0IHg9JzE1MicgeT0nNTInIHdpZHRoPSc5NicgaGVpZ2h0PScxNzYnIHJ4PScxOCcgZmlsbD0nIzBmMTcyYScgc3Ryb2tlPScjZTJlOGYwJyBzdHJva2Utd2lkdGg9JzQnLz48cmVjdCB4PScxNjEnIHk9JzY2JyB3aWR0aD0nNzgnIGhlaWdodD0nMTQ2JyByeD0nMTAnIGZpbGw9J3VybCgjc2NyZWVuR3JhZCknLz48cmVjdCB4PScxODYnIHk9JzU4JyB3aWR0aD0nMjgnIGhlaWdodD0nNCcgcng9JzInIGZpbGw9JyM2NDc0OGInLz48Y2lyY2xlIGN4PScyMDAnIGN5PScxOTgnIHI9JzUnIGZpbGw9J3JnYmEoMjU1LDI1NSwyNTUsMC41KScvPjxyZWN0IHg9JzEyMCcgeT0nMjE4JyB3aWR0aD0nMTYwJyBoZWlnaHQ9JzI2JyByeD0nMTMnIGZpbGw9J3JnYmEoMTUsMjMsNDIsMC41NSknLz48dGV4dCB4PScyMDAnIHk9JzIzNScgZm9udC1mYW1pbHk9J1NlZ29lIFVJLCBBcmlhbCwgc2Fucy1zZXJpZicgZm9udC1zaXplPScxMicgZm9udC13ZWlnaHQ9JzcwMCcgZmlsbD0nI2ZmZmZmZicgdGV4dC1hbmNob3I9J21pZGRsZScgbGV0dGVyLXNwYWNpbmc9JzEnPkdBTEFYWSBTMjQgVUxUUkE8L3RleHQ+PC9zdmc+','Galaxy S24 Ultra 5G (256 Go - Titane)','Samsung',13490,15,'PRD-PHN-001',3),
(2,19500,'2026-11-09 09:56:22','Certifié Intel Evo, processeur Core i7-1365U, 32 Go LPDDR5, SSD NVMe 1 To, clavier rétroéclairé légendaire et châssis fibre de carbone 1,12 kg.',1,'data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCA0MDAgMjYwJz48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9J2JnR3JhZCcgeDE9JzAlJyB5MT0nMCUnIHgyPScxMDAlJyB5Mj0nMTAwJSc+PHN0b3Agb2Zmc2V0PScwJScgc3RvcC1jb2xvcj0nIzExMTgyNycvPjxzdG9wIG9mZnNldD0nMTAwJScgc3RvcC1jb2xvcj0nIzBmNzY2ZScvPjwvbGluZWFyR3JhZGllbnQ+PGxpbmVhckdyYWRpZW50IGlkPSdzY3JlZW5HcmFkJyB4MT0nMCUnIHkxPScwJScgeDI9JzEwMCUnIHkyPScxMDAlJz48c3RvcCBvZmZzZXQ9JzAlJyBzdG9wLWNvbG9yPScjMzhiZGY4Jy8+PHN0b3Agb2Zmc2V0PScxMDAlJyBzdG9wLWNvbG9yPScjNjM2NmYxJy8+PC9saW5lYXJHcmFkaWVudD48L2RlZnM+PHJlY3Qgd2lkdGg9JzQwMCcgaGVpZ2h0PScyNjAnIGZpbGw9J3VybCgjYmdHcmFkKScvPjxjaXJjbGUgY3g9JzM0MCcgY3k9JzQwJyByPSc5MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA4KScvPjxjaXJjbGUgY3g9JzUwJyBjeT0nMjMwJyByPSc4MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA2KScvPjxyZWN0IHg9JzExMCcgeT0nNzInIHdpZHRoPScxODAnIGhlaWdodD0nMTE0JyByeD0nMTAnIGZpbGw9JyMwZjE3MmEnIHN0cm9rZT0nI2UyZThmMCcgc3Ryb2tlLXdpZHRoPSc0Jy8+PHJlY3QgeD0nMTIyJyB5PSc4NCcgd2lkdGg9JzE1NicgaGVpZ2h0PSc5MCcgcng9JzQnIGZpbGw9J3VybCgjc2NyZWVuR3JhZCknLz48cGF0aCBkPSdNODYgMTg4IEgzMTQgTDI5OCAyMDQgSDEwMiBaJyBmaWxsPScjY2JkNWUxJy8+PHJlY3QgeD0nMTc2JyB5PScxODgnIHdpZHRoPSc0OCcgaGVpZ2h0PSc1JyByeD0nMicgZmlsbD0nIzY0NzQ4YicvPjxyZWN0IHg9JzEyMCcgeT0nMjE4JyB3aWR0aD0nMTYwJyBoZWlnaHQ9JzI2JyByeD0nMTMnIGZpbGw9J3JnYmEoMTUsMjMsNDIsMC41NSknLz48dGV4dCB4PScyMDAnIHk9JzIzNScgZm9udC1mYW1pbHk9J1NlZ29lIFVJLCBBcmlhbCwgc2Fucy1zZXJpZicgZm9udC1zaXplPScxMicgZm9udC13ZWlnaHQ9JzcwMCcgZmlsbD0nI2ZmZmZmZicgdGV4dC1hbmNob3I9J21pZGRsZScgbGV0dGVyLXNwYWNpbmc9JzEnPlRISU5LUEFEIFgxPC90ZXh0Pjwvc3ZnPg==','ThinkPad X1 Carbon Gen 11 (Core i7 / 32 Go)','Lenovo',18200,8,'PRD-LAP-002',1),
(3,8490,'2026-11-09 09:56:22','Puce M2 ultra-rapide, compatible Apple Pencil Pro, écran Liquid Retina laminé antireflet — parfait pour la prise de notes en cycle ingénieur.',0,'data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCA0MDAgMjYwJz48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9J2JnR3JhZCcgeDE9JzAlJyB5MT0nMCUnIHgyPScxMDAlJyB5Mj0nMTAwJSc+PHN0b3Agb2Zmc2V0PScwJScgc3RvcC1jb2xvcj0nIzBjNGE2ZScvPjxzdG9wIG9mZnNldD0nMTAwJScgc3RvcC1jb2xvcj0nIzAyODRjNycvPjwvbGluZWFyR3JhZGllbnQ+PGxpbmVhckdyYWRpZW50IGlkPSdzY3JlZW5HcmFkJyB4MT0nMCUnIHkxPScwJScgeDI9JzEwMCUnIHkyPScxMDAlJz48c3RvcCBvZmZzZXQ9JzAlJyBzdG9wLWNvbG9yPScjMzhiZGY4Jy8+PHN0b3Agb2Zmc2V0PScxMDAlJyBzdG9wLWNvbG9yPScjNjM2NmYxJy8+PC9saW5lYXJHcmFkaWVudD48L2RlZnM+PHJlY3Qgd2lkdGg9JzQwMCcgaGVpZ2h0PScyNjAnIGZpbGw9J3VybCgjYmdHcmFkKScvPjxjaXJjbGUgY3g9JzM0MCcgY3k9JzQwJyByPSc5MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA4KScvPjxjaXJjbGUgY3g9JzUwJyBjeT0nMjMwJyByPSc4MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA2KScvPjxyZWN0IHg9JzE1MicgeT0nNTInIHdpZHRoPSc5NicgaGVpZ2h0PScxNzYnIHJ4PScxOCcgZmlsbD0nIzBmMTcyYScgc3Ryb2tlPScjZTJlOGYwJyBzdHJva2Utd2lkdGg9JzQnLz48cmVjdCB4PScxNjEnIHk9JzY2JyB3aWR0aD0nNzgnIGhlaWdodD0nMTQ2JyByeD0nMTAnIGZpbGw9J3VybCgjc2NyZWVuR3JhZCknLz48cmVjdCB4PScxODYnIHk9JzU4JyB3aWR0aD0nMjgnIGhlaWdodD0nNCcgcng9JzInIGZpbGw9JyM2NDc0OGInLz48Y2lyY2xlIGN4PScyMDAnIGN5PScxOTgnIHI9JzUnIGZpbGw9J3JnYmEoMjU1LDI1NSwyNTUsMC41KScvPjxyZWN0IHg9JzEyMCcgeT0nMjE4JyB3aWR0aD0nMTYwJyBoZWlnaHQ9JzI2JyByeD0nMTMnIGZpbGw9J3JnYmEoMTUsMjMsNDIsMC41NSknLz48dGV4dCB4PScyMDAnIHk9JzIzNScgZm9udC1mYW1pbHk9J1NlZ29lIFVJLCBBcmlhbCwgc2Fucy1zZXJpZicgZm9udC1zaXplPScxMicgZm9udC13ZWlnaHQ9JzcwMCcgZmlsbD0nI2ZmZmZmZicgdGV4dC1hbmNob3I9J21pZGRsZScgbGV0dGVyLXNwYWNpbmc9JzEnPklQQUQgQUlSIE0yPC90ZXh0Pjwvc3ZnPg==','iPad Air M2 11\" Wi-Fi 256 Go','Apple',7890,14,'PRD-PHN-003',3),
(4,NULL,'2026-11-09 09:56:22','Puce A18 Pro gravée en 3 nm, écran Super Retina XDR ProMotion 120 Hz, triple caméra Pro 48 MP avec zoom optique 5x et USB-C haut débit.',1,'data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCA0MDAgMjYwJz48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9J2JnR3JhZCcgeDE9JzAlJyB5MT0nMCUnIHgyPScxMDAlJyB5Mj0nMTAwJSc+PHN0b3Agb2Zmc2V0PScwJScgc3RvcC1jb2xvcj0nIzFjMTkxNycvPjxzdG9wIG9mZnNldD0nMTAwJScgc3RvcC1jb2xvcj0nIzU3NTM0ZScvPjwvbGluZWFyR3JhZGllbnQ+PGxpbmVhckdyYWRpZW50IGlkPSdzY3JlZW5HcmFkJyB4MT0nMCUnIHkxPScwJScgeDI9JzEwMCUnIHkyPScxMDAlJz48c3RvcCBvZmZzZXQ9JzAlJyBzdG9wLWNvbG9yPScjMzhiZGY4Jy8+PHN0b3Agb2Zmc2V0PScxMDAlJyBzdG9wLWNvbG9yPScjNjM2NmYxJy8+PC9saW5lYXJHcmFkaWVudD48L2RlZnM+PHJlY3Qgd2lkdGg9JzQwMCcgaGVpZ2h0PScyNjAnIGZpbGw9J3VybCgjYmdHcmFkKScvPjxjaXJjbGUgY3g9JzM0MCcgY3k9JzQwJyByPSc5MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA4KScvPjxjaXJjbGUgY3g9JzUwJyBjeT0nMjMwJyByPSc4MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA2KScvPjxyZWN0IHg9JzE1MicgeT0nNTInIHdpZHRoPSc5NicgaGVpZ2h0PScxNzYnIHJ4PScxOCcgZmlsbD0nIzBmMTcyYScgc3Ryb2tlPScjZTJlOGYwJyBzdHJva2Utd2lkdGg9JzQnLz48cmVjdCB4PScxNjEnIHk9JzY2JyB3aWR0aD0nNzgnIGhlaWdodD0nMTQ2JyByeD0nMTAnIGZpbGw9J3VybCgjc2NyZWVuR3JhZCknLz48cmVjdCB4PScxODYnIHk9JzU4JyB3aWR0aD0nMjgnIGhlaWdodD0nNCcgcng9JzInIGZpbGw9JyM2NDc0OGInLz48Y2lyY2xlIGN4PScyMDAnIGN5PScxOTgnIHI9JzUnIGZpbGw9J3JnYmEoMjU1LDI1NSwyNTUsMC41KScvPjxyZWN0IHg9JzEyMCcgeT0nMjE4JyB3aWR0aD0nMTYwJyBoZWlnaHQ9JzI2JyByeD0nMTMnIGZpbGw9J3JnYmEoMTUsMjMsNDIsMC41NSknLz48dGV4dCB4PScyMDAnIHk9JzIzNScgZm9udC1mYW1pbHk9J1NlZ29lIFVJLCBBcmlhbCwgc2Fucy1zZXJpZicgZm9udC1zaXplPScxMicgZm9udC13ZWlnaHQ9JzcwMCcgZmlsbD0nI2ZmZmZmZicgdGV4dC1hbmNob3I9J21pZGRsZScgbGV0dGVyLXNwYWNpbmc9JzEnPklQSE9ORSAxNiBQUk88L3RleHQ+PC9zdmc+','iPhone 16 Pro 256 Go - Titane Naturel','Apple',14290,10,'PRD-PHN-002',3),
(5,NULL,'2026-11-09 09:56:22','Puce H2, réduction active du bruit jusqu\'à 2x supérieure, mode Transparence adaptatif et boîtier MagSafe USB-C résistant à l\'eau IP54.',0,'data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCA0MDAgMjYwJz48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9J2JnR3JhZCcgeDE9JzAlJyB5MT0nMCUnIHgyPScxMDAlJyB5Mj0nMTAwJSc+PHN0b3Agb2Zmc2V0PScwJScgc3RvcC1jb2xvcj0nIzBmMTcyYScvPjxzdG9wIG9mZnNldD0nMTAwJScgc3RvcC1jb2xvcj0nIzAzNjlhMScvPjwvbGluZWFyR3JhZGllbnQ+PGxpbmVhckdyYWRpZW50IGlkPSdzY3JlZW5HcmFkJyB4MT0nMCUnIHkxPScwJScgeDI9JzEwMCUnIHkyPScxMDAlJz48c3RvcCBvZmZzZXQ9JzAlJyBzdG9wLWNvbG9yPScjMzhiZGY4Jy8+PHN0b3Agb2Zmc2V0PScxMDAlJyBzdG9wLWNvbG9yPScjNjM2NmYxJy8+PC9saW5lYXJHcmFkaWVudD48L2RlZnM+PHJlY3Qgd2lkdGg9JzQwMCcgaGVpZ2h0PScyNjAnIGZpbGw9J3VybCgjYmdHcmFkKScvPjxjaXJjbGUgY3g9JzM0MCcgY3k9JzQwJyByPSc5MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA4KScvPjxjaXJjbGUgY3g9JzUwJyBjeT0nMjMwJyByPSc4MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA2KScvPjxwYXRoIGQ9J00xMzIgMTU2IFYxMzIgQTY4IDY4IDAgMCAxIDI2OCAxMzIgVjE1NicgZmlsbD0nbm9uZScgc3Ryb2tlPScjZTJlOGYwJyBzdHJva2Utd2lkdGg9JzEyJyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48cmVjdCB4PScxMTQnIHk9JzEzOCcgd2lkdGg9JzM0JyBoZWlnaHQ9JzYyJyByeD0nMTYnIGZpbGw9JyMwZjE3MmEnIHN0cm9rZT0nIzM4YmRmOCcgc3Ryb2tlLXdpZHRoPSc0Jy8+PHJlY3QgeD0nMjUyJyB5PScxMzgnIHdpZHRoPSczNCcgaGVpZ2h0PSc2Micgcng9JzE2JyBmaWxsPScjMGYxNzJhJyBzdHJva2U9JyMzOGJkZjgnIHN0cm9rZS13aWR0aD0nNCcvPjxjaXJjbGUgY3g9JzIwMCcgY3k9JzE1MCcgcj0nMjAnIGZpbGw9J3JnYmEoMjU1LDI1NSwyNTUsMC4xMiknLz48cmVjdCB4PScxMjAnIHk9JzIxOCcgd2lkdGg9JzE2MCcgaGVpZ2h0PScyNicgcng9JzEzJyBmaWxsPSdyZ2JhKDE1LDIzLDQyLDAuNTUpJy8+PHRleHQgeD0nMjAwJyB5PScyMzUnIGZvbnQtZmFtaWx5PSdTZWdvZSBVSSwgQXJpYWwsIHNhbnMtc2VyaWYnIGZvbnQtc2l6ZT0nMTInIGZvbnQtd2VpZ2h0PSc3MDAnIGZpbGw9JyNmZmZmZmYnIHRleHQtYW5jaG9yPSdtaWRkbGUnIGxldHRlci1zcGFjaW5nPScxJz5BSVJQT0RTIFBSTyAyPC90ZXh0Pjwvc3ZnPg==','AirPods Pro (2e génération) USB-C','Apple',2790,22,'PRD-AUD-002',4),
(6,890,'2026-11-09 09:56:22','Retour haptique immersif, gâchettes adaptatives dynamiques, microphone intégré et compatibilité native Bluetooth/USB-C sur PC et PlayStation 5.',0,'data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCA0MDAgMjYwJz48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9J2JnR3JhZCcgeDE9JzAlJyB5MT0nMCUnIHgyPScxMDAlJyB5Mj0nMTAwJSc+PHN0b3Agb2Zmc2V0PScwJScgc3RvcC1jb2xvcj0nIzE3MjU1NCcvPjxzdG9wIG9mZnNldD0nMTAwJScgc3RvcC1jb2xvcj0nIzI1NjNlYicvPjwvbGluZWFyR3JhZGllbnQ+PGxpbmVhckdyYWRpZW50IGlkPSdzY3JlZW5HcmFkJyB4MT0nMCUnIHkxPScwJScgeDI9JzEwMCUnIHkyPScxMDAlJz48c3RvcCBvZmZzZXQ9JzAlJyBzdG9wLWNvbG9yPScjMzhiZGY4Jy8+PHN0b3Agb2Zmc2V0PScxMDAlJyBzdG9wLWNvbG9yPScjNjM2NmYxJy8+PC9saW5lYXJHcmFkaWVudD48L2RlZnM+PHJlY3Qgd2lkdGg9JzQwMCcgaGVpZ2h0PScyNjAnIGZpbGw9J3VybCgjYmdHcmFkKScvPjxjaXJjbGUgY3g9JzM0MCcgY3k9JzQwJyByPSc5MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA4KScvPjxjaXJjbGUgY3g9JzUwJyBjeT0nMjMwJyByPSc4MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA2KScvPjxyZWN0IHg9Jzk1JyB5PSc5Nicgd2lkdGg9JzIxMCcgaGVpZ2h0PSc5Micgcng9JzQyJyBmaWxsPScjMGYxNzJhJyBzdHJva2U9JyNlMmU4ZjAnIHN0cm9rZS13aWR0aD0nNCcvPjxwYXRoIGQ9J00xNDUgMTI0IFYxNjAgTTEyNyAxNDIgSDE2Mycgc3Ryb2tlPScjMzhiZGY4JyBzdHJva2Utd2lkdGg9JzgnIHN0cm9rZS1saW5lY2FwPSdyb3VuZCcvPjxjaXJjbGUgY3g9JzI1MicgY3k9JzEyOCcgcj0nOCcgZmlsbD0nI2ZiNzE4NScvPjxjaXJjbGUgY3g9JzI3MicgY3k9JzE0Micgcj0nOCcgZmlsbD0nIzM0ZDM5OScvPjxjaXJjbGUgY3g9JzIzMicgY3k9JzE0Micgcj0nOCcgZmlsbD0nI2ZiYmYyNCcvPjxjaXJjbGUgY3g9JzI1MicgY3k9JzE1Nicgcj0nOCcgZmlsbD0nIzgxOGNmOCcvPjxyZWN0IHg9JzEyMCcgeT0nMjE4JyB3aWR0aD0nMTYwJyBoZWlnaHQ9JzI2JyByeD0nMTMnIGZpbGw9J3JnYmEoMTUsMjMsNDIsMC41NSknLz48dGV4dCB4PScyMDAnIHk9JzIzNScgZm9udC1mYW1pbHk9J1NlZ29lIFVJLCBBcmlhbCwgc2Fucy1zZXJpZicgZm9udC1zaXplPScxMicgZm9udC13ZWlnaHQ9JzcwMCcgZmlsbD0nI2ZmZmZmZicgdGV4dC1hbmNob3I9J21pZGRsZScgbGV0dGVyLXNwYWNpbmc9JzEnPkRVQUxTRU5TRSBQUk88L3RleHQ+PC9zdmc+','Manette DualSense Wireless PS5 & PC','Sony',790,30,'PRD-GAM-003',2),
(7,17990,'2026-11-09 09:56:22','Puce Apple M3 8 coeurs, écran Liquid Retina 15,3 pouces, autonomie jusqu\'à 18 heures, châssis aluminium ultra-fin idéal pour le développement logiciel et l\'ingénierie.',1,'data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCA0MDAgMjYwJz48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9J2JnR3JhZCcgeDE9JzAlJyB5MT0nMCUnIHgyPScxMDAlJyB5Mj0nMTAwJSc+PHN0b3Agb2Zmc2V0PScwJScgc3RvcC1jb2xvcj0nIzBmMTcyYScvPjxzdG9wIG9mZnNldD0nMTAwJScgc3RvcC1jb2xvcj0nIzFkNGVkOCcvPjwvbGluZWFyR3JhZGllbnQ+PGxpbmVhckdyYWRpZW50IGlkPSdzY3JlZW5HcmFkJyB4MT0nMCUnIHkxPScwJScgeDI9JzEwMCUnIHkyPScxMDAlJz48c3RvcCBvZmZzZXQ9JzAlJyBzdG9wLWNvbG9yPScjMzhiZGY4Jy8+PHN0b3Agb2Zmc2V0PScxMDAlJyBzdG9wLWNvbG9yPScjNjM2NmYxJy8+PC9saW5lYXJHcmFkaWVudD48L2RlZnM+PHJlY3Qgd2lkdGg9JzQwMCcgaGVpZ2h0PScyNjAnIGZpbGw9J3VybCgjYmdHcmFkKScvPjxjaXJjbGUgY3g9JzM0MCcgY3k9JzQwJyByPSc5MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA4KScvPjxjaXJjbGUgY3g9JzUwJyBjeT0nMjMwJyByPSc4MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA2KScvPjxyZWN0IHg9JzExMCcgeT0nNzInIHdpZHRoPScxODAnIGhlaWdodD0nMTE0JyByeD0nMTAnIGZpbGw9JyMwZjE3MmEnIHN0cm9rZT0nI2UyZThmMCcgc3Ryb2tlLXdpZHRoPSc0Jy8+PHJlY3QgeD0nMTIyJyB5PSc4NCcgd2lkdGg9JzE1NicgaGVpZ2h0PSc5MCcgcng9JzQnIGZpbGw9J3VybCgjc2NyZWVuR3JhZCknLz48cGF0aCBkPSdNODYgMTg4IEgzMTQgTDI5OCAyMDQgSDEwMiBaJyBmaWxsPScjY2JkNWUxJy8+PHJlY3QgeD0nMTc2JyB5PScxODgnIHdpZHRoPSc0OCcgaGVpZ2h0PSc1JyByeD0nMicgZmlsbD0nIzY0NzQ4YicvPjxyZWN0IHg9JzEyMCcgeT0nMjE4JyB3aWR0aD0nMTYwJyBoZWlnaHQ9JzI2JyByeD0nMTMnIGZpbGw9J3JnYmEoMTUsMjMsNDIsMC41NSknLz48dGV4dCB4PScyMDAnIHk9JzIzNScgZm9udC1mYW1pbHk9J1NlZ29lIFVJLCBBcmlhbCwgc2Fucy1zZXJpZicgZm9udC1zaXplPScxMicgZm9udC13ZWlnaHQ9JzcwMCcgZmlsbD0nI2ZmZmZmZicgdGV4dC1hbmNob3I9J21pZGRsZScgbGV0dGVyLXNwYWNpbmc9JzEnPk1BQ0JPT0sgTTM8L3RleHQ+PC9zdmc+','MacBook Air 15\" M3 (16 Go / 512 Go SSD)','Apple',16490,12,'PRD-LAP-001',1),
(8,2190,'2026-11-09 09:56:22','Boîtier CNC tout aluminium, montage Gasket, switches tactiles lubrifiés Hot-Swap, connexion Bluetooth 5.1 multi-appareils et firmware QMK/VIA.',1,'data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCA0MDAgMjYwJz48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9J2JnR3JhZCcgeDE9JzAlJyB5MT0nMCUnIHgyPScxMDAlJyB5Mj0nMTAwJSc+PHN0b3Agb2Zmc2V0PScwJScgc3RvcC1jb2xvcj0nIzFlMjkzYicvPjxzdG9wIG9mZnNldD0nMTAwJScgc3RvcC1jb2xvcj0nIzYzNjZmMScvPjwvbGluZWFyR3JhZGllbnQ+PGxpbmVhckdyYWRpZW50IGlkPSdzY3JlZW5HcmFkJyB4MT0nMCUnIHkxPScwJScgeDI9JzEwMCUnIHkyPScxMDAlJz48c3RvcCBvZmZzZXQ9JzAlJyBzdG9wLWNvbG9yPScjMzhiZGY4Jy8+PHN0b3Agb2Zmc2V0PScxMDAlJyBzdG9wLWNvbG9yPScjNjM2NmYxJy8+PC9saW5lYXJHcmFkaWVudD48L2RlZnM+PHJlY3Qgd2lkdGg9JzQwMCcgaGVpZ2h0PScyNjAnIGZpbGw9J3VybCgjYmdHcmFkKScvPjxjaXJjbGUgY3g9JzM0MCcgY3k9JzQwJyByPSc5MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA4KScvPjxjaXJjbGUgY3g9JzUwJyBjeT0nMjMwJyByPSc4MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA2KScvPjxyZWN0IHg9Jzk1JyB5PSc5Nicgd2lkdGg9JzIxMCcgaGVpZ2h0PSc5Micgcng9JzQyJyBmaWxsPScjMGYxNzJhJyBzdHJva2U9JyNlMmU4ZjAnIHN0cm9rZS13aWR0aD0nNCcvPjxwYXRoIGQ9J00xNDUgMTI0IFYxNjAgTTEyNyAxNDIgSDE2Mycgc3Ryb2tlPScjMzhiZGY4JyBzdHJva2Utd2lkdGg9JzgnIHN0cm9rZS1saW5lY2FwPSdyb3VuZCcvPjxjaXJjbGUgY3g9JzI1MicgY3k9JzEyOCcgcj0nOCcgZmlsbD0nI2ZiNzE4NScvPjxjaXJjbGUgY3g9JzI3MicgY3k9JzE0Micgcj0nOCcgZmlsbD0nIzM0ZDM5OScvPjxjaXJjbGUgY3g9JzIzMicgY3k9JzE0Micgcj0nOCcgZmlsbD0nI2ZiYmYyNCcvPjxjaXJjbGUgY3g9JzI1MicgY3k9JzE1Nicgcj0nOCcgZmlsbD0nIzgxOGNmOCcvPjxyZWN0IHg9JzEyMCcgeT0nMjE4JyB3aWR0aD0nMTYwJyBoZWlnaHQ9JzI2JyByeD0nMTMnIGZpbGw9J3JnYmEoMTUsMjMsNDIsMC41NSknLz48dGV4dCB4PScyMDAnIHk9JzIzNScgZm9udC1mYW1pbHk9J1NlZ29lIFVJLCBBcmlhbCwgc2Fucy1zZXJpZicgZm9udC1zaXplPScxMicgZm9udC13ZWlnaHQ9JzcwMCcgZmlsbD0nI2ZmZmZmZicgdGV4dC1hbmNob3I9J21pZGRsZScgbGV0dGVyLXNwYWNpbmc9JzEnPktFWUNIUk9OIFExIFBSTzwvdGV4dD48L3N2Zz4=','Clavier Mécanique Keychron Q1 Pro Sans Fil','Keychron',1890,20,'PRD-GAM-001',2),
(9,1290,'2026-11-09 09:56:22','Capteur optique Darkfield 8000 DPI sur verre, molette électromagnétique MagSpeed ultra-rapide et clics silencieux ergonomiques pour développeurs.',0,'data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCA0MDAgMjYwJz48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9J2JnR3JhZCcgeDE9JzAlJyB5MT0nMCUnIHgyPScxMDAlJyB5Mj0nMTAwJSc+PHN0b3Agb2Zmc2V0PScwJScgc3RvcC1jb2xvcj0nIzBmMTcyYScvPjxzdG9wIG9mZnNldD0nMTAwJScgc3RvcC1jb2xvcj0nIzBkOTQ4OCcvPjwvbGluZWFyR3JhZGllbnQ+PGxpbmVhckdyYWRpZW50IGlkPSdzY3JlZW5HcmFkJyB4MT0nMCUnIHkxPScwJScgeDI9JzEwMCUnIHkyPScxMDAlJz48c3RvcCBvZmZzZXQ9JzAlJyBzdG9wLWNvbG9yPScjMzhiZGY4Jy8+PHN0b3Agb2Zmc2V0PScxMDAlJyBzdG9wLWNvbG9yPScjNjM2NmYxJy8+PC9saW5lYXJHcmFkaWVudD48L2RlZnM+PHJlY3Qgd2lkdGg9JzQwMCcgaGVpZ2h0PScyNjAnIGZpbGw9J3VybCgjYmdHcmFkKScvPjxjaXJjbGUgY3g9JzM0MCcgY3k9JzQwJyByPSc5MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA4KScvPjxjaXJjbGUgY3g9JzUwJyBjeT0nMjMwJyByPSc4MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA2KScvPjxyZWN0IHg9Jzk1JyB5PSc5Nicgd2lkdGg9JzIxMCcgaGVpZ2h0PSc5Micgcng9JzQyJyBmaWxsPScjMGYxNzJhJyBzdHJva2U9JyNlMmU4ZjAnIHN0cm9rZS13aWR0aD0nNCcvPjxwYXRoIGQ9J00xNDUgMTI0IFYxNjAgTTEyNyAxNDIgSDE2Mycgc3Ryb2tlPScjMzhiZGY4JyBzdHJva2Utd2lkdGg9JzgnIHN0cm9rZS1saW5lY2FwPSdyb3VuZCcvPjxjaXJjbGUgY3g9JzI1MicgY3k9JzEyOCcgcj0nOCcgZmlsbD0nI2ZiNzE4NScvPjxjaXJjbGUgY3g9JzI3MicgY3k9JzE0Micgcj0nOCcgZmlsbD0nIzM0ZDM5OScvPjxjaXJjbGUgY3g9JzIzMicgY3k9JzE0Micgcj0nOCcgZmlsbD0nI2ZiYmYyNCcvPjxjaXJjbGUgY3g9JzI1MicgY3k9JzE1Nicgcj0nOCcgZmlsbD0nIzgxOGNmOCcvPjxyZWN0IHg9JzEyMCcgeT0nMjE4JyB3aWR0aD0nMTYwJyBoZWlnaHQ9JzI2JyByeD0nMTMnIGZpbGw9J3JnYmEoMTUsMjMsNDIsMC41NSknLz48dGV4dCB4PScyMDAnIHk9JzIzNScgZm9udC1mYW1pbHk9J1NlZ29lIFVJLCBBcmlhbCwgc2Fucy1zZXJpZicgZm9udC1zaXplPScxMicgZm9udC13ZWlnaHQ9JzcwMCcgZmlsbD0nI2ZmZmZmZicgdGV4dC1hbmNob3I9J21pZGRsZScgbGV0dGVyLXNwYWNpbmc9JzEnPk1YIE1BU1RFUiAzUzwvdGV4dD48L3N2Zz4=','Souris Logitech MX Master 3S Sans Fil','Logitech',1090,25,'PRD-GAM-002',2),
(10,NULL,'2026-11-09 09:56:22','Écran OLED Nebula 240 Hz, Intel Core Ultra 9, NVIDIA GeForce RTX 4070 8 Go GDDR6, 32 Go RAM — conçu pour l\'IA, la 3D et le gaming AAA.',0,'data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCA0MDAgMjYwJz48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9J2JnR3JhZCcgeDE9JzAlJyB5MT0nMCUnIHgyPScxMDAlJyB5Mj0nMTAwJSc+PHN0b3Agb2Zmc2V0PScwJScgc3RvcC1jb2xvcj0nIzMxMTA0MicvPjxzdG9wIG9mZnNldD0nMTAwJScgc3RvcC1jb2xvcj0nI2JlMTIzYycvPjwvbGluZWFyR3JhZGllbnQ+PGxpbmVhckdyYWRpZW50IGlkPSdzY3JlZW5HcmFkJyB4MT0nMCUnIHkxPScwJScgeDI9JzEwMCUnIHkyPScxMDAlJz48c3RvcCBvZmZzZXQ9JzAlJyBzdG9wLWNvbG9yPScjMzhiZGY4Jy8+PHN0b3Agb2Zmc2V0PScxMDAlJyBzdG9wLWNvbG9yPScjNjM2NmYxJy8+PC9saW5lYXJHcmFkaWVudD48L2RlZnM+PHJlY3Qgd2lkdGg9JzQwMCcgaGVpZ2h0PScyNjAnIGZpbGw9J3VybCgjYmdHcmFkKScvPjxjaXJjbGUgY3g9JzM0MCcgY3k9JzQwJyByPSc5MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA4KScvPjxjaXJjbGUgY3g9JzUwJyBjeT0nMjMwJyByPSc4MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA2KScvPjxyZWN0IHg9JzExMCcgeT0nNzInIHdpZHRoPScxODAnIGhlaWdodD0nMTE0JyByeD0nMTAnIGZpbGw9JyMwZjE3MmEnIHN0cm9rZT0nI2UyZThmMCcgc3Ryb2tlLXdpZHRoPSc0Jy8+PHJlY3QgeD0nMTIyJyB5PSc4NCcgd2lkdGg9JzE1NicgaGVpZ2h0PSc5MCcgcng9JzQnIGZpbGw9J3VybCgjc2NyZWVuR3JhZCknLz48cGF0aCBkPSdNODYgMTg4IEgzMTQgTDI5OCAyMDQgSDEwMiBaJyBmaWxsPScjY2JkNWUxJy8+PHJlY3QgeD0nMTc2JyB5PScxODgnIHdpZHRoPSc0OCcgaGVpZ2h0PSc1JyByeD0nMicgZmlsbD0nIzY0NzQ4YicvPjxyZWN0IHg9JzEyMCcgeT0nMjE4JyB3aWR0aD0nMTYwJyBoZWlnaHQ9JzI2JyByeD0nMTMnIGZpbGw9J3JnYmEoMTUsMjMsNDIsMC41NSknLz48dGV4dCB4PScyMDAnIHk9JzIzNScgZm9udC1mYW1pbHk9J1NlZ29lIFVJLCBBcmlhbCwgc2Fucy1zZXJpZicgZm9udC1zaXplPScxMicgZm9udC13ZWlnaHQ9JzcwMCcgZmlsbD0nI2ZmZmZmZicgdGV4dC1hbmNob3I9J21pZGRsZScgbGV0dGVyLXNwYWNpbmc9JzEnPlJPRyBSVFggNDA3MDwvdGV4dD48L3N2Zz4=','ASUS ROG Zephyrus G16 RTX 4070','ASUS',22900,5,'PRD-LAP-003',1),
(11,4190,'2026-11-09 09:56:22','Référence mondiale de l\'ANC avec 8 microphones, autonomie 30 heures, charge rapide USB-PD (3 min = 3h d\'écoute) et audio haute résolution LDAC.',1,'data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCA0MDAgMjYwJz48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9J2JnR3JhZCcgeDE9JzAlJyB5MT0nMCUnIHgyPScxMDAlJyB5Mj0nMTAwJSc+PHN0b3Agb2Zmc2V0PScwJScgc3RvcC1jb2xvcj0nIzE4MTgxYicvPjxzdG9wIG9mZnNldD0nMTAwJScgc3RvcC1jb2xvcj0nIzRmNDZlNScvPjwvbGluZWFyR3JhZGllbnQ+PGxpbmVhckdyYWRpZW50IGlkPSdzY3JlZW5HcmFkJyB4MT0nMCUnIHkxPScwJScgeDI9JzEwMCUnIHkyPScxMDAlJz48c3RvcCBvZmZzZXQ9JzAlJyBzdG9wLWNvbG9yPScjMzhiZGY4Jy8+PHN0b3Agb2Zmc2V0PScxMDAlJyBzdG9wLWNvbG9yPScjNjM2NmYxJy8+PC9saW5lYXJHcmFkaWVudD48L2RlZnM+PHJlY3Qgd2lkdGg9JzQwMCcgaGVpZ2h0PScyNjAnIGZpbGw9J3VybCgjYmdHcmFkKScvPjxjaXJjbGUgY3g9JzM0MCcgY3k9JzQwJyByPSc5MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA4KScvPjxjaXJjbGUgY3g9JzUwJyBjeT0nMjMwJyByPSc4MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA2KScvPjxwYXRoIGQ9J00xMzIgMTU2IFYxMzIgQTY4IDY4IDAgMCAxIDI2OCAxMzIgVjE1NicgZmlsbD0nbm9uZScgc3Ryb2tlPScjZTJlOGYwJyBzdHJva2Utd2lkdGg9JzEyJyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48cmVjdCB4PScxMTQnIHk9JzEzOCcgd2lkdGg9JzM0JyBoZWlnaHQ9JzYyJyByeD0nMTYnIGZpbGw9JyMwZjE3MmEnIHN0cm9rZT0nIzM4YmRmOCcgc3Ryb2tlLXdpZHRoPSc0Jy8+PHJlY3QgeD0nMjUyJyB5PScxMzgnIHdpZHRoPSczNCcgaGVpZ2h0PSc2Micgcng9JzE2JyBmaWxsPScjMGYxNzJhJyBzdHJva2U9JyMzOGJkZjgnIHN0cm9rZS13aWR0aD0nNCcvPjxjaXJjbGUgY3g9JzIwMCcgY3k9JzE1MCcgcj0nMjAnIGZpbGw9J3JnYmEoMjU1LDI1NSwyNTUsMC4xMiknLz48cmVjdCB4PScxMjAnIHk9JzIxOCcgd2lkdGg9JzE2MCcgaGVpZ2h0PScyNicgcng9JzEzJyBmaWxsPSdyZ2JhKDE1LDIzLDQyLDAuNTUpJy8+PHRleHQgeD0nMjAwJyB5PScyMzUnIGZvbnQtZmFtaWx5PSdTZWdvZSBVSSwgQXJpYWwsIHNhbnMtc2VyaWYnIGZvbnQtc2l6ZT0nMTInIGZvbnQtd2VpZ2h0PSc3MDAnIGZpbGw9JyNmZmZmZmYnIHRleHQtYW5jaG9yPSdtaWRkbGUnIGxldHRlci1zcGFjaW5nPScxJz5XSC0xMDAwWE01PC90ZXh0Pjwvc3ZnPg==','Casque Sony WH-1000XM5 Réduction de Bruit','Sony',3690,17,'PRD-AUD-001',4),
(12,2290,'2026-11-09 09:56:22','Vitesses séquentielles fulgurantes jusqu\'à 7450 Mo/s en lecture et 6900 Mo/s en écriture PCIe 4.0, contrôleur nickelé haute efficacité thermique.',0,'data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHZpZXdCb3g9JzAgMCA0MDAgMjYwJz48ZGVmcz48bGluZWFyR3JhZGllbnQgaWQ9J2JnR3JhZCcgeDE9JzAlJyB5MT0nMCUnIHgyPScxMDAlJyB5Mj0nMTAwJSc+PHN0b3Agb2Zmc2V0PScwJScgc3RvcC1jb2xvcj0nIzFlMWI0YicvPjxzdG9wIG9mZnNldD0nMTAwJScgc3RvcC1jb2xvcj0nI2I5MWMxYycvPjwvbGluZWFyR3JhZGllbnQ+PGxpbmVhckdyYWRpZW50IGlkPSdzY3JlZW5HcmFkJyB4MT0nMCUnIHkxPScwJScgeDI9JzEwMCUnIHkyPScxMDAlJz48c3RvcCBvZmZzZXQ9JzAlJyBzdG9wLWNvbG9yPScjMzhiZGY4Jy8+PHN0b3Agb2Zmc2V0PScxMDAlJyBzdG9wLWNvbG9yPScjNjM2NmYxJy8+PC9saW5lYXJHcmFkaWVudD48L2RlZnM+PHJlY3Qgd2lkdGg9JzQwMCcgaGVpZ2h0PScyNjAnIGZpbGw9J3VybCgjYmdHcmFkKScvPjxjaXJjbGUgY3g9JzM0MCcgY3k9JzQwJyByPSc5MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA4KScvPjxjaXJjbGUgY3g9JzUwJyBjeT0nMjMwJyByPSc4MCcgZmlsbD0ncmdiYSgyNTUsMjU1LDI1NSwwLjA2KScvPjxyZWN0IHg9JzExNicgeT0nNzgnIHdpZHRoPScxNjgnIGhlaWdodD0nMTIwJyByeD0nMTYnIGZpbGw9JyMwZjE3MmEnIHN0cm9rZT0nI2UyZThmMCcgc3Ryb2tlLXdpZHRoPSc0Jy8+PHJlY3QgeD0nMTM2JyB5PScxMDAnIHdpZHRoPScxMjgnIGhlaWdodD0nMjInIHJ4PSc2JyBmaWxsPSd1cmwoI3NjcmVlbkdyYWQpJy8+PGNpcmNsZSBjeD0nMTUyJyBjeT0nMTU2JyByPSc4JyBmaWxsPScjMzRkMzk5Jy8+PGNpcmNsZSBjeD0nMTc4JyBjeT0nMTU2JyByPSc4JyBmaWxsPScjMzhiZGY4Jy8+PHJlY3QgeD0nMjA0JyB5PScxNTAnIHdpZHRoPSc1NicgaGVpZ2h0PScxMicgcng9JzYnIGZpbGw9JyM0NzU1NjknLz48cmVjdCB4PScxMjAnIHk9JzIxOCcgd2lkdGg9JzE2MCcgaGVpZ2h0PScyNicgcng9JzEzJyBmaWxsPSdyZ2JhKDE1LDIzLDQyLDAuNTUpJy8+PHRleHQgeD0nMjAwJyB5PScyMzUnIGZvbnQtZmFtaWx5PSdTZWdvZSBVSSwgQXJpYWwsIHNhbnMtc2VyaWYnIGZvbnQtc2l6ZT0nMTInIGZvbnQtd2VpZ2h0PSc3MDAnIGZpbGw9JyNmZmZmZmYnIHRleHQtYW5jaG9yPSdtaWRkbGUnIGxldHRlci1zcGFjaW5nPScxJz45OTAgUFJPIDJUQjwvdGV4dD48L3N2Zz4=','SSD Samsung 990 PRO NVMe M.2 - 2 To','Samsung',1950,16,'PRD-STK-001',5);
/*!40000 ALTER TABLE `produit` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;
SET AUTOCOMMIT=@OLD_AUTOCOMMIT;

--
-- Dumping routines for database 'ecommerce_jsf_jpa'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2026-11-09  9:57:29
