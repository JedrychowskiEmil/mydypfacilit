-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: localhost    Database: diploma_process_facilitator
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `department` varchar(100) NOT NULL,
  `field` varchar(100) DEFAULT NULL,
  `study_mode` varchar(50) DEFAULT NULL,
  `study_group` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'Wydział Architektury Budownictwa i Sztuk Stosowanych','Informatyka','Stacjonarnie','I'),(2,'Wydział Architektury Budownictwa i Sztuk Stosowanych','Informatyka','Zaocznie','II'),(3,'Wydział Architektury Budownictwa i Sztuk Stosowanych','Grafika','Zaocznie','I'),(4,'Wydział Architektury Budownictwa i Sztuk Stosowanych','Mechatronika','Zaocznie','II'),(8,'Wydział Nauk Medycznych','Pielegniarstwo','Stacjonarnie','III'),(9,'Wydział Nauk Medycznych','Kierunek lekarski','Stacjonarnie','I');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department_user`
--

DROP TABLE IF EXISTS `department_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department_user` (
  `user_id` int(11) NOT NULL,
  `department_id` int(11) DEFAULT NULL,
  KEY `FK_USER_DEP01` (`user_id`),
  KEY `FK_USER_DEP02` (`department_id`),
  CONSTRAINT `FK_USER_DEP01` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_USER_DEP02` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department_user`
--

LOCK TABLES `department_user` WRITE;
/*!40000 ALTER TABLE `department_user` DISABLE KEYS */;
INSERT INTO `department_user` VALUES (8,2),(7,1),(6,1),(4,1),(4,2),(5,1),(5,2),(11,1),(17,8),(18,8),(19,2),(20,2),(10,1),(9,8),(3,8),(21,3),(21,4),(22,8),(22,9);
/*!40000 ALTER TABLE `department_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diploma_topic`
--

DROP TABLE IF EXISTS `diploma_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diploma_topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) DEFAULT NULL,
  `promoter_id` int(11) DEFAULT NULL,
  `subject` varchar(50) NOT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `department_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_DIPLOMA_STUDENT` (`student_id`),
  KEY `FK_DIPLOMA_PROMOTER` (`promoter_id`),
  KEY `FK_DIPLOMA_STATUS` (`status`),
  KEY `FK_DIPLOMA_DEPARTMENT` (`department_id`),
  CONSTRAINT `FK_DIPLOMA_DEPARTMENT` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
  CONSTRAINT `FK_DIPLOMA_PROMOTER` FOREIGN KEY (`promoter_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_DIPLOMA_STATUS` FOREIGN KEY (`status`) REFERENCES `status` (`id`),
  CONSTRAINT `FK_DIPLOMA_STUDENT` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diploma_topic`
--

LOCK TABLES `diploma_topic` WRITE;
/*!40000 ALTER TABLE `diploma_topic` DISABLE KEYS */;
INSERT INTO `diploma_topic` VALUES (18,10,4,'Gra platformowa - \"Monkey run\"','Nowoczesna gra platformowa o nazwie \"Monkey run\". Gra utwrzona przy pomocy silnika Unity. Charakteryzowałaby się własnoręcznie utworzonymi modelami, dokładną dynamiką postaci oraz niebanalną fabułą.',4,1),(19,7,NULL,'Praktyczne wykorzystanie technologii VOIP','Projekt sieci telefonicznej wykorzystującej technologię VOIP.',2,1),(20,6,NULL,'Gra komputerowa z elementami RPG','Prosta gra komputerowa z elementami RPG utworzona w Unity.',3,1),(21,20,4,'Wizualizacja 3D efektu cieplarnianego','Dokładna wizualizacja 3D efektu cieplarnianego. Praca musi zawierać opis problemu, wykorzystane technologie oraz samą wizualizacje.',5,2),(22,8,5,'Integracja systemu bazodanowego z systemem CCTV','Tematem pracy dyplomowej jest integracja systemu bazodanowego z systemem CCTV na potrzeby Firmy \"Template Inc.\". Praca po za dokładnym opisem całego procesu zawierać będzie również teoretyczny opis samych systemów.',6,2),(23,19,5,'Projekt rozkładu sieci w budynku firmy budowlanej','Projekt rozkładu sieci w budynku firmy budowlanej \"BudexPole sp. z.o.o\". Po za samym projektem rozkładu sieci praca zawierać będzie opis kosztów sprzętu, dokumentacje z konfiguracji urządzeń oraz analize ryzyka samego procesu.',7,2),(24,9,3,'Opieka pielęgniarska w chorobach wewnętrznych','Opieka pielęgniarska w chorobach wewnętrznych w szpitalu publicznym.',6,8),(25,18,3,'Promocja zdrowia i edukacja zdrowotna.','Promocja zdrowia i edukacja zdrowotna.\r\n',9,8),(26,17,3,'Opieka nad pacjentem leczonym chirurgicznie','Opieka nad pacjentem leczonym chirurgicznie.',13,8),(27,NULL,4,'Porównanie charakterystyki systemów operacyjnych','Porównanie charakterystyki kilku najpopularniejszych systemów operacyjnych. Praca musi składać się z opisu teoretycznego każdego z opisywanych systemów, porównania systemów oraz zestawu wykresów,',5,1);
/*!40000 ALTER TABLE `diploma_topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL,
  `content` varchar(2000) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `is_important` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news`
--

LOCK TABLES `news` WRITE;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
INSERT INTO `news` VALUES (3,'TERMINY SKŁADANIA PRAC DYPLOMOWYCH','Pierwsza sesja dyplomowania <br>\r\nWszystkie kierunki do 14.02.2021 r. <br>\r\nDruga sesja dyplomowania <br>\r\nInformatyka – do 10.04.2021 r. <br>\r\nPozostałe kierunki - do 12.04.2021 r.','2020-10-11',1),(4,'OPŁATA ZA WYSTAWIENIE DYPPLOMU W JĘZYKU ANGIELSKIM','Aby otrzymać kopię dyplomu w języku angielskim należy uiścić opłatę w wysokości 50zł oraz przedstawić dowód wpłaty w dziekanacie. ','2020-10-12',0),(5,'ZASADY DYPLOMOWANIA','Listę zasad dyplomowania można znaleźć na oficjalnej stronie uczelni.','2020-06-15',0),(6,'TERMINY OBRON 2020/2021','Obrony prac dyplomowych będą odbywać się pomiędzy 09.11.2020 a 19.03.2021.\r\nDokładna terminarz obron dostępny jest na oficjalnej stronie uczelni.\r\n\r\nWszystkie osoby zobowiązane są do stawienia się minimum 30 minut przed planowanym rozpoczęciem obrony.','2020-12-03',0);
/*!40000 ALTER TABLE `news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ROLE_STUDENT'),(2,'ROLE_PROMOTER'),(3,'ROLE_ADMIN');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (1,'Nie wybrano tematu pracy'),(2,'Brak promotora'),(3,'Temat odrzucono'),(4,'Zaproponowano temat'),(5,'Aplikowano o Temat promotora'),(6,'W trakcie pisania pracy'),(7,'Wymaga poprawy'),(8,'Praca zatwierdzona przez promotora'),(9,'Praca przyjęta i zatwierdzona'),(13,'Niestandardowy status pracy');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `password` char(80) NOT NULL,
  `academic_title` varchar(30) DEFAULT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'a@a','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'Jan','Kowalski'),(3,'was.l@email.net','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','dr','Lech','Wąs'),(4,'mucha.katarzyna@email.com','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','prof','Katarzyna','Mucha'),(5,'bajorm@email.com','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','prof','Michał','Bajor'),(6,'kot.e@email.com','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'Emilia','Kot'),(7,'bgodan.n@email.com','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'Bogdan','Niemaszka'),(8,'j.k@email.com','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'Janusz','Kowalski'),(9,'a.wisniewska@email.com','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'Anna','Wiśniewska'),(10,'jerzy.kaszubik@email.pl','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'Jerzy','Kaszubik'),(11,'a.nowak@email.com','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'Adam','Nowak'),(17,'michal.krzak@email.pl','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'Michał','Krzakowski'),(18,'grzechot.m@email.com','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'Magdalena','Grzechot'),(19,'jm91@email.com','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'Jerzy','Maćkiewicz'),(20,'k.dominik@email.com','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',NULL,'Dominik','Kieniek'),(21,'kisc.jan@email.com','$2a$10$PKD3T8wIDWvL1.qhx4aF9.cV2KhbIQfNFRhTEAEvVd4Gi5q3E7.0e','mgr inż.','Jan','Kiść'),(22,'kowalska.k@email.com','$2a$10$jWWByVzjBAeVfNW05BL6UOjkDWlL4iKJhCxQMefwAreRJr.QyT72u','dr','Katarzyna','Kowalska');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK_ROLE_idx` (`role_id`),
  CONSTRAINT `FK_ROLE` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_USER_05` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (6,1),(7,1),(8,1),(9,1),(10,1),(11,1),(17,1),(18,1),(19,1),(20,1),(3,2),(4,2),(5,2),(21,2),(22,2),(1,3);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-06 22:53:36
