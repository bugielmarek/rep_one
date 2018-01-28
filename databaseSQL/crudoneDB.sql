-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: timetable
-- ------------------------------------------------------
-- Server version	5.7.19-log

create database if not exists crudoneDB;
use crudoneDB;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contacts` (
  `contact_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(60) DEFAULT NULL,
  `first_name` varchar(60) DEFAULT NULL,
  `last_name` varchar(60) DEFAULT NULL,
  `name` varchar(80) DEFAULT NULL,
  `phone_numbers` varchar(60) DEFAULT NULL,
  `text` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`contact_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacts`
--

LOCK TABLES `contacts` WRITE;
/*!40000 ALTER TABLE `contacts` DISABLE KEYS */;
INSERT INTO `contacts` (email, first_name, last_name, name, phone_numbers, text) VALUES
('aaa@com.pl','Abigail','Nowak',NULL,NULL,NULL),
('bbb@com.pl','Ada','Kowalska',NULL,NULL,NULL),
('ccc@com.pl','Adela','Kowalsky',NULL,NULL,NULL),
('ddd@com','Adelajda','Nowakowska',NULL,'132 4451','dzwonić pomiędzy 8-10'),
('eee@com.pl','Adelina','Nówak',NULL,'124 541 511',NULL),
('fff@com.pl','Adrianna','Nuwak',NULL,NULL,NULL),
('ggg@com.pl',NULL,NULL,'Sp. z o.o.','111-111-111',NULL),
('hhh@com.pl','Afodyta',NULL,NULL,NULL, NULL),
('iii@com.pl','Agata','Kówalka',NULL,'222-222-22',NULL),
('jjj@com.pl','Aida',NULL,NULL,NULL,NULL),
(NULL,'Agnieszka','Koowalska',NULL,'555-55-66',NULL),
(NULL,'Alana','Novak',NULL,'666-666-66',NULL);
/*!40000 ALTER TABLE `contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issues`
--

DROP TABLE IF EXISTS `issues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issues` (
  `issue_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `deadline` date NOT NULL,
  `text` longtext NOT NULL,
  `syg_number` varchar(10) DEFAULT NULL,
  `name` varchar(80) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `case_type` varchar(4) NOT NULL,
  PRIMARY KEY (`issue_id`),
  KEY `FKcigc16s3flsg53i2sy0m37e` (`user_id`),
  CONSTRAINT `FKcigc16s3flsg53i2sy0m37e` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issues`
--

LOCK TABLES `issues` WRITE;
/*!40000 ALTER TABLE `issues` DISABLE KEYS */;
INSERT INTO `issues` (deadline, text, syg_number, name, case_type) VALUES 
('2017-08-29','aaa','2015/15','bbb','KMS'),
('2018-02-18','opis i oszacowanie','1111/18','Kowalska','KM'),
('2018-02-11','licencja na XZY','265/18','Nowak','KMP'),
('2018-03-18','licytacja','92/18','Nowak','KMS'),
('2018-04-05','prokuratura','25/17','Nowakowska','KMP');
/*!40000 ALTER TABLE `issues` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payments` (
  `payment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `deadline` date NOT NULL,
  `name` varchar(80) NOT NULL,
  `syg_number` varchar(10) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `case_type` varchar(4) NOT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `FKj94hgy9v5fw1munb90tar2eje` (`user_id`),
  CONSTRAINT `FKj94hgy9v5fw1munb90tar2eje` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` (deadline, name, syg_number, user_id, case_type) VALUES 
('2018-02-14','Alana Kowalski','360/16',1,'KM'),
('2018-01-31','Albina Kowalski','567/17',1,'KMS'),
('2018-02-14','Aldona Kowalski','587/17',NULL,'KMP'),
('2018-02-09','Aleksandra Kowalski','14/15',4,'KM'),
('2018-01-31','Alicja Kowalski','4003/16',4,'KMP'),
('2018-01-31','Alina Kowalski','3214/15',4,'KM'),
('2018-02-16','Alma Kowalski','600/15',4,'KMS'),
('2018-03-08','Amanda Kowalski','12/17',2,'KM'),
('2018-01-30','Ambrozja Kowalski','761/15',2,'KMS'),
('2018-02-13','Amelia Kowalski','1484/14',2,'KM'),
('2018-02-15','Antastazja Kowalski','1111/11',2,'KMS'),
('2018-02-14','Anatola Nowak','928/15',NULL,'KM'),
('2018-02-01','Angela Nowak','2222/18',NULL,'KM'),
('2018-03-09','Aneta Nowak','4564/18',NULL,'KM'),
('2018-04-17','Aniela Nowak','3003/14',NULL,'KMP'),
('2018-05-14','Anita Nowakowska','2/12',NULL,'KMS'),
('2018-02-01','Firma xyz','22/18',NULL,'KM'),
('2018-03-09','Antonina Nowak','4564/18',NULL,'KM'),
('2018-04-17','Apolonina Nowak','3003/16',NULL,'KMP'),
('2018-05-14','Arabela Nowakowska','162/18',NULL,'KMS');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `received_case_files`
--

DROP TABLE IF EXISTS `received_case_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `received_case_files` (
  `received_case_file_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `arrived_at` date DEFAULT NULL,
  `case_type` varchar(255) NOT NULL,
  `name` varchar(80) DEFAULT NULL,
  `office` varchar(80) DEFAULT NULL,
  `syg_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`received_case_file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `received_case_files`
--

LOCK TABLES `received_case_files` WRITE;
/*!40000 ALTER TABLE `received_case_files` DISABLE KEYS */;
INSERT INTO `received_case_files` (arrived_at, case_type, name, office, syg_number) VALUES 
('2017-11-22','KMS','Areta Nowak','Komornik Katowice 1','360/16'),
('2017-10-27','KMP','Ariela Kowalsky','Komornika Warszawa 12','14/15'),
('2018-03-18','KM','Amelia Antastazja','Komornik Kowalsky','148/17'),
('2018-04-18','KM','Bohadana Bolesława','Komornik Nowacka','451/16'),
('2018-02-18','KM','Bożena Borzysława','Komornik Nowak','2011/16'),
('2018-03-18','KM','Sp. z o.o.','Komornik Kowalsky','148/17');
/*!40000 ALTER TABLE `received_case_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `authority` varchar(20) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(6) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (user_id, authority, enabled, password, username) VALUES 
(1,'ROLE_ADMIN',1,'68bfc5c1d754853b38c5ab1e80daeda56fa90f1d6b668171ab355eed76259942ccde4d72127685bb', 'Admini'),
(2,'ROLE_USER',1,'68bfc5c1d754853b38c5ab1e80daeda56fa90f1d6b668171ab355eed76259942ccde4d72127685bb', 'UzytkA'),
(3,'ROLE_USER',1,'68bfc5c1d754853b38c5ab1e80daeda56fa90f1d6b668171ab355eed76259942ccde4d72127685bb', 'UzytkB'),
(4,'ROLE_USER',1,'68bfc5c1d754853b38c5ab1e80daeda56fa90f1d6b668171ab355eed76259942ccde4d72127685bb', 'UzytkC');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'timetable'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-28  0:08:26
