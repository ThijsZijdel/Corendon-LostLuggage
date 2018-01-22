-- Host: corendon    Database: corendonlostluggage
-- Author: Thijs Zijdel & IS103-3
-- ------------------------------------------------------
-- Server version	5.7.20-log


--
-- Create the DB
--
CREATE DATABASE  IF NOT EXISTS `corendonlostluggage` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `corendonlostluggage`;


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
-- Table structure for table `claim`
--

DROP TABLE IF EXISTS `claim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `claim` (
  `idclaim` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `dateOfClaim` date NOT NULL,
  `description` varchar(200) NOT NULL,
  `lostform` int(11) NOT NULL,
  `foundform` int(11) NOT NULL,
  `employee` varchar(45) NOT NULL,
  PRIMARY KEY (`idclaim`,`dateOfClaim`),
  KEY `claim processed by employee_idx` (`employee`),
  KEY `found form_idx` (`foundform`),
  KEY `registration of lost form_idx` (`lostform`),
  CONSTRAINT `claim processed by employee` FOREIGN KEY (`employee`) REFERENCES `employee` (`employeeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `registration of found form` FOREIGN KEY (`foundform`) REFERENCES `foundluggage` (`registrationNr`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `registration of lost form` FOREIGN KEY (`lostform`) REFERENCES `lostluggage` (`registrationNr`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `claim`
--

LOCK TABLES `claim` WRITE;
/*!40000 ALTER TABLE `claim` DISABLE KEYS */;
/*!40000 ALTER TABLE `claim` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `color`
--

DROP TABLE IF EXISTS `color`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `color` (
  `ralCode` int(11) NOT NULL,
  `english` varchar(45) NOT NULL,
  `dutch` varchar(45) NOT NULL,
  PRIMARY KEY (`ralCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `color`
--

LOCK TABLES `color` WRITE;
/*!40000 ALTER TABLE `color` DISABLE KEYS */;
INSERT INTO `color` VALUES (1003,'Yellow','Geel'),(1015,'Cream','Crème'),(1024,'Olive','Olijf'),(2004,'Orange','Oranje'),(3000,'Red','Rood'),(3005,'Darkred','Donkerrood'),(3017,'Pink','Roze'),(4005,'Purple','Paars'),(4010,'Violet','Violet'),(5002,'Blue','Blauw'),(5015,'Lightblue','Lichtblauw'),(5022,'Darkblue','Donkerblauw'),(6002,'Green','Groen'),(6004,'Bluegreen','Blauwgroen'),(6022,'Darkgreen','Donkergroen'),(6038,'Lightgreen','Lichtgroen'),(7000,'Lightgray','Lichtgrijs'),(7015,'Gray','Grijs'),(8002,'Brown','Bruin'),(8011,'Darkbrown','Donkerbruin'),(8023,'Lightbrown','Lichtbruin'),(9001,'White','Wit'),(9005,'Black','Zwart'),(9011,'Darkgray','Donkergrijs');
/*!40000 ALTER TABLE `color` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `destination`
--

DROP TABLE IF EXISTS `destination`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `destination` (
  `IATAcode` varchar(45) NOT NULL,
  `airport` varchar(45) NOT NULL,
  `country` varchar(45) NOT NULL,
  `timeZone` varchar(45) NOT NULL,
  `daylightSaving` tinyint(4) NOT NULL,
  PRIMARY KEY (`IATAcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `destination`
--

LOCK TABLES `destination` WRITE;
/*!40000 ALTER TABLE `destination` DISABLE KEYS */;
INSERT INTO `destination` VALUES ('ACE','Lanzarote','Spain','0',1),('ADB','Izmir','Turkey','3',0),('AGA','Agadir','Morocco','0',0),('AGP','Malaga','Spain','0',1),('AMS','Amsterdam','The Netherlands','1',1),('AYT','Antalya','Turkey','3',0),('BJL','Banjul','Gambia','0',0),('BJV','Bodrum','Turkey','3',0),('BOJ','Burgas','Bulgaria','2',1),('BRU','Brussels','Belgium','1',1),('CFU','Corfu','Greece','2',1),('CTA','Sicily (Catania)','Italy','1',1),('DLM','Dalaman','Turkey','3',0),('DXB','Dubai','United Arab Emirates','4',0),('ECN','Nicosia-Ercan','Cyprus (North)','2',1),('EIN','Eindhoven','The Netherlands','1',1),('FAO','Faro','Portugal','0',1),('FUE','Fuerteventura','Spain','0',1),('GZP','Gazipasa-Alanya','Turkey','3',0),('HER','Heraklion','Greece','2',1),('HRG','Hurghada','Egypt','2',0),('IST','Istanbul','Turkey','3',0),('KGS','Kos','Greece','2',1),('LPA','Gran Canaria','Spain','0',1),('MJT','Mytilene','Greece','2',1),('NBE','Enfidha','Tunesië','1',0),('OHD','Ohrid','Macedonia','1',1),('PAR','Paris','France','1',1),('PMI','Palma de Mallorca','Spain','0',1),('RAK','Marrakech','Morocco','0',1),('RHO','Rodes','Greece','2',1),('SMI','Samos','Greece','2',1),('TFO','Tenerife','Spain','0',1),('ZTH','Zakynthos','Greece','2',1);
/*!40000 ALTER TABLE `destination` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `employeeId` varchar(45) NOT NULL,
  `firstname` varchar(45) NOT NULL,
  `lastname` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `role` varchar(45) NOT NULL,
  `location` varchar(45) NOT NULL,
  `status` varchar(45) NOT NULL,
  PRIMARY KEY (`employeeId`),
  UNIQUE KEY `employeeId_UNIQUE` (`employeeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES ('AA1','Ahmet','Aksu','poow','Manager','BJL','Inactive'),('AK1','arthur','krom','asd','Administrator','AMS','Active'),('BJ1','Bram','Janssen','asd','Service','AMS','Active'),('DJ1','Daan','Jong, de','asd','Service','AMS','Active'),('DO1','Daron','Ozdemir','ppok','Manager','BOJ','Active'),('LD1','Lucas','Dijkstra','asd','Service','AMS','Active'),('MB1','Michael','Boer, de','ajax','Administrator','Amsterdam','Active'),('MS1','Milan','Smit','asd','Service','AMS','Active'),('PL1','poek','ligthart','kwiik','Service','CFU','Active'),('SM1','Sem','Mulder','asd','Service','AMS','Active'),('TZ1','Thijs','Zijdel','asd','Service','AMS','Active');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flight`
--

DROP TABLE IF EXISTS `flight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flight` (
  `flightNr` varchar(45) NOT NULL,
  `airline` varchar(45) NOT NULL,
  `from` varchar(45) NOT NULL,
  `to` varchar(45) NOT NULL,
  PRIMARY KEY (`flightNr`),
  KEY `this flight leaves from_idx` (`from`),
  KEY `this flight arrives at_idx` (`to`),
  CONSTRAINT `this flight arrives at` FOREIGN KEY (`to`) REFERENCES `destination` (`IATAcode`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `this flight leaves from` FOREIGN KEY (`from`) REFERENCES `destination` (`IATAcode`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flight`
--

LOCK TABLES `flight` WRITE;
/*!40000 ALTER TABLE `flight` DISABLE KEYS */;
INSERT INTO `flight` VALUES ('CAI020','Corendon','AMS','AYT'),('CAI021','Corendon','AYT','AMS'),('CAI023','Corendon','AYT','AMS'),('CAI040','Corendon','EIN','AYT'),('CAI041','Corendon','AYT','EIN'),('CAI1827','Corendon','AYT','BRU'),('CAI1828','Corendon','BRU','AYT'),('CAI201','Corendon','BJV','AMS'),('CAI202','Corendon','AMS','BJV'),('CAI421','Corendon','AYT','BRU'),('CAI524','Corendon','BRU','AYT'),('CAI723','Corendon','AYT','BRU'),('CAI724','Corendon','BRU','AYT'),('CAI805','Corendon','AYT','AMS'),('CAI806','Corendon','AMS','AYT'),('CND117','Corendon','AMS','AGP'),('CND118','Corendon','AGP','AMS'),('CND513','Corendon','AMS','HER'),('CND593','Corendon','AMS','AGP'),('CND594','Corendon','AGP','AMS'),('CND712','Corendon','HER','AMS'),('HV355','Transavia','AMS','BJV'),('HV356','Transavia','BJV','AMS'),('HV6115','Transavia','AMS','AGP'),('HV6224','Transavia','AGP','AMS'),('HV649','Transavia','AMS','AYT'),('HV650','Transavia','AYT','AMS'),('HV740','Transavia','AYT','AMS'),('HV799','Transavia','AMS','AYT'),('KL1039','KLM','AMS','AGP'),('KL1040','KLM','AGP','AMS'),('KL1041','KLM','AMS','AGP'),('KL1042','KLM','AGP','AMS'),('PC5665','Pegasus','AYT','AMS'),('TK1823','Turkish Airlines','IST','PAR'),('TK1824','Turkish Airlines','PAR','IST'),('TK1827','Turkish Airlines','IST','PAR'),('TK1830','Turkish Airlines','PAR','IST'),('TK1938','Turkish Airlines','BRU','IST'),('TK1939','Turkish Airlines','IST','BRU'),('TK1942','Turkish Airlines','BRU','IST'),('TK1943','Turkish Airlines','IST','BRU'),('TK1951','Turkish Airlines','IST','AMS'),('TK1952','Turkish Airlines','AMS','IST'),('TK1955','Turkish Airlines','IST','AMS'),('TK1958','Turkish Airlines','AMS','IST'),('TK2409','Turkish Airlines','AYT','IST'),('TK2414','Turkish Airlines','IST','AYT'),('TK2425','Turkish Airlines','AYT','IST'),('TK2430','Turkish Airlines','IST','AYT'),('TK2505','Turkish Airlines','BJV','IST'),('TK2510','Turkish Airlines','IST','BJV'),('TO3002','Transavia','PAR','AGA'),('TO3005','Transavia','AGA','PAR'),('TO3160','Transavia','PAR','AGP'),('TO3163','Transavia','AGP','PAR'),('VY2150','Vueling','AGP','BRU'),('VY2151','Vueling','BRU','AGP');
/*!40000 ALTER TABLE `flight` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `foundluggage`
--

DROP TABLE IF EXISTS `foundluggage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `foundluggage` (
  `registrationNr` int(11) NOT NULL AUTO_INCREMENT,
  `dateFound` date NOT NULL,
  `timeFound` time NOT NULL,
  `luggageTag` varchar(45) DEFAULT NULL,
  `luggageType` int(11) NOT NULL,
  `brand` varchar(45) DEFAULT NULL,
  `mainColor` int(11) NOT NULL,
  `secondColor` int(11) DEFAULT NULL,
  `size` varchar(45) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `otherCharacteristics` varchar(200) DEFAULT NULL,
  `arrivedWithFlight` varchar(45) DEFAULT NULL,
  `locationFound` int(11) DEFAULT NULL,
  `employeeId` varchar(45) DEFAULT NULL,
  `passengerId` int(11) DEFAULT NULL,
  `matchedId` int(11) DEFAULT NULL,
  `airport` varchar(45) DEFAULT NULL,
  `image` mediumblob,
  `destination` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`registrationNr`),
  UNIQUE KEY `registrationNr_UNIQUE` (`registrationNr`),
  KEY `the type of luggage_idx` (`luggageType`),
  KEY `the main color of found luggage_idx` (`mainColor`),
  KEY `the second color of found luggage_idx` (`secondColor`),
  KEY `arrived with flight_idx` (`arrivedWithFlight`),
  KEY `is found at location_idx` (`locationFound`),
  KEY `formulier has been submitted by_idx` (`employeeId`),
  KEY `Belongs to a passenger like this_idx` (`passengerId`),
  KEY `foundluggage matched to matched table_idx` (`matchedId`),
  KEY `found form filled in at_idx` (`airport`),
  KEY `destination of the found luggage_idx` (`destination`),
  CONSTRAINT `Belongs to a passenger like this` FOREIGN KEY (`passengerId`) REFERENCES `passenger` (`passengerId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `This luggage has this main color` FOREIGN KEY (`mainColor`) REFERENCES `color` (`ralCode`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `This luggage has this second color` FOREIGN KEY (`secondColor`) REFERENCES `color` (`ralCode`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `destination of the found luggage` FOREIGN KEY (`destination`) REFERENCES `destination` (`IATAcode`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `formulier has been submitted by` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`employeeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `found form filled in at` FOREIGN KEY (`airport`) REFERENCES `destination` (`IATAcode`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `foundluggage matched to matched table` FOREIGN KEY (`matchedId`) REFERENCES `matched` (`matchedId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `this lugage is of this type` FOREIGN KEY (`luggageType`) REFERENCES `luggagetype` (`luggageTypeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `this luggage has arrived with flight` FOREIGN KEY (`arrivedWithFlight`) REFERENCES `flight` (`flightNr`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `this luggage has been found at` FOREIGN KEY (`locationFound`) REFERENCES `location` (`locationId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=466 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `foundluggage`
--

LOCK TABLES `foundluggage` WRITE;
/*!40000 ALTER TABLE `foundluggage` DISABLE KEYS */;
INSERT INTO `foundluggage` VALUES (410,'2016-06-17','19:25:00','1153481443',4,'',1015,NULL,'',0,'',NULL,NULL,NULL,1,NULL,NULL,NULL,NULL),(411,'2016-09-07','10:04:00','1297047756',1,'Perry Mackin',6004,4010,'80x60x40',0,'holywood sticker',NULL,6,NULL,2,NULL,NULL,NULL,NULL),(412,'2016-07-04','20:05:00','1321391290',6,'Eastsport',3017,8011,'',0,'red-bull sticker','CAI020',1,NULL,3,NULL,NULL,NULL,NULL),(413,'2016-09-09','13:18:00','1557534916',5,'Baggallini',1024,3005,'60x30x30',15,'','CAI724',1,NULL,4,NULL,NULL,NULL,NULL),(414,'2015-11-25','12:00:00','1688722916',2,'Baggallini',9005,5002,'60x30x30',15,'Orange stripes','CAI1828',2,NULL,5,NULL,NULL,NULL,NULL),(415,'2016-09-10','16:30:00','1957629307',1,'Ivy',2004,NULL,'70x50x20',0,'',NULL,7,NULL,6,NULL,NULL,NULL,NULL),(416,'2016-09-09','11:56:00','1963627893',1,'Nautica',1003,6038,'80x60x30',20,'many scratches',NULL,7,NULL,7,NULL,NULL,NULL,NULL),(417,'2015-10-20','11:50:00','2771896151',6,'Ivy',5002,NULL,'50x40x15',10,'','TK2414',0,NULL,8,NULL,NULL,NULL,NULL),(418,'2016-09-08','11:29:00','2973839061',4,'',4010,NULL,'60x40x30',15,'chain lock','HV799',0,NULL,9,NULL,NULL,NULL,NULL),(419,'2016-08-23','07:30:00','3217712035',1,'Travel Gear',8023,1003,'100x60x40',30,'ajax stickers',NULL,NULL,NULL,10,NULL,NULL,NULL,NULL),(420,'2016-03-13','19:23:00','3260024106',3,'Hedgren',3000,1015,'',0,'football stickers','TK2430',1,NULL,11,NULL,NULL,NULL,NULL),(421,'2016-09-02','09:25:00','3299609395',5,'Fjallraven',8023,NULL,'60x30x30',0,'','HV799',2,NULL,NULL,NULL,NULL,NULL,NULL),(422,'2016-01-17','14:13:00','3794786696',1,'Glove It',4010,9001,'80x60x30',15,'','CAI724',5,NULL,12,NULL,NULL,NULL,NULL),(423,'2016-09-04','09:40:00','4497537549',6,'Glove It',4005,NULL,'50x40x15',10,'','CAI1828',0,NULL,13,NULL,NULL,NULL,NULL),(424,'2016-08-24','08:10:00','4811246270',2,'Fjallraven',7015,NULL,'50x40x15',10,'Orange stripes','HV649',3,NULL,14,NULL,NULL,NULL,NULL),(425,'2016-08-31','08:11:00','5364334705',3,'Travel Gear',9001,NULL,'60x30x30',15,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(426,'2016-07-19','21:05:00','5703242384',2,'Samsonite',8002,NULL,'',0,'Bicycle stickers',NULL,8,NULL,NULL,NULL,NULL,NULL,NULL),(427,'2016-08-11','23:00:00','5877130095',6,'Baggallini',5022,NULL,'',0,'red-bull sticker','CAI040',0,NULL,15,NULL,NULL,NULL,NULL),(428,'2016-07-25','22:00:00','5941005772',4,'',6038,NULL,'',0,'','CAI806',4,NULL,NULL,NULL,NULL,NULL,NULL),(429,'2016-07-15','20:35:00','5955243509',1,'Everest',5015,NULL,'60x40x20',15,'','TK2430',5,NULL,16,NULL,NULL,NULL,NULL),(430,'2016-08-06','22:15:00','6175011250',5,'Samsonite',8011,NULL,'',0,'',NULL,8,NULL,17,NULL,NULL,NULL,NULL),(431,'2016-09-08','10:17:00','6327958189',3,'Perry Mackin',6002,NULL,'60x30x30',10,'',NULL,6,NULL,NULL,NULL,NULL,NULL,NULL),(432,'2016-09-10','16:00:00','6377992003',6,'Everest',5015,3017,'50x40x15',10,'','CAI806',0,NULL,18,NULL,NULL,NULL,NULL),(433,'2016-06-18','19:40:00','6895742082',5,'Briggs',3005,NULL,'',0,'','HV649',1,NULL,NULL,NULL,NULL,NULL,NULL),(434,'2016-05-24','18:44:00','7620963089',1,'Hedgren',1015,NULL,'70x50x20',10,'','CAI806',2,NULL,19,NULL,NULL,NULL,NULL),(435,'2016-09-10','14:28:00','7686938228',2,'AmeriLeather',5022,7015,'60x30x30',10,'Olympic rings','HV799',4,NULL,NULL,NULL,NULL,NULL,NULL),(436,'2016-04-13','17:17:00','7975308223',2,'Delsey',5002,9005,'',0,'Olympic rings','CAI524',5,NULL,20,NULL,NULL,NULL,NULL),(437,'2016-09-10','15:50:00','9896064347',5,'AmeriLeather',8011,6004,'60x30x30',10,'BRT television sticker','CAI524',3,NULL,NULL,NULL,NULL,NULL,NULL),(438,'2016-09-01','09:10:00','',4,'',3017,NULL,'60x40x30',15,'','HV649',5,NULL,NULL,NULL,NULL,NULL,NULL),(439,'2016-09-06','09:58:00','',7,'',6038,4005,'100x60x40',30,'',NULL,8,NULL,NULL,NULL,NULL,NULL,NULL),(440,'2016-09-07','10:13:00','',2,'Eastsport',9011,1024,'50x40x15',0,'Orange stripes','CAI724',3,NULL,21,NULL,NULL,NULL,NULL),(441,'2016-09-08','11:43:00','',5,'Eastsport',9001,NULL,'60x30x30',10,'','CAI040',2,NULL,22,NULL,NULL,NULL,NULL),(442,'2016-09-09','11:54:00','',7,'',7015,6022,'80x60x40',25,'red name tag',NULL,8,NULL,NULL,NULL,NULL,NULL,NULL),(443,'2016-09-09','12:01:00','',3,'Nautica',1024,2004,'60x30x30',0,'',NULL,7,NULL,23,NULL,NULL,NULL,NULL),(444,'2015-12-25','12:04:00','',4,'',7000,3000,'60x40x30',0,'','TK2430',3,NULL,24,NULL,NULL,NULL,NULL),(445,'2016-09-09','13:21:00','',6,'Hedgren',8002,NULL,'50x40x15',10,'ajax stickers','CAI020',3,NULL,25,NULL,NULL,NULL,NULL),(446,'2016-09-10','13:37:00','',7,'',4005,8023,'80x60x30',0,'','CAI020',5,NULL,NULL,NULL,NULL,NULL,NULL),(447,'2016-09-10','15:31:00','',3,'Glove It',6004,5015,'60x30x30',10,'','CAI040',4,NULL,26,NULL,NULL,NULL,NULL),(448,'2016-09-10','15:40:00','',4,'',9011,5022,'',0,'frech national flag sticker',NULL,8,NULL,27,NULL,NULL,NULL,NULL),(449,'2016-02-10','16:22:00','',7,'',7000,NULL,'80x60x30',15,'',NULL,8,NULL,NULL,NULL,NULL,NULL,NULL),(450,'2016-04-27','17:34:00','',3,'Ivy',3000,9011,'',0,'',NULL,7,NULL,28,NULL,NULL,NULL,NULL),(451,'2016-04-30','17:44:00','',4,'',9005,7000,'',0,'broken lock',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(452,'2015-12-25','18:09:00','',5,'Delsey',6002,8002,'',0,'','CAI1828',4,NULL,29,NULL,NULL,NULL,NULL),(453,'2016-05-03','18:10:00','',6,'Fjallraven',2004,6002,'',0,'','CAI524',1,NULL,NULL,NULL,NULL,NULL,NULL),(454,'2016-05-14','18:37:00','',7,'',6022,NULL,'70x50x20',20,'duvel sticker',NULL,6,NULL,NULL,NULL,NULL,NULL,NULL),(455,'2016-06-04','19:20:00','',2,'Briggs',1003,NULL,'',0,'Olympic rings','TK2414',2,NULL,30,NULL,NULL,NULL,NULL),(456,'2016-07-09','20:18:00','',7,'',3005,NULL,'70x50x20',10,'',NULL,7,NULL,31,NULL,NULL,NULL,NULL),(457,'2016-08-17','21:45:00','',3,'Everest',6022,NULL,'',0,'','TK2414',4,NULL,32,NULL,NULL,NULL,NULL),(459,'2017-12-09','15:16:00','93930388',3,'Adidas',3017,4005,'30x30x30',5,'Black stripes','CAI023',2,'aa',41,NULL,NULL,NULL,NULL),(461,'2017-12-14','17:54:00','ookwieuuir3',5,'Puma',3005,4005,'30x30x30',5,'Yellow stars','CAI040',3,'aa',47,NULL,NULL,NULL,NULL),(464,'2018-01-11','13:10:00','aosdkoasd',3,'',2004,NULL,'',0,'','CAI023',NULL,'aa',NULL,NULL,NULL,NULL,NULL),(465,'2018-01-04','02:02:00','kweiod8',4,'',2004,NULL,'',0,'','CAI201',NULL,'BJ1',NULL,NULL,'AMS',NULL,NULL);
/*!40000 ALTER TABLE `foundluggage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `locationId` int(11) NOT NULL,
  `english` varchar(45) NOT NULL,
  `dutch` varchar(45) NOT NULL,
  PRIMARY KEY (`locationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
INSERT INTO `location` VALUES (0,'belt-06','band-06'),(1,'belt-05','band-05'),(2,'belt-04','band-04'),(3,'belt-03','band-03'),(4,'belt-02','band-02'),(5,'belt-01','band-01'),(6,'departure hall','vertrekhal'),(7,'arrival hall','aankomsthal'),(8,'toilet','toilet');
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lostluggage`
--

DROP TABLE IF EXISTS `lostluggage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lostluggage` (
  `registrationNr` int(11) NOT NULL AUTO_INCREMENT,
  `dateLost` date NOT NULL,
  `timeLost` time NOT NULL,
  `luggageTag` varchar(45) DEFAULT NULL,
  `luggageType` int(11) NOT NULL,
  `brand` varchar(45) DEFAULT NULL,
  `mainColor` int(11) NOT NULL,
  `secondColor` int(11) DEFAULT NULL,
  `size` varchar(45) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `otherCharacteristics` mediumtext,
  `flight` varchar(45) DEFAULT NULL,
  `employeeId` varchar(45) NOT NULL,
  `passengerId` int(11) DEFAULT NULL,
  `matchedId` int(11) DEFAULT NULL,
  `airport` varchar(45) DEFAULT NULL,
  `destination` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`registrationNr`),
  KEY `the main color of the luggage_idx` (`mainColor`),
  KEY `the second color of the luggage_idx` (`secondColor`),
  KEY `form has been submitted by employee_idx` (`employeeId`),
  KEY `type of luggage_idx` (`luggageType`),
  KEY `should have arrived with this flight` (`flight`),
  KEY `belongs to this passenger_idx` (`passengerId`),
  KEY `lostluggage matched to match table_idx` (`matchedId`),
  KEY `lost form filled in at airport_idx` (`airport`),
  KEY `destination of the lost luggage_idx` (`destination`),
  CONSTRAINT `belongs to this passenger` FOREIGN KEY (`passengerId`) REFERENCES `passenger` (`passengerId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `destination of the lost luggage` FOREIGN KEY (`destination`) REFERENCES `destination` (`IATAcode`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `form has been submitted by employee` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`employeeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `lost form filled in at airport` FOREIGN KEY (`airport`) REFERENCES `destination` (`IATAcode`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `lostluggage matched to match table` FOREIGN KEY (`matchedId`) REFERENCES `matched` (`matchedId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `should have arrived with this flight` FOREIGN KEY (`flight`) REFERENCES `flight` (`flightNr`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `the main color of the luggage` FOREIGN KEY (`mainColor`) REFERENCES `color` (`ralCode`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `the second color of the luggage` FOREIGN KEY (`secondColor`) REFERENCES `color` (`ralCode`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `type of luggage` FOREIGN KEY (`luggageType`) REFERENCES `luggagetype` (`luggageTypeId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lostluggage`
--

LOCK TABLES `lostluggage` WRITE;
/*!40000 ALTER TABLE `lostluggage` DISABLE KEYS */;
INSERT INTO `lostluggage` VALUES (1,'2017-12-01','13:14:00',NULL,3,'Puma',8011,9011,'30x40x20',5,'Yellow spongebob stickers','CAI806','ak',11,NULL,NULL,NULL),(2,'2017-11-09','09:37:00',NULL,6,'Samsonite',6004,8011,'20x20x30',2,'Scratches on wheels','CAI040','tz',27,NULL,NULL,NULL),(3,'2017-11-17','16:55:00',NULL,4,'Louis Vutton',6002,3000,'30x30x30',4,NULL,'TK2409','md',17,NULL,NULL,NULL),(4,'2017-12-10','09:00:00',NULL,4,'Nike',6004,3005,'10x40x40',3,NULL,'CAI805','tz',22,NULL,NULL,NULL),(5,'2017-10-16','00:00:00','9087547382',8,'Sony',1015,3005,'30x30x30',5,NULL,'CAI020','pl',13,NULL,NULL,NULL),(6,'2017-12-05','20:00:00','8937594873',8,'Lacoste',1015,8011,'20x40x50',8,NULL,'CND118','ak',25,NULL,NULL,NULL),(7,'2017-10-07','00:00:00',NULL,8,'Nike',9005,6004,'60x60x60',9,NULL,'CAI806','ak',16,NULL,NULL,NULL),(8,'2017-07-09','13:11:00',NULL,3,'Adidas',1015,8023,'40x20x30',2,NULL,'TO3002','aa',8,NULL,NULL,NULL),(9,'2017-10-18','00:00:00',NULL,7,'Panther',5022,6022,'10x30x50',9,'Flag of argentina','CAI1828','do',17,NULL,NULL,NULL),(10,'2017-02-15','00:00:00',NULL,5,'Philips',6004,NULL,'20x90x87',3,'Blue wheels','CAI723','do',32,NULL,NULL,NULL),(11,'2017-12-01','14:56:00','99937',6,'Nike',3017,2004,'60x60x60',3,'Red stripes','CAI041','aa',39,NULL,NULL,NULL),(12,'2017-12-01','14:05:00','112233568',5,'Samsonite',3005,4005,'30x30x30',55,'Black cars','CAI041','aa',43,NULL,NULL,NULL),(14,'2017-12-07','15:55:00','asd33da',5,'Pakdiijk',1024,3017,'30x30x30',5,'Gayrainbows','CAI023','aa',48,NULL,NULL,NULL),(15,'2018-01-04','15:09:00','oaksd',5,'Nike',3000,3017,'30x30x30',5,'Paarse ponys','CAI040','aa',49,NULL,NULL,NULL),(16,'2018-01-06','15:14:00','3kkkd',7,'Nike',3000,3017,'30x30x30',5,'Pink flowers','CAI040','aa',50,NULL,NULL,NULL),(17,'2018-01-24','12:57:00','3kkkd',6,'SuitSuit',9005,5015,'30x30x43',4,'None caracters','CAI040','aa',51,NULL,NULL,NULL),(18,'2018-01-03','12:55:00','aa44',5,'Nike',2004,2004,'30x30x30',5,'Purple stars','CAI041','aa',52,NULL,NULL,NULL),(19,'2018-01-03','12:55:00','aa44',5,'Nike',2004,2004,'30x30x30',5,'Purple stars','CAI041','aa',53,NULL,NULL,NULL),(20,'2018-01-03','12:55:00','aa44',5,'Nike',2004,2004,'30x30x30',5,'Purple stars','CAI041','aa',54,NULL,NULL,NULL),(21,'2018-01-03','06:02:00','',4,'',2004,NULL,'',0,'','CAI041','BJ1',55,NULL,'AMS',NULL);
/*!40000 ALTER TABLE `lostluggage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `luggagetype`
--

DROP TABLE IF EXISTS `luggagetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `luggagetype` (
  `luggageTypeId` int(11) NOT NULL,
  `english` varchar(45) NOT NULL,
  `dutch` varchar(45) NOT NULL,
  PRIMARY KEY (`luggageTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `luggagetype`
--

LOCK TABLES `luggagetype` WRITE;
/*!40000 ALTER TABLE `luggagetype` DISABLE KEYS */;
INSERT INTO `luggagetype` VALUES (1,'Suitcase','Koffer'),(2,'Bag','Tas'),(3,'Bagpack','Rugzak'),(4,'Box','Doos'),(5,'Sports Bag','Sporttas'),(6,'Business Case','Zakenkoffer'),(7,'Case','Kist'),(8,'Other','Anders');
/*!40000 ALTER TABLE `luggagetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `matched`
--

DROP TABLE IF EXISTS `matched`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `matched` (
  `matchedId` int(11) NOT NULL AUTO_INCREMENT,
  `foundluggage` int(11) NOT NULL,
  `lostluggage` int(11) NOT NULL,
  `employeeId` varchar(45) NOT NULL,
  `dateMatched` varchar(45) NOT NULL,
  `delivery` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`matchedId`),
  KEY `submitted by employee_idx` (`employeeId`),
  KEY `lostluggage form_idx` (`lostluggage`),
  KEY `foundluggage form_idx` (`foundluggage`),
  CONSTRAINT `foundluggage form` FOREIGN KEY (`foundluggage`) REFERENCES `foundluggage` (`registrationNr`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `lostluggage form` FOREIGN KEY (`lostluggage`) REFERENCES `lostluggage` (`registrationNr`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `submitted by employee` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`employeeId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matched`
--

LOCK TABLES `matched` WRITE;
/*!40000 ALTER TABLE `matched` DISABLE KEYS */;
/*!40000 ALTER TABLE `matched` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `passenger`
--

DROP TABLE IF EXISTS `passenger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `passenger` (
  `passengerId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `place` varchar(45) DEFAULT NULL,
  `postalcode` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`passengerId`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `passenger`
--

LOCK TABLES `passenger` WRITE;
/*!40000 ALTER TABLE `passenger` DISABLE KEYS */;
INSERT INTO `passenger` VALUES (1,'P. Curie','Versaille',NULL,NULL,NULL,NULL,NULL),(2,'R. Hauer','Bussum',NULL,NULL,NULL,NULL,NULL),(3,'M. Verstappen','Monaco',NULL,NULL,NULL,NULL,NULL),(4,'S. Appelmans','De Panne',NULL,NULL,NULL,NULL,NULL),(5,'A. van Buren','Wassenaar',NULL,NULL,NULL,NULL,NULL),(6,'D. Kuyt','Rotterdam',NULL,NULL,NULL,NULL,NULL),(7,'C. van Houten','Naarden',NULL,NULL,NULL,NULL,NULL),(8,'M. Messi','Barcelona',NULL,NULL,NULL,NULL,NULL),(9,'N. Bonaparte','Paris',NULL,NULL,NULL,NULL,NULL),(10,'M. van Basten','Alkmaar',NULL,NULL,NULL,NULL,NULL),(11,'F. van der Elst','Brussel',NULL,NULL,NULL,NULL,NULL),(12,'R. van Persie','Rotterdam',NULL,NULL,NULL,NULL,NULL),(13,'M. Rutte','den Haag',NULL,NULL,NULL,NULL,NULL),(14,'W.A. van Buren','Wassenaar',NULL,NULL,NULL,NULL,NULL),(15,'J. Verstappen','Oss',NULL,NULL,NULL,NULL,NULL),(16,'A. Gerritse','Ilpendam',NULL,NULL,NULL,NULL,NULL),(17,'D. de Munck','Amsterdam',NULL,NULL,NULL,NULL,NULL),(18,'R. de Boer','Southhampton',NULL,NULL,NULL,NULL,NULL),(19,'S. Kramer','Heerenveen',NULL,NULL,NULL,NULL,NULL),(20,'I. de Bruijn','Leiden',NULL,NULL,NULL,NULL,NULL),(21,'M. van Buren','Wassenaar',NULL,NULL,NULL,NULL,NULL),(22,'E. Gruyaert','Antwerpen',NULL,NULL,NULL,NULL,NULL),(23,'Mw. Hollande','Paris',NULL,NULL,NULL,NULL,NULL),(24,'G. d\'Esting','Paris',NULL,NULL,NULL,NULL,NULL),(25,'F. de Boer','Amsterdam',NULL,NULL,NULL,NULL,NULL),(26,'Mw. Zoetemelk','Lyon',NULL,NULL,NULL,NULL,NULL),(27,'F. Mitterand','Paris',NULL,NULL,NULL,NULL,NULL),(28,'L.. Van Moortsel','Breda',NULL,NULL,NULL,NULL,NULL),(29,'E. Leyers','Turnhout',NULL,NULL,NULL,NULL,NULL),(30,'P. van den Hoogenband','Eindhoven',NULL,NULL,NULL,NULL,NULL),(31,'E. de Munck','Brugge',NULL,NULL,NULL,NULL,NULL),(32,'F. van der Sande','Wuustwezel',NULL,NULL,NULL,NULL,NULL),(33,'D.klaasen','Terhooge 413','Amsterdam','1083EQ','Nederland','d.klaasen@gmail.com','988398'),(34,'Poek','ietsstraat 4','amsterdam','0192EP','Nederland','poek@hotmail.com','0993891'),(35,'Tony','tonasdtrq','okok','okk','ko','jjdasd','iuiuu'),(36,'Bauer, F','Terneuzenstraat 885','Maastricht','9938QP','Nederland','fransbauer1950@hotmail.com','06556354565545'),(37,'J. Flint','Nassaustreet 55','Nassau','2993QW','America','jamesflint@hotmail.com','839481239'),(38,'G, Koolhoven','ooqiwekk','Okiiau','1132QW','Germany','gkoolhoven@gmail.com','+399849938'),(39,'T, Kooten','Achterstraat 99','Hoofddorp','98277QW','Nederland','kootent@gmail.com','993881'),(41,'T. Kok','Duivenlaan 5','Duivendrecht','993QP','Nederland','tonykok@hotmail.com','0679928377'),(42,'Q. Korven','Stalaan 874','Utrecht','9938QW','Nederland','korven@gmail.com','0482777298'),(43,'T. Kok','duivenlaan 5998','Purmerend','9083SS','Nederland','tonykokker@gmail.com','069925492'),(44,'asdko','kokok','ok','ok','ok','k','ok'),(45,'A. Koqieurn','ddss','ss','sss','','',''),(46,'Kokkasdok','okaoskdoak','ok','qokaoskdaosdk','osk','asdasasdsad','okqodk'),(47,'M. Brody','Salazaroad 3','Daroku','9883QW','Aldueio','brodym@gmail.com','9988492'),(48,'Ytakk','owiik 43','Asowiu','388918 QW','Amuuepk','eyunkauwmloduj@hotmail.com','39858392'),(49,'A. Blok','huizenstraat 88','Enkhuizen','8837QW','The Netherlands','ablok@gmail.com','059488939'),(50,'T. Ranker','burgerlaan 5','Enkhuizen','8837QW','The Netherlands','rankter@gmail.com','059488939'),(51,'H. Hendriks','Wibautmooerjerer','Amsterdam','1234 ND','Netherlands','Hendriks@gmail.com','0623847234'),(52,'K. Klarenbeek','terhaagenlaan 5','Uithoorn','9837EQ','Netherlands','kklarenbeek@gmail.com','068372897'),(53,'K. Klarenbeek','terhaagenlaan 5','Uithoorn','9837EQ','Netherlands','kklarenbeek@gmail.com','068372897'),(54,'K. Klarenbeek','terhaagenlaan 5','Uithoorn','9837EQ','Netherlands','kklarenbeek@gmail.com','068372897'),(55,'ok','okok','ok','o','asd','ok','okok');
/*!40000 ALTER TABLE `passenger` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-16 11:03:35