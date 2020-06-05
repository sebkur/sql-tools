-- MySQL dump 10.13  Distrib 5.7.30, for Linux (x86_64)
--
-- Host: localhost    Database: testing-db
-- ------------------------------------------------------
-- Server version	5.7.30-0ubuntu0.16.04.1

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
-- Table structure for table `tblog`
--

DROP TABLE IF EXISTS `tblog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tblog` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `id_blog_reference` int(11) NOT NULL DEFAULT '0',
  `id_sprache` int(11) NOT NULL DEFAULT '1',
  `id_author` int(11) NOT NULL DEFAULT '0',
  `id_picture_teaser` int(11) NOT NULL DEFAULT '0',
  `filename` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `metatitle` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `metadesc` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `metakeys` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `teasertext` text COLLATE latin1_german2_ci,
  `csscode` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `htmlcode` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `datum` date DEFAULT NULL,
  `flag_visible` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=41 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblog`
--

LOCK TABLES `tblog` WRITE;
/*!40000 ALTER TABLE `tblog` DISABLE KEYS */;
INSERT INTO `tblog` VALUES (1,1,1,5,337,'foo','asdf');
INSERT INTO `tblog` VALUES (2,1,2,2,337,'bar','test');
/*!40000 ALTER TABLE `tblog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblog2personen`
--

DROP TABLE IF EXISTS `tblog2personen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tblog2personen` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_personen` int(11) NOT NULL DEFAULT '0',
  `id_tblog` int(11) NOT NULL DEFAULT '0',
  `flag_checked` tinyint(1) NOT NULL DEFAULT '0',
  `orderid` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblog2personen`
--

LOCK TABLES `tblog2personen` WRITE;
/*!40000 ALTER TABLE `tblog2personen` DISABLE KEYS */;
/*!40000 ALTER TABLE `tblog2personen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblog2templates`
--

DROP TABLE IF EXISTS `tblog2templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tblog2templates` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `id_blog` int(11) NOT NULL DEFAULT '0',
  `id_templates` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=127 DEFAULT CHARSET=latin1 COLLATE=latin1_german2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblog2templates`
--

LOCK TABLES `tblog2templates` WRITE;
/*!40000 ALTER TABLE `tblog2templates` DISABLE KEYS */;
INSERT INTO `tblog2templates` VALUES (1,2,9),(3,1,9),(4,3,10),(5,4,10),(6,5,8),(7,5,15),(8,6,7),(9,7,16),(10,7,15),(11,8,15),(12,8,16),(13,9,8),(14,10,7),(15,24,10),(16,24,66),(17,14,66),(105,14,50),(106,14,4),(20,14,23),(21,15,10),(22,16,23),(23,17,15),(24,22,10),(25,21,60),(26,20,67),(27,20,68),(28,14,70),(29,19,19),(30,18,26),(31,25,60),(32,26,61),(33,20,20),(34,27,61),(35,27,7),(36,27,20),(37,24,69),(38,1,56),(39,22,56),(40,33,60),(41,33,70),(42,33,66),(43,29,66),(44,29,69),(45,28,67),(46,28,60),(48,36,10),(49,35,7),(50,36,9),(51,35,15),(53,35,6),(54,35,14),(55,35,16),(56,35,17),(57,35,25),(58,35,55),(59,33,52),(60,33,31),(61,36,30),(62,29,52),(63,29,53),(64,29,41),(65,29,12),(66,29,74),(67,29,22),(68,29,54),(69,28,26),(70,28,55),(71,28,37),(72,28,32),(74,27,53),(75,26,56),(76,26,37),(77,25,55),(78,24,39),(79,15,47),(80,15,39),(81,16,54),(82,16,59),(83,16,63),(84,17,41),(85,17,30),(86,21,22),(87,21,62),(88,20,21),(89,20,58),(90,20,32),(91,19,25),(92,18,50),(93,18,68),(94,18,27),(121,39,55),(96,18,57),(97,18,58),(98,18,59),(99,18,62),(100,6,57),(101,5,21),(102,27,68),(103,35,47),(104,20,64),(107,14,27),(108,14,63),(109,14,64),(110,14,31),(111,7,4),(112,7,6),(113,7,8),(114,7,14),(115,38,11),(116,38,13),(117,38,12),(118,37,11),(119,37,13),(120,37,74),(122,39,60),(123,39,61),(124,40,55),(125,40,60),(126,40,61);
/*!40000 ALTER TABLE `tblog2templates` ENABLE KEYS */;
UNLOCK TABLES;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-30  8:39:55
