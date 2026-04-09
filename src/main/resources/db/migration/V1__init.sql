
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `results` (
  `Pid` int NOT NULL DEFAULT '0',
  `ChipCode` varchar(7) DEFAULT NULL,
  `Bib` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Cat` varchar(50) DEFAULT NULL,
  `SubCat` varchar(50) DEFAULT NULL,
  `Category` varchar(50) DEFAULT NULL,
  `Name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ChiName` varchar(50) DEFAULT NULL,
  `TeamNo` varchar(10) DEFAULT NULL,
  `Team` varchar(50) DEFAULT NULL,
  `Team1K` varchar(255) DEFAULT NULL,
  `Age` varchar(50) DEFAULT NULL,
  `Nationality` varchar(50) DEFAULT NULL,
  `Country` varchar(50) DEFAULT NULL,
  `SN` varchar(50) DEFAULT NULL,
  `Race` smallint DEFAULT '0',
  `Rank1Cat` int DEFAULT '0',
  `Rank1Mix` int DEFAULT '0',
  `Rank1Tot` int DEFAULT '0',
  `Rank1Team` int DEFAULT '0',
  `Rank2Team` int DEFAULT '0',
  `Lap` int DEFAULT '0',
  `BonusLap` int DEFAULT '0',
  `DQLap` smallint DEFAULT '0',
  `NR` tinyint(1) DEFAULT NULL,
  `DNS` tinyint(1) DEFAULT NULL,
  `DNF` tinyint(1) DEFAULT NULL,
  `DQ` tinyint(1) DEFAULT NULL,
  `Remark` varchar(50) DEFAULT NULL,
  `TimeTeam` int DEFAULT '0',
  `TimeGun` int DEFAULT '0',
  `TimeStart` int DEFAULT '0',
  `TimeFinish` int DEFAULT '0',
  `TimeCP1` int DEFAULT '0',
  `TimeStart1K` int DEFAULT '0',
  `TimeFinish1K` int DEFAULT '0',
  `TimeTeam1K` int DEFAULT '0',
  `TimeGun1K` int DEFAULT '0',
  `Sex` varchar(10) DEFAULT NULL,
  `Rank1Cat1K` int DEFAULT '0',
  `Rank1Mix1K` int DEFAULT '0',
  `Rank1Tot1K` int DEFAULT '0',
  `TimeCP2` int DEFAULT '0',
  `TimeCP3` int DEFAULT '0',
  `TimeCP4` int DEFAULT '0',
  `TimeCP5` int DEFAULT '0',
  `TimeCP6` int DEFAULT '0',
  `TimeCP7` int DEFAULT '0',
  `TimeCP8` int DEFAULT '0',
  `TimeCP9` int DEFAULT '0',
  `time0` int DEFAULT '0',
  `time1` int DEFAULT NULL,
  `time2` int DEFAULT NULL,
  `time3` int DEFAULT NULL,
  `time4` int DEFAULT NULL,
  `time5` int DEFAULT NULL,
  `time6` int DEFAULT NULL,
  `time7` int DEFAULT NULL,
  `time8` int DEFAULT NULL,
  `time9` int DEFAULT NULL,
  `time10` int DEFAULT NULL,
  `time11` int DEFAULT NULL,
  `time12` int DEFAULT NULL,
  `time13` int DEFAULT NULL,
  `time14` int DEFAULT NULL,
  `time15` int DEFAULT NULL,
  `time16` int DEFAULT NULL,
  `time17` int DEFAULT NULL,
  `time18` int DEFAULT NULL,
  `time19` int DEFAULT NULL,
  `time20` int DEFAULT NULL,
  `time21` int DEFAULT NULL,
  `time22` int DEFAULT NULL,
  `time23` int DEFAULT NULL,
  `time24` int DEFAULT NULL,
  `time25` int DEFAULT NULL,
  `time26` int DEFAULT NULL,
  `time27` int DEFAULT NULL,
  `time28` int DEFAULT NULL,
  `time29` int DEFAULT NULL,
  `time30` int DEFAULT NULL,
  `time31` int DEFAULT NULL,
  `time32` int DEFAULT NULL,
  `time33` int DEFAULT NULL,
  `time34` int DEFAULT NULL,
  `time35` int DEFAULT NULL,
  `time36` int DEFAULT NULL,
  `time37` int DEFAULT NULL,
  `time38` int DEFAULT NULL,
  `time39` int DEFAULT NULL,
  `time40` int DEFAULT NULL,
  `time41` int DEFAULT NULL,
  `time42` int DEFAULT NULL,
  `time43` int DEFAULT NULL,
  `time44` int DEFAULT NULL,
  `time45` int DEFAULT NULL,
  `time46` int DEFAULT NULL,
  `time47` int DEFAULT NULL,
  `time48` int DEFAULT NULL,
  `time49` int DEFAULT NULL,
  `time50` int DEFAULT NULL,
  `time51` int DEFAULT NULL,
  `time52` int DEFAULT NULL,
  `time53` int DEFAULT NULL,
  `time54` int DEFAULT NULL,
  `time55` int DEFAULT NULL,
  `time56` int DEFAULT NULL,
  `time57` int DEFAULT NULL,
  `time58` int DEFAULT NULL,
  `time59` int DEFAULT NULL,
  `time60` int DEFAULT NULL,
  `time61` int DEFAULT NULL,
  `time62` int DEFAULT NULL,
  `time63` int DEFAULT NULL,
  `time64` int DEFAULT NULL,
  `time65` int DEFAULT NULL,
  `time66` int DEFAULT NULL,
  `time67` int DEFAULT NULL,
  `time68` int DEFAULT NULL,
  `time69` int DEFAULT NULL,
  `time70` int DEFAULT NULL,
  `time71` int DEFAULT NULL,
  `time72` int DEFAULT NULL,
  `time73` int DEFAULT NULL,
  `time74` int DEFAULT NULL,
  `time75` int DEFAULT NULL,
  `time76` int DEFAULT NULL,
  `time77` int DEFAULT NULL,
  `time78` int DEFAULT NULL,
  `time79` int DEFAULT NULL,
  `time80` int DEFAULT NULL,
  `time81` int DEFAULT NULL,
  `time82` int DEFAULT NULL,
  `time83` int DEFAULT NULL,
  `time84` int DEFAULT NULL,
  `time85` int DEFAULT NULL,
  `time86` int DEFAULT NULL,
  `time87` int DEFAULT NULL,
  `time88` int DEFAULT NULL,
  `time89` int DEFAULT NULL,
  `time90` int DEFAULT NULL,
  `time91` int DEFAULT NULL,
  `time92` int DEFAULT NULL,
  `time93` int DEFAULT NULL,
  `time94` int DEFAULT NULL,
  `time95` int DEFAULT NULL,
  `time96` int DEFAULT NULL,
  `time97` int DEFAULT NULL,
  `time98` int DEFAULT NULL,
  `time99` int DEFAULT NULL,
  `time100` int DEFAULT NULL,
  `Bib_1` varchar(50) DEFAULT NULL,
  `No` int DEFAULT NULL,
  `Bib2` varchar(50) DEFAULT NULL,
  `Name2` varchar(50) DEFAULT NULL,
  `eventid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `DOB` varchar(50) DEFAULT NULL,
  `Name3` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `FS` tinyint DEFAULT NULL,
  `NSBF` tinyint DEFAULT NULL,
  `Distance` decimal(10,2) DEFAULT NULL,
  `NRIC` varchar(50) DEFAULT NULL,
  `company` varchar(50) DEFAULT NULL,
  `TimeCP10` int DEFAULT NULL,
  PRIMARY KEY (`Pid`),
  UNIQUE KEY `SYS_IDX_SYS_PK_10623_10624` (`Pid`),
  KEY `RESULTS_CHIPCODE` (`ChipCode`),
  KEY `RESULTS_PID` (`Pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `results_archive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `results_archive` (
  `Pid` int NOT NULL DEFAULT '0',
  `ChipCode` varchar(7) DEFAULT NULL,
  `Bib` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Cat` varchar(50) DEFAULT NULL,
  `SubCat` varchar(50) DEFAULT NULL,
  `Category` varchar(50) DEFAULT NULL,
  `Name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ChiName` varchar(50) DEFAULT NULL,
  `TeamNo` varchar(10) DEFAULT NULL,
  `Team` varchar(50) DEFAULT NULL,
  `Team1K` varchar(255) DEFAULT NULL,
  `Age` varchar(50) DEFAULT NULL,
  `Nationality` varchar(50) DEFAULT NULL,
  `Country` varchar(50) DEFAULT NULL,
  `SN` varchar(50) DEFAULT NULL,
  `Race` smallint DEFAULT '0',
  `Rank1Cat` int DEFAULT '0',
  `Rank1Mix` int DEFAULT '0',
  `Rank1Tot` int DEFAULT '0',
  `Rank1Team` int DEFAULT '0',
  `Rank2Team` int DEFAULT '0',
  `Lap` int DEFAULT '0',
  `BonusLap` int DEFAULT '0',
  `DQLap` smallint DEFAULT '0',
  `NR` tinyint(1) DEFAULT NULL,
  `DNS` tinyint(1) DEFAULT NULL,
  `DNF` tinyint(1) DEFAULT NULL,
  `DQ` tinyint(1) DEFAULT NULL,
  `Remark` varchar(50) DEFAULT NULL,
  `TimeTeam` int DEFAULT '0',
  `TimeGun` int DEFAULT '0',
  `TimeStart` int DEFAULT '0',
  `TimeFinish` int DEFAULT '0',
  `TimeCP1` int DEFAULT '0',
  `TimeStart1K` int DEFAULT '0',
  `TimeFinish1K` int DEFAULT '0',
  `TimeTeam1K` int DEFAULT '0',
  `TimeGun1K` int DEFAULT '0',
  `Sex` varchar(10) DEFAULT NULL,
  `Rank1Cat1K` int DEFAULT '0',
  `Rank1Mix1K` int DEFAULT '0',
  `Rank1Tot1K` int DEFAULT '0',
  `TimeCP2` int DEFAULT '0',
  `TimeCP3` int DEFAULT '0',
  `TimeCP4` int DEFAULT '0',
  `TimeCP5` int DEFAULT '0',
  `TimeCP6` int DEFAULT '0',
  `TimeCP7` int DEFAULT '0',
  `TimeCP8` int DEFAULT '0',
  `TimeCP9` int DEFAULT '0',
  `time0` int DEFAULT '0',
  `time1` int DEFAULT NULL,
  `time2` int DEFAULT NULL,
  `time3` int DEFAULT NULL,
  `time4` int DEFAULT NULL,
  `time5` int DEFAULT NULL,
  `time6` int DEFAULT NULL,
  `time7` int DEFAULT NULL,
  `time8` int DEFAULT NULL,
  `time9` int DEFAULT NULL,
  `time10` int DEFAULT NULL,
  `time11` int DEFAULT NULL,
  `time12` int DEFAULT NULL,
  `time13` int DEFAULT NULL,
  `time14` int DEFAULT NULL,
  `time15` int DEFAULT NULL,
  `time16` int DEFAULT NULL,
  `time17` int DEFAULT NULL,
  `time18` int DEFAULT NULL,
  `time19` int DEFAULT NULL,
  `time20` int DEFAULT NULL,
  `time21` int DEFAULT NULL,
  `time22` int DEFAULT NULL,
  `time23` int DEFAULT NULL,
  `time24` int DEFAULT NULL,
  `time25` int DEFAULT NULL,
  `time26` int DEFAULT NULL,
  `time27` int DEFAULT NULL,
  `time28` int DEFAULT NULL,
  `time29` int DEFAULT NULL,
  `time30` int DEFAULT NULL,
  `time31` int DEFAULT NULL,
  `time32` int DEFAULT NULL,
  `time33` int DEFAULT NULL,
  `time34` int DEFAULT NULL,
  `time35` int DEFAULT NULL,
  `time36` int DEFAULT NULL,
  `time37` int DEFAULT NULL,
  `time38` int DEFAULT NULL,
  `time39` int DEFAULT NULL,
  `time40` int DEFAULT NULL,
  `time41` int DEFAULT NULL,
  `time42` int DEFAULT NULL,
  `time43` int DEFAULT NULL,
  `time44` int DEFAULT NULL,
  `time45` int DEFAULT NULL,
  `time46` int DEFAULT NULL,
  `time47` int DEFAULT NULL,
  `time48` int DEFAULT NULL,
  `time49` int DEFAULT NULL,
  `time50` int DEFAULT NULL,
  `time51` int DEFAULT NULL,
  `time52` int DEFAULT NULL,
  `time53` int DEFAULT NULL,
  `time54` int DEFAULT NULL,
  `time55` int DEFAULT NULL,
  `time56` int DEFAULT NULL,
  `time57` int DEFAULT NULL,
  `time58` int DEFAULT NULL,
  `time59` int DEFAULT NULL,
  `time60` int DEFAULT NULL,
  `time61` int DEFAULT NULL,
  `time62` int DEFAULT NULL,
  `time63` int DEFAULT NULL,
  `time64` int DEFAULT NULL,
  `time65` int DEFAULT NULL,
  `time66` int DEFAULT NULL,
  `time67` int DEFAULT NULL,
  `time68` int DEFAULT NULL,
  `time69` int DEFAULT NULL,
  `time70` int DEFAULT NULL,
  `time71` int DEFAULT NULL,
  `time72` int DEFAULT NULL,
  `time73` int DEFAULT NULL,
  `time74` int DEFAULT NULL,
  `time75` int DEFAULT NULL,
  `time76` int DEFAULT NULL,
  `time77` int DEFAULT NULL,
  `time78` int DEFAULT NULL,
  `time79` int DEFAULT NULL,
  `time80` int DEFAULT NULL,
  `time81` int DEFAULT NULL,
  `time82` int DEFAULT NULL,
  `time83` int DEFAULT NULL,
  `time84` int DEFAULT NULL,
  `time85` int DEFAULT NULL,
  `time86` int DEFAULT NULL,
  `time87` int DEFAULT NULL,
  `time88` int DEFAULT NULL,
  `time89` int DEFAULT NULL,
  `time90` int DEFAULT NULL,
  `time91` int DEFAULT NULL,
  `time92` int DEFAULT NULL,
  `time93` int DEFAULT NULL,
  `time94` int DEFAULT NULL,
  `time95` int DEFAULT NULL,
  `time96` int DEFAULT NULL,
  `time97` int DEFAULT NULL,
  `time98` int DEFAULT NULL,
  `time99` int DEFAULT NULL,
  `time100` int DEFAULT NULL,
  `Bib_1` varchar(50) DEFAULT NULL,
  `No` int DEFAULT NULL,
  `Bib2` varchar(50) DEFAULT NULL,
  `Name2` varchar(50) DEFAULT NULL,
  `eventid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `DOB` varchar(50) DEFAULT NULL,
  `Name3` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `FS` tinyint DEFAULT NULL,
  `NSBF` tinyint DEFAULT NULL,
  `Distance` decimal(10,2) DEFAULT NULL,
  `NRIC` varchar(50) DEFAULT NULL,
  `company` varchar(50) DEFAULT NULL,
  `TimeCP10` int DEFAULT NULL,
  KEY `RESULTS_ARCHIVE_CHIPCODE` (`ChipCode`),
  KEY `RESULTS_ARCHIVE_EVENTID` (`eventid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `t_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_event` (
  `id` varchar(36) NOT NULL,
  `name` varchar(200) NOT NULL,
  `event_dt` date DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `country` varchar(100) NOT NULL,
  `weather` varchar(200) DEFAULT NULL,
  `archived` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `t_event_cat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_event_cat` (
  `event_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `cat` varchar(100) NOT NULL,
  `category` varchar(100) NOT NULL,
  `distance` decimal(10,0) NOT NULL,
  `race` varchar(100) NOT NULL,
  `gender` varchar(100) NOT NULL,
  `TimeGun` int DEFAULT NULL,
  `TimeGun1` int DEFAULT NULL,
  `TimeGun2` int DEFAULT NULL,
  `TimeGun3` int DEFAULT NULL,
  `TimeGun4` int DEFAULT NULL,
  `TimeGun5` int DEFAULT NULL,
  `TimeGun6` int DEFAULT NULL,
  `CPLIST` varchar(200) DEFAULT NULL,
  `is_Lap` tinyint DEFAULT NULL,
  `racemode` varchar(100) DEFAULT NULL,
  `cat_id` int DEFAULT NULL,
  `Top` int DEFAULT NULL,
  `is_Live` int DEFAULT NULL,
  `is_result` int DEFAULT NULL,
  `top_prize` decimal(10,0) DEFAULT '0',
  KEY `t_event_cat_t_event_FK` (`event_id`),
  CONSTRAINT `t_event_cat_t_event_FK` FOREIGN KEY (`event_id`) REFERENCES `t_event` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `t_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_org` (
  `id` varchar(36) NOT NULL,
  `org_name` varchar(200) NOT NULL,
  `owner` varchar(200) NOT NULL,
  `is_active` tinyint NOT NULL,
  `ownercontact` varchar(100) DEFAULT NULL,
  `owneremail` varchar(100) DEFAULT NULL,
  `dt_create` date DEFAULT NULL,
  `dt_update` date DEFAULT NULL,
  `alias` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `t_org_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_org_event` (
  `id` varchar(36) NOT NULL,
  `org_id` varchar(36) NOT NULL,
  `event_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `t_org_event_t_org_fk` (`org_id`),
  KEY `t_org_event_t_event_FK` (`event_id`),
  CONSTRAINT `t_org_event_t_event_FK` FOREIGN KEY (`event_id`) REFERENCES `t_event` (`id`),
  CONSTRAINT `t_org_event_t_org_fk` FOREIGN KEY (`org_id`) REFERENCES `t_org` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `t_org_contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_org_contract` (
  `id` varchar(36) NOT NULL,
  `org_id` varchar(36) NOT NULL,
  `description` varchar(100) NOT NULL,
  `contract_start_date` date NOT NULL,
  `contract_end_date` date NOT NULL,
  `billing_duration` varchar(100) NOT NULL,
  `status` varchar(100) NOT NULL,
  `is_active` tinyint NOT NULL,
  KEY `t_org_contract_t_org_fk` (`org_id`),
  CONSTRAINT `t_org_contract_t_org_fk` FOREIGN KEY (`org_id`) REFERENCES `t_org` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `t_org_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_org_user` (
  `id` varchar(36) NOT NULL,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `org_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `session_id` varchar(100) DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `session_expiry_time` datetime DEFAULT NULL,
  `full_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Role` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `t_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_results` (
  `EventId` varchar(50) DEFAULT NULL,
  `Pid` int NOT NULL DEFAULT '0',
  `ChipCode` varchar(7) DEFAULT NULL,
  `Bib` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Cat` varchar(50) DEFAULT NULL,
  `SubCat` varchar(50) DEFAULT NULL,
  `Category` varchar(50) DEFAULT NULL,
  `Name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ChiName` varchar(50) DEFAULT NULL,
  `TeamNo` varchar(10) DEFAULT NULL,
  `Team` varchar(50) DEFAULT NULL,
  `Team1K` varchar(255) DEFAULT NULL,
  `Age` varchar(50) DEFAULT NULL,
  `Nationality` varchar(50) DEFAULT NULL,
  `Country` varchar(50) DEFAULT NULL,
  `SN` varchar(50) DEFAULT NULL,
  `Race` smallint DEFAULT '0',
  `Rank1Cat` int DEFAULT '0',
  `Rank1Mix` int DEFAULT '0',
  `Rank1Tot` int DEFAULT '0',
  `Rank1Team` int DEFAULT '0',
  `Rank2Team` int DEFAULT '0',
  `Lap` int DEFAULT '0',
  `BonusLap` int DEFAULT '0',
  `DQLap` smallint DEFAULT '0',
  `NR` tinyint(1) DEFAULT NULL,
  `DNS` tinyint(1) DEFAULT NULL,
  `DNF` tinyint(1) DEFAULT NULL,
  `DQ` tinyint(1) DEFAULT NULL,
  `FS` tinyint(1) DEFAULT NULL,
  `NSBF` tinyint(1) DEFAULT NULL,
  `Remark` varchar(50) DEFAULT NULL,
  `TimeTeam` int DEFAULT '0',
  `TimeGun` int DEFAULT '0',
  `TimeStart` int DEFAULT '0',
  `TimeFinish` int DEFAULT '0',
  `TimeCP1` int DEFAULT '0',
  `TimeStart1K` int DEFAULT '0',
  `TimeFinish1K` int DEFAULT '0',
  `TimeTeam1K` int DEFAULT '0',
  `TimeGun1K` int DEFAULT '0',
  `Sex` varchar(10) DEFAULT NULL,
  `Rank1Cat1K` int DEFAULT '0',
  `Rank1Mix1K` int DEFAULT '0',
  `Rank1Tot1K` int DEFAULT '0',
  `TimeCP2` int DEFAULT '0',
  `TimeCP3` int DEFAULT '0',
  `TimeCP4` int DEFAULT '0',
  `TimeCP5` int DEFAULT '0',
  `TimeCP6` int DEFAULT '0',
  `TimeCP7` int DEFAULT '0',
  `TimeCP8` int DEFAULT '0',
  `TimeCP9` int DEFAULT '0',
  `TimeCP10` int DEFAULT '0',
  `time1` int DEFAULT '0',
  `time2` int DEFAULT '0',
  `time3` int DEFAULT '0',
  `time4` int DEFAULT '0',
  `time5` int DEFAULT '0',
  `time6` int DEFAULT '0',
  `time7` int DEFAULT '0',
  `time8` int DEFAULT '0',
  `time9` int DEFAULT '0',
  `time10` int DEFAULT '0',
  `time11` int DEFAULT '0',
  `time12` int DEFAULT '0',
  `time13` int DEFAULT '0',
  `time14` int DEFAULT '0',
  `time15` int DEFAULT '0',
  `time16` int DEFAULT '0',
  `time17` int DEFAULT '0',
  `time18` int DEFAULT '0',
  `time19` int DEFAULT '0',
  `time20` int DEFAULT '0',
  `time21` int DEFAULT '0',
  `time22` int DEFAULT '0',
  `time23` int DEFAULT '0',
  `time24` int DEFAULT '0',
  `time25` int DEFAULT '0',
  `time26` int DEFAULT '0',
  `time27` int DEFAULT '0',
  `time28` int DEFAULT '0',
  `time29` int DEFAULT '0',
  `time30` int DEFAULT '0',
  `time31` int DEFAULT '0',
  `time32` int DEFAULT '0',
  `time33` int DEFAULT '0',
  `time34` int DEFAULT '0',
  `time35` int DEFAULT '0',
  `time36` int DEFAULT '0',
  `time37` int DEFAULT '0',
  `time38` int DEFAULT '0',
  `time39` int DEFAULT '0',
  `time40` int DEFAULT '0',
  `time41` int DEFAULT '0',
  `time42` int DEFAULT '0',
  `time43` int DEFAULT '0',
  `time44` int DEFAULT '0',
  `time45` int DEFAULT '0',
  `time46` int DEFAULT '0',
  `time47` int DEFAULT '0',
  `time48` int DEFAULT '0',
  `time49` int DEFAULT '0',
  `time50` int DEFAULT '0',
  `time51` int DEFAULT '0',
  `time52` int DEFAULT '0',
  `time53` int DEFAULT '0',
  `time54` int DEFAULT '0',
  `time55` int DEFAULT '0',
  `time56` int DEFAULT '0',
  `time57` int DEFAULT '0',
  `time58` int DEFAULT '0',
  `time59` int DEFAULT '0',
  `time60` int DEFAULT '0',
  `time61` int DEFAULT '0',
  `time62` int DEFAULT '0',
  `time63` int DEFAULT '0',
  `time64` int DEFAULT '0',
  `time65` int DEFAULT '0',
  `time66` int DEFAULT '0',
  `time67` int DEFAULT '0',
  `time68` int DEFAULT '0',
  `time69` int DEFAULT '0',
  `time70` int DEFAULT '0',
  `time71` int DEFAULT '0',
  `time72` int DEFAULT '0',
  `time73` int DEFAULT '0',
  `time74` int DEFAULT '0',
  `time75` int DEFAULT '0',
  `time76` int DEFAULT '0',
  `time77` int DEFAULT '0',
  `time78` int DEFAULT '0',
  `time79` int DEFAULT '0',
  `time80` int DEFAULT '0',
  `time81` int DEFAULT '0',
  `time82` int DEFAULT '0',
  `time83` int DEFAULT '0',
  `time84` int DEFAULT '0',
  `time85` int DEFAULT '0',
  `time86` int DEFAULT '0',
  `time87` int DEFAULT '0',
  `time88` int DEFAULT '0',
  `time89` int DEFAULT '0',
  `time90` int DEFAULT '0',
  `time91` int DEFAULT '0',
  `time92` int DEFAULT '0',
  `time93` int DEFAULT '0',
  `time94` int DEFAULT '0',
  `time95` int DEFAULT '0',
  `time96` int DEFAULT '0',
  `time97` int DEFAULT '0',
  `time98` int DEFAULT '0',
  `time99` int DEFAULT '0',
  `time100` int DEFAULT '0',
  `Bib_1` varchar(50) DEFAULT NULL,
  `No` int DEFAULT NULL,
  `Bib2` varchar(50) DEFAULT NULL,
  `Name2` varchar(50) DEFAULT NULL,
  `DOB` varchar(50) DEFAULT NULL,
  `Name3` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Distance` decimal(10,2) DEFAULT NULL,
  `NRIC` varchar(50) DEFAULT NULL,
  `company` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Pid`),
  UNIQUE KEY `SYS_IDX_SYS_PK_10623_10624` (`Pid`),
  KEY `RESULTS_CHIPCODE` (`ChipCode`),
  KEY `RESULTS_PID` (`Pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `servicelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `servicelog` (
  `Query` varchar(100) DEFAULT NULL,
  `Query_Text` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `Log_Time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


-- Functions
DELIMITER $$

DROP FUNCTION IF EXISTS `_FORMATTIME`$$
CREATE FUNCTION `_FORMATTIME`(n INT) RETURNS varchar(20) CHARSET utf8mb4
    READS SQL DATA
BEGIN
    DECLARE result VARCHAR(20);

    IF n > 0 THEN
        SET result = CONCAT(
            LPAD(FLOOR(n / 3600000), 2, '0'), ':', 
            LPAD(FLOOR((n % 3600000) / 60000), 2, '0'), ':', 
            LPAD(FLOOR((n % 60000) / 1000), 2, '0'), '.', 
            LPAD(n % 1000, 3, '0')
        );
    ELSE
        SET result = NULL;
    END IF;
    RETURN result;
END$$

-- Procedures

DROP PROCEDURE IF EXISTS `P_LEADERBOARD_REPORT`$$
CREATE PROCEDURE `P_LEADERBOARD_REPORT`(
    IN in_eventid CHAR(36),
    IN in_category VARCHAR(100)
)
BEGIN
    DECLARE is_archived TINYINT(1);

    -- Check if the event is archived
    SELECT IFNULL(archived, 0) INTO is_archived
    FROM t_event
    WHERE id = in_eventid;

    -- If event is archived, query from results_archive table
    IF is_archived = 1 THEN
        SELECT
            r.bib,
            r.name,
            r.category,
            r.rank1cat AS rankCat,
            r.rank1mix AS rankMix,
            r.rank1tot AS rankTot,
            r.timestart,
            r.timefinish,
            r.timegun,
            r.timecp1,
            r.timecp2,
            r.timecp3,
            r.timecp4,
            r.timecp5,
            r.timecp6,
            r.timecp7,
            r.timecp8,
            r.timecp9,
            r.timecp10,
            r.sex,
            r.lap,
            ec.cplist
        FROM results_archive r
        LEFT JOIN t_event_cat ec ON r.eventid = ec.event_id AND r.cat = ec.cat
        WHERE r.eventid = in_eventid
        AND r.cat = in_category
        AND r.rank1cat > 0
        ORDER BY r.rank1cat ASC;
    ELSE
        -- If event is NOT archived, query from results table
        SELECT
            r.bib,
            r.name,
            r.category,
            r.rank1cat AS rankCat,
            r.rank1mix AS rankMix,
            r.rank1tot AS rankTot,
            r.timestart,
            r.timefinish,
            r.timegun,
            r.timecp1,
            r.timecp2,
            r.timecp3,
            r.timecp4,
            r.timecp5,
            r.timecp6,
            r.timecp7,
            r.timecp8,
            r.timecp9,
            r.timecp10,
            r.sex,
            r.lap,
            ec.cplist
        FROM results r
        LEFT JOIN t_event_cat ec ON r.eventid = ec.event_id AND r.cat = ec.cat
        WHERE r.eventid = in_eventid
        AND r.cat = in_category
        AND r.rank1cat > 0
        ORDER BY r.rank1cat ASC;
    END IF;
END$$

DROP PROCEDURE IF EXISTS `P_SEL_RESULT_RANK1CAT`$$
CREATE PROCEDURE `P_SEL_RESULT_RANK1CAT`(
    IN in_EventID VARCHAR(100),
    IN in_Race VARCHAR(100)
)
BEGIN
    DECLARE sql_query TEXT;
    DECLARE race_mode TEXT;
    DECLARE race_CP TEXT;
    DECLARE result_CP TEXT;
    DECLARE i INT DEFAULT 1;
    DECLARE temp TEXT DEFAULT '';
    DECLARE prev_time TEXT;

    -- Get RACEMODE and CPLIST from t_event_cat
    SELECT DISTINCT RACEMODE, CPLIST
    INTO race_mode, race_CP
    FROM t_event_cat
    WHERE cat = in_Race AND event_id = in_EventID;

    IF race_mode = 'LAP' THEN
        -- Build dynamic columns for LAP mode - return raw time values
        SET temp = '';
        SET i = 1;
        
        -- Return raw time fields without calculations
        WHILE i <= race_CP DO
            IF i > 1 THEN
                SET temp = CONCAT(temp, ', ');
            END IF;
            SET temp = CONCAT(temp, 'time', i);
            SET i = i + 1;
        END WHILE;
        SET result_CP = temp;

        -- Build dynamic query for LAP mode
        SET @sql_query = CONCAT(
            'SELECT CAT, CATEGORY, RANK1MIX as RK1MIX, RANK1CAT as RK1CAT, Bib, Name, ',
            'timestart, timegun, timefinish, time0, ',
            result_CP, ', ',
            'Remark ',
            'FROM results ',
            'WHERE cat = "', in_Race, '" ',
            'AND eventid = "', in_EventID, '" ',
            'AND rank1cat > 0 ',
            'ORDER BY RANK1CAT=0, RANK1CAT;'
        );
    ELSE
        -- For normal mode, parse CPLIST into columns (return raw values)
        SET result_cp = '';
        SET @cp_list = race_CP;

        WHILE LOCATE(',', @cp_list) > 0 DO
            SET @cp = SUBSTRING_INDEX(@cp_list, ',', 1);
            SET result_cp = CONCAT(result_cp, @cp, ', ');
            SET @cp_list = SUBSTRING(@cp_list, LOCATE(',', @cp_list) + 1);
        END WHILE;

        -- Add the last checkpoint
        SET result_cp = CONCAT(result_cp, @cp_list);

        -- Build dynamic query for non-LAP mode
        SET @sql_query = CONCAT(
            'SELECT CATEGORY, RANK1CAT as RK1CAT, Bib, Name, ',
            'TimeStart, TimeFinish, TimeGun, ',
            result_cp, ', Remark ',
            'FROM results ',
            'WHERE cat = "', in_Race, '" ',
            'AND eventid = "', in_EventID, '" ',
            'AND rank1cat > 0 ',
            'ORDER BY RANK1CAT=0, RANK1CAT;'
        );
    END IF;

    -- Log the generated query
    INSERT INTO servicelog(Query, Query_text, log_time)
    VALUES('P_SEL_RESULT_RANK1CAT', @sql_query, NOW());

    -- Execute the dynamic query
    PREPARE stmt FROM @sql_query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$

DROP PROCEDURE IF EXISTS `P_SEL_RESULT_RANK1MIX`$$
CREATE PROCEDURE `P_SEL_RESULT_RANK1MIX`(
    IN in_EventID VARCHAR(100),
    IN in_dist VARCHAR(100),
    IN in_gender VARCHAR(100)
)
BEGIN
    DECLARE sql_query TEXT;
    DECLARE race_mode TEXT;
    DECLARE race_CP TEXT;
    DECLARE result_CP TEXT;
    DECLARE i INT DEFAULT 1;
    DECLARE temp TEXT DEFAULT '';

    -- Get RACEMODE and CPLIST from t_event_cat
    SELECT DISTINCT RACEMODE, CPLIST
    INTO race_mode, race_CP
    FROM t_event_cat
    WHERE distance = in_dist AND event_id = in_EventID;

    IF race_mode = 'LAP' THEN
        -- Build dynamic columns for LAP mode - return raw time values
        SET temp = '';
        SET i = 1;
        
        -- Return raw time fields without calculations
        WHILE i <= race_CP DO
            IF i > 1 THEN
                SET temp = CONCAT(temp, ', ');
            END IF;
            SET temp = CONCAT(temp, 'time', i);
            SET i = i + 1;
        END WHILE;
        SET result_CP = temp;

        -- Build dynamic query for LAP mode
        SET @sql_query = CONCAT(
            'SELECT CAT, CATEGORY, RANK1MIX as RK1MIX, RANK1CAT as RK1CAT, Bib, Name, ',
            'timestart, timegun, timefinish, time0, ',
            result_CP, ', ',
            'Remark ',
            'FROM results ',
            'WHERE distance = "', in_dist, '" ',
            'AND eventid = "', in_EventID, '" ',
            'AND sex = "', in_gender, '" ',
            'AND rank1mix > 0 ',
            'ORDER BY RANK1MIX=0, RANK1MIX;'
        );
    ELSE
        -- For normal mode, parse CPLIST into columns (return raw values)
        SET result_cp = '';
        SET @cp_list = race_CP;

        WHILE LOCATE(',', @cp_list) > 0 DO
            SET @cp = SUBSTRING_INDEX(@cp_list, ',', 1);
            SET result_cp = CONCAT(result_cp, @cp, ', ');
            SET @cp_list = SUBSTRING(@cp_list, LOCATE(',', @cp_list) + 1);
        END WHILE;

        -- Add the last checkpoint
        SET result_cp = CONCAT(result_cp, @cp_list);

        -- Build dynamic query for non-LAP mode
        SET @sql_query = CONCAT(
            'SELECT ''RKMIX'' as item, RANK1MIX as RK1MIX, RANK1CAT as RK1CAT, Bib, Name, ',
            'TimeStart, TimeFinish, TimeGun, ',
            result_cp, ', Remark ',
            'FROM results ',
            'WHERE distance = "', in_dist, '" ',
            'AND eventid = "', in_EventID, '" ',
            'AND sex = "', in_gender, '" ',
            'AND rank1mix > 0 ',
            'ORDER BY RANK1MIX=0, RANK1MIX;'
        );
    END IF;

    -- Log the generated query
    INSERT INTO servicelog(Query, Query_text, log_time)
    VALUES('P_SEL_RESULT_RANK1MIX', @sql_query, NOW());

    -- Execute the dynamic query
    PREPARE stmt FROM @sql_query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$

DROP PROCEDURE IF EXISTS `P_SEL_RESULT_RANK1TOT`$$
CREATE PROCEDURE `P_SEL_RESULT_RANK1TOT`(
    IN in_EventID VARCHAR(100),
    IN in_dist VARCHAR(100)
)
BEGIN
    DECLARE sql_query TEXT;
    DECLARE race_mode TEXT;
    DECLARE race_CP TEXT;
    DECLARE result_CP TEXT;
    DECLARE i INT DEFAULT 1;
    DECLARE temp TEXT DEFAULT '';

    -- Get RACEMODE and CPLIST from t_event_cat
    SELECT DISTINCT RACEMODE, CPLIST
    INTO race_mode, race_CP
    FROM t_event_cat
    WHERE distance = in_dist AND event_id = in_EventID
    LIMIT 1;

    IF race_mode = 'LAP' THEN
        -- Build dynamic columns for LAP mode - return raw time values
        SET temp = '';
        SET i = 1;
        
        -- Return raw time fields without calculations
        WHILE i <= race_CP DO
            IF i > 1 THEN
                SET temp = CONCAT(temp, ', ');
            END IF;
            SET temp = CONCAT(temp, 'time', i);
            SET i = i + 1;
        END WHILE;
        SET result_CP = temp;

        -- Build dynamic query for LAP mode
        SET @sql_query = CONCAT(
            'SELECT CAT, CATEGORY, RANK1TOT as RK1TOT, RANK1MIX as RK1MIX, RANK1CAT as RK1CAT, Bib, Name, ',
            'timestart, timegun, timefinish, time0, ',
            result_CP, ', ',
            'Remark ',
            'FROM results ',
            'WHERE distance = "', in_dist, '" ',
            'AND eventid = "', in_EventID, '" ',
            'AND rank1tot > 0 ',
            'ORDER BY RANK1TOT=0, RANK1TOT;'
        );
    ELSE
        -- For normal mode, parse CPLIST into columns (return raw values)
        SET result_cp = '';
        SET @cp_list = race_CP;

        WHILE LOCATE(',', @cp_list) > 0 DO
            SET @cp = SUBSTRING_INDEX(@cp_list, ',', 1);
            SET result_cp = CONCAT(result_cp, @cp, ', ');
            SET @cp_list = SUBSTRING(@cp_list, LOCATE(',', @cp_list) + 1);
        END WHILE;

        -- Add the last checkpoint
        SET result_cp = CONCAT(result_cp, @cp_list);

        -- Build dynamic query for non-LAP mode
        SET @sql_query = CONCAT(
            'SELECT ''RKTOT'' as item, RANK1TOT as RK1TOT, RANK1MIX as RK1MIX, RANK1CAT as RK1CAT, Bib, Name, ',
            'TimeStart, TimeFinish, TimeGun, ',
            result_cp, ', Remark ',
            'FROM results ',
            'WHERE distance = "', in_dist, '" ',
            'AND eventid = "', in_EventID, '" ',
            'ORDER BY RANK1TOT=0, RANK1TOT;'
        );
    END IF;

    -- Log the generated query
    INSERT INTO servicelog(Query, Query_text, log_time)
    VALUES('P_SEL_RESULT_RANK1TOT', @sql_query, NOW());

    -- Execute the dynamic query
    PREPARE stmt FROM @sql_query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$

-- Missing procedure: generate_marathon_result

-- Missing procedure: get_marathon_results

DROP PROCEDURE IF EXISTS `P_ASSIGN_RANK1CAT`$$
CREATE PROCEDURE `P_ASSIGN_RANK1CAT`(
    IN in_eventid CHAR(36),
    IN in_cat VARCHAR(100))
BEGIN
    DECLARE race_mode TEXT;
    DECLARE race_cp TEXT;
    DECLARE order_columns TEXT DEFAULT '';
    DECLARE where_conditions TEXT DEFAULT '';
    DECLARE i INT DEFAULT 1;
    DECLARE row_count_results INT DEFAULT 0;
    DECLARE row_count_cat INT DEFAULT 0;

    -- Check existence in t_event_cat
    SELECT COUNT(*) INTO row_count_cat 
    FROM t_event_cat 
    WHERE event_id = in_eventid AND cat = in_cat;

    -- Check existence in t_results
    SELECT COUNT(*) INTO row_count_results
    FROM results 
    WHERE EventId = in_eventid AND cat = in_cat;

    -- Only proceed if both have data
    IF row_count_cat > 0 AND row_count_results > 0 THEN

        -- Get race mode and cp list
        SELECT racemode, cplist INTO race_mode, race_cp
        FROM t_event_cat
        WHERE cat = in_cat AND event_id = in_eventid;

        IF race_mode = 'LAP' THEN
            -- race_cp holds lap count (e.g., '3')
            SET i = 1;
            SET order_columns = '';
            WHILE i <= CAST(race_cp AS UNSIGNED) DO
                SET order_columns = CONCAT(order_columns, IF(i > 1, ',', ''), 'time', i);
                SET i = i + 1;
            END WHILE;
            SET where_conditions = ''; -- usually no need, but could add if required (e.g., time1>0)
        ELSE
            -- race_cp is comma-separated list, e.g., 'TimeCP1,TimeCP2,TimeFinish'
            SET @cp_list = race_cp;
            SET order_columns = '';
            SET where_conditions = 'DQ <> 1 and NR <> 1 AND TimeStart>0 and Timefinish>0 ';

            WHILE LOCATE(',', @cp_list) > 0 DO
                SET @cp = TRIM(SUBSTRING_INDEX(@cp_list, ',', 1));

                SET order_columns = CONCAT(order_columns, IF(order_columns != '', ',', ''), @cp);
                SET where_conditions = CONCAT(where_conditions, IF(where_conditions != '', ' AND ', ''), @cp, ' > 0');

                SET @cp_list = SUBSTRING(@cp_list, LOCATE(',', @cp_list) + 1);
            END WHILE;

            -- Add last cp
            SET @cp = TRIM(@cp_list);
            SET order_columns = CONCAT(order_columns, IF(order_columns != '', ',', ''), @cp);
            SET where_conditions = CONCAT(where_conditions, IF(where_conditions != '', ' AND ', ''), @cp, ' > 0');
        END IF;

        -- Build final dynamic SQL
        SET @sql_query = CONCAT(
            'WITH ranked AS (',
            ' SELECT pid, ROW_NUMBER() OVER (ORDER BY timeFinish) AS new_rank',
            ' FROM results ',
            ' WHERE EventId = "', in_eventid, '" AND DQ <> 1 and NR <> 1 AND cat = "', in_cat, '"',
            IF(where_conditions != '', CONCAT(' AND ', where_conditions), ''),
            ') ',
            'UPDATE results r ',
            'JOIN ranked rk ON r.pid = rk.pid ',
            'SET r.rank1cat = rk.new_rank;'
        );

        -- Log
        INSERT INTO servicelog(Query, Query_text, log_time)
        VALUES('P_ASSIGN_RANK1CAT', @sql_query, NOW());

        -- Execute
        PREPARE stmt FROM @sql_query;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;

    ELSE
        -- log if nothing to update
        INSERT INTO servicelog(Query, Query_text, log_time)
        VALUES('P_ASSIGN_RANK1CAT', CONCAT('Skipped: No data found in t_event_cat or results for eventid=', in_eventid, ' and cat=', in_cat), NOW());
    END IF;
END$$

DROP PROCEDURE IF EXISTS `P_ASSIGN_RANK1TOT`$$
CREATE PROCEDURE `P_ASSIGN_RANK1TOT`(
    IN in_eventid CHAR(36),
    IN in_dist VARCHAR(100))
BEGIN
    DECLARE race_mode TEXT;
    DECLARE race_cp TEXT;
    DECLARE order_columns TEXT DEFAULT '';
    DECLARE where_conditions TEXT DEFAULT '';
    DECLARE i INT DEFAULT 1;
    DECLARE row_count_results INT DEFAULT 0;
    DECLARE row_count_cat INT DEFAULT 0;

    -- Check existence in t_event_cat
    SELECT COUNT(*) INTO row_count_cat 
    FROM t_event_cat 
    WHERE event_id = in_eventid AND distance = in_dist;

    -- Check existence in t_results
    SELECT COUNT(*) INTO row_count_results
    FROM results 
    WHERE EventId = in_eventid AND distance = in_dist;

    -- Only proceed if both have data
    IF row_count_cat > 0 AND row_count_results > 0 THEN

        -- Get race mode and cp list
        SELECT racemode, cplist INTO race_mode, race_cp
        FROM t_event_cat
        WHERE distance = in_dist AND event_id = in_eventid
        limit 1;

        IF race_mode = 'LAP' THEN
            -- race_cp holds lap count (e.g., '3')
            SET i = 1;
            SET order_columns = '';
            WHILE i <= CAST(race_cp AS UNSIGNED) DO
                SET order_columns = CONCAT(order_columns, IF(i > 1, ',', ''), 'time', i);
                SET i = i + 1;
            END WHILE;
            SET where_conditions = ''; -- usually no need, but could add if required (e.g., time1>0)
        ELSE
            -- race_cp is comma-separated list, e.g., 'TimeCP1,TimeCP2,TimeFinish'
            SET @cp_list = race_cp;
            SET order_columns = '';
            SET where_conditions = 'DQ <> 1 and NR <> 1 AND  TimeStart>0 and Timefinish>0 ';

            WHILE LOCATE(',', @cp_list) > 0 DO
                SET @cp = TRIM(SUBSTRING_INDEX(@cp_list, ',', 1));

                SET order_columns = CONCAT(order_columns, IF(order_columns != '', ',', ''), @cp);
                SET where_conditions = CONCAT(where_conditions, IF(where_conditions != '', ' AND ', ''), @cp, ' > 0');

                SET @cp_list = SUBSTRING(@cp_list, LOCATE(',', @cp_list) + 1);
            END WHILE;

            -- Add last cp
            SET @cp = TRIM(@cp_list);
            SET order_columns = CONCAT(order_columns, IF(order_columns != '', ',', ''), @cp);
            SET where_conditions = CONCAT(where_conditions, IF(where_conditions != '', ' AND ', ''), @cp, ' > 0');
        END IF;

        -- Build final dynamic SQL
        SET @sql_query = CONCAT(
            'WITH ranked AS (',
            ' SELECT pid, ROW_NUMBER() OVER (ORDER BY timeFinish) AS new_rank',
            ' FROM results ',
            ' WHERE EventId = "', in_eventid, '" AND DQ <> 1 and NR <> 1 AND distance = "', in_dist, '"',
            IF(where_conditions != '', CONCAT(' AND ', where_conditions), ''),
            ') ',
            'UPDATE results r ',
            'JOIN ranked rk ON r.pid = rk.pid ',
            'SET r.rank1tot = rk.new_rank;'
        );

        -- Log
        INSERT INTO servicelog(Query, Query_text, log_time)
        VALUES('P_ASSIGN_RANK1TOT', @sql_query, NOW());

        -- Execute
        PREPARE stmt FROM @sql_query;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;

    ELSE
        -- log if nothing to update
        INSERT INTO servicelog(Query, Query_text, log_time)
        VALUES('P_ASSIGN_RANK1TOT', CONCAT('Skipped: No data found in t_event_cat or results for eventid=', in_eventid, ' and cat=', in_cat), NOW());
    END IF;
END$$

DROP PROCEDURE IF EXISTS `P_ASSIGN_RANK1MIX`$$
CREATE PROCEDURE `P_ASSIGN_RANK1MIX`(
    IN in_eventid CHAR(36),
    IN in_dist VARCHAR(100),
    IN in_gender VARCHAR(100))
BEGIN
    DECLARE race_mode TEXT;
    DECLARE race_cp TEXT;
    DECLARE order_columns TEXT DEFAULT '';
    DECLARE where_conditions TEXT DEFAULT '';
    DECLARE i INT DEFAULT 1;
    DECLARE row_count_results INT DEFAULT 0;
    DECLARE row_count_cat INT DEFAULT 0;

    -- Check existence in t_event_cat
    SELECT COUNT(*) INTO row_count_cat 
    FROM t_event_cat 
    WHERE event_id = in_eventid AND distance = in_dist;

    -- Check existence in t_results
    SELECT COUNT(*) INTO row_count_results
    FROM results 
    WHERE EventId = in_eventid AND distance = in_dist;

    -- Only proceed if both have data
    IF row_count_cat > 0 AND row_count_results > 0 THEN

        -- Get race mode and cp list
        SELECT racemode, cplist INTO race_mode, race_cp
        FROM t_event_cat
        WHERE distance = in_dist AND event_id = in_eventid
        limit 1;

        IF race_mode = 'LAP' THEN
            -- race_cp holds lap count (e.g., '3')
            SET i = 1;
            SET order_columns = '';
            WHILE i <= CAST(race_cp AS UNSIGNED) DO
                SET order_columns = CONCAT(order_columns, IF(i > 1, ',', ''), 'time', i);
                SET i = i + 1;
            END WHILE;
            SET where_conditions = ''; -- usually no need, but could add if required (e.g., time1>0)
        ELSE
            -- race_cp is comma-separated list, e.g., 'TimeCP1,TimeCP2,TimeFinish'
            SET @cp_list = race_cp;
            SET order_columns = '';
            SET where_conditions = 'DQ <> 1 and NR <> 1 AND  TimeStart>0 and Timefinish>0 ';

            WHILE LOCATE(',', @cp_list) > 0 DO
                SET @cp = TRIM(SUBSTRING_INDEX(@cp_list, ',', 1));

                SET order_columns = CONCAT(order_columns, IF(order_columns != '', ',', ''), @cp);
                SET where_conditions = CONCAT(where_conditions, IF(where_conditions != '', ' AND ', ''), @cp, ' > 0');

                SET @cp_list = SUBSTRING(@cp_list, LOCATE(',', @cp_list) + 1);
            END WHILE;

            -- Add last cp
            SET @cp = TRIM(@cp_list);
            SET order_columns = CONCAT(order_columns, IF(order_columns != '', ',', ''), @cp);
            SET where_conditions = CONCAT(where_conditions, IF(where_conditions != '', ' AND ', ''), @cp, ' > 0');
        END IF;

        -- Build final dynamic SQL
        SET @sql_query = CONCAT(
            'WITH ranked AS (',
            ' SELECT pid, ROW_NUMBER() OVER (ORDER BY timeFinish) AS new_rank',
            ' FROM results ',
            ' WHERE EventId = "', in_eventid, '" AND DQ <> 1 and NR <> 1 AND sex = "', in_gender, '" AND distance = "', in_dist, '"',
            IF(where_conditions != '', CONCAT(' AND ', where_conditions), ''),
            ') ',
            'UPDATE results r ',
            'JOIN ranked rk ON r.pid = rk.pid ',
            'SET r.rank1mix = rk.new_rank;'
        );

        -- Log
        INSERT INTO servicelog(Query, Query_text, log_time)
        VALUES('P_ASSIGN_RANK1MIX', @sql_query, NOW());

        -- Execute
        PREPARE stmt FROM @sql_query;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;

    ELSE
        -- log if nothing to update
        INSERT INTO servicelog(Query, Query_text, log_time)
        VALUES('P_ASSIGN_RANK1MIX', CONCAT('Skipped: No data found in t_event_cat or results for eventid=', in_eventid, ' and cat=', in_cat), NOW());
    END IF;
END$$

DROP PROCEDURE IF EXISTS `P_STATISTIC_00_REPORT`$$
CREATE PROCEDURE `P_STATISTIC_00_REPORT`(IN in_eventid CHAR(36))
BEGIN
UPDATE results SET nsbf=0,dns=0,dnf=0,nsbf=0,fs=0 WHERE eventid=in_eventid;
UPDATE results SET DNS = 1 WHERE eventid=in_eventid AND timestart =0 AND TRIM(name) <> '' AND name IS NOT NULL AND cat IS NOT NULL AND cat <>'';
UPDATE results SET DNF = 1 WHERE eventid=in_eventid AND  timestart >0 AND TRIM(name) <> '' AND name IS NOT NULL AND cat IS NOT NULL  AND cat <>'' AND timefinish = 0;
UPDATE results SET NSBF = 1 WHERE eventid=in_eventid AND  timestart = 0 AND  TRIM(name) <> '' AND name IS NOT NULL AND cat IS NOT NULL  AND cat <>'' AND timefinish > 0;
UPDATE results SET FS = 1 WHERE eventid=in_eventid AND  timestart <timeGun AND  TRIM(name) <> '' AND name IS NOT NULL AND cat IS NOT NULL  AND cat <>'' AND timestart>0 AND timegun>0;

SELECT
    'Statistic' AS STATISTIC,
    Cat,
    IFNULL(category, 'UNASSIGNED_CAT') AS CATEGORY,
    COUNT(*) AS REGISTERED,
    SUM(timestart  > 0) AS STARTED,
    SUM(DNS > 0) AS DID_NOT_START,
    SUM(timestart > 0 AND TimeFinish > 0) AS FINISHED,
    SUM(DNF > 0) AS DID_NOT_FINISH,
    SUM(FS > 0) AS FALSE_START,
    SUM(NSBF > 0) AS NO_START_BUT_FINISHED,
    SUM(DQ > 0) AS Disqualified,
    distance
FROM results
WHERE category IS NOT NULL AND eventid=in_eventid
GROUP BY cat,category,distance

UNION ALL

SELECT
    'zStatistic',
    ' ' AS Cat,
    CONCAT('SubTotal: ',ROUND(distance),'KM'),
    COUNT(*),
    SUM(timestart > 0),
    SUM(DNS > 0),
    SUM(timestart > 0 AND TimeFinish > 0),
    SUM(DNF > 0),
    SUM(FS > 0),
    SUM(NSBF > 0),
    SUM(DQ > 0),
    distance
FROM results
WHERE category IS NOT NULL AND eventid=in_eventid
GROUP BY distance

UNION ALL

SELECT
    'Statistic',
    'Z' AS Cat,
    'Z_TOTAL',
    COUNT(*),
    SUM(timestart > 0),
    SUM(DNS > 0),
    SUM(timestart > 0 AND TimeFinish > 0),
    SUM(DNF > 0),
    SUM(FS > 0),
    SUM(NSBF > 0),
    SUM(DQ > 0),
    0
FROM results
WHERE category IS NOT NULL AND eventid=in_eventid;
END$$

DROP PROCEDURE IF EXISTS `P_STATISTIC_00_REGLIST`$$
CREATE PROCEDURE `P_STATISTIC_00_REGLIST`(IN in_eventid CHAR(36))
BEGIN
    SELECT 'Registered' AS item, category, bib, name 
    FROM results 
    WHERE eventid=in_eventid AND category IS NOT NULL AND category <>'' 
    ORDER BY 2,3;
END$$

DROP PROCEDURE IF EXISTS `P_STATISTIC_01_STARTLIST`$$
CREATE PROCEDURE `P_STATISTIC_01_STARTLIST`(IN in_eventid CHAR(36))
BEGIN
    SELECT 'StartList' AS item, category, bib, name, 
           CONCAT('''', _formatTime(timestart)) AS TimeStart,
           CONCAT('''', _formatTime(timeGun)) AS TimeGun 
    FROM results 
    WHERE eventid=in_eventid AND timestart > 0 AND category IS NOT NULL AND category <>'' 
    ORDER BY cat;
END$$

DROP PROCEDURE IF EXISTS `P_STATISTIC_02_DNS`$$
CREATE PROCEDURE `P_STATISTIC_02_DNS`(IN in_eventid CHAR(36))
BEGIN
    SELECT 'DNS' AS item, category, bib, name,
           CONCAT('''', _formatTime(timestart)) AS TimeStart,
           CONCAT('''', _formatTime(timeGun)) AS TimeGun 
    FROM results 
    WHERE eventid=in_eventid AND dns=1 
    ORDER BY cat;
END$$

DROP PROCEDURE IF EXISTS `P_STATISTIC_03_FINISHED`$$
CREATE PROCEDURE `P_STATISTIC_03_FINISHED`(IN in_eventid CHAR(36))
BEGIN
    SELECT 'Finished' AS item, category, bib, name,
           CONCAT('''', _formatTime(timestart)) AS TimeStart,
           CONCAT('''', _formatTime(timeGun)) AS TimeGun,
           CONCAT('''', _formatTime(timefinish)) AS TimeFinish 
    FROM results 
    WHERE eventid=in_eventid AND timestart > 0 AND TimeFinish > 0 
    ORDER BY cat;
END$$

DROP PROCEDURE IF EXISTS `P_STATISTIC_04_DNF`$$
CREATE PROCEDURE `P_STATISTIC_04_DNF`(IN in_eventid CHAR(36))
BEGIN
    SELECT 'DNF' AS item, category, bib, name,
           CONCAT('''', _formatTime(timestart)) AS TimeStart,
           CONCAT('''', _formatTime(timeGun)) AS TimeGun,
           CONCAT('''', _formatTime(timefinish)) AS TimeFinish 
    FROM results 
    WHERE eventid=in_eventid AND DNF=1 
    ORDER BY cat;
END$$

DROP PROCEDURE IF EXISTS `P_STATISTIC_05_FS`$$
CREATE PROCEDURE `P_STATISTIC_05_FS`(IN in_eventid CHAR(36))
BEGIN
    SELECT 'FALSE_START' AS item, category, bib, name,
           CONCAT('''', _formatTime(timestart)) AS TimeStart,
           CONCAT('''', _formatTime(timegun)) AS TimeGun 
    FROM results 
    WHERE eventid=in_eventid AND FS=1 
    ORDER BY cat;
END$$

DROP PROCEDURE IF EXISTS `P_STATISTIC_06_NSBF`$$
CREATE PROCEDURE `P_STATISTIC_06_NSBF`(IN in_eventid CHAR(36))
BEGIN
    SELECT 'NSBF' AS item, category, bib, name,
           CONCAT('''', _formatTime(timestart)) AS TimeStart,
           CONCAT('''', _formatTime(timeGun)) AS TimeGun,
           CONCAT('''', _formatTime(timefinish)) AS TimeFinish 
    FROM results 
    WHERE eventid=in_eventid AND NSBF=1 
    ORDER BY cat, bib;
END$$

DROP PROCEDURE IF EXISTS `P_STATISTIC_07_DQ`$$
CREATE PROCEDURE `P_STATISTIC_07_DQ`(IN in_eventid CHAR(36))
BEGIN
    SELECT 'DQ' AS item, category, bib, name,
           CONCAT('''', _formatTime(timestart)) AS TimeStart,
           CONCAT('''', _formatTime(timeGun)) AS TimeGun,
           CONCAT('''', _formatTime(timefinish)) AS TimeFinish,
           Remark 
    FROM results 
    WHERE eventid=in_eventid AND DQ=1 
    ORDER BY cat, bib;
END$$

DROP PROCEDURE IF EXISTS `P_DUMMY_REPORT`$$
CREATE PROCEDURE `P_DUMMY_REPORT`(
    IN p_event_id VARCHAR(36),
    IN p_category VARCHAR(10),
    IN p_timegun INT
)
BEGIN
    DECLARE v_cplist TEXT;
    DECLARE v_cp_count INT DEFAULT 0;
    DECLARE v_cp_name VARCHAR(20);
    DECLARE v_remaining TEXT;
    DECLARE v_comma_pos INT;

    -- Get cplist from t_event_cat
    SELECT cplist INTO v_cplist
    FROM t_event_cat
    WHERE event_id = p_event_id AND cat = p_category
    LIMIT 1;

    -- Count checkpoints
    IF v_cplist IS NOT NULL AND v_cplist != '' THEN
        SET v_cp_count = (LENGTH(v_cplist) - LENGTH(REPLACE(v_cplist, ',', '')) + 1);
    END IF;

    -- Update results table for all participants in this event/category
    UPDATE results r
    SET
        -- Set gun time
        r.timegun = p_timegun,

        -- Set start time (randomly 1-60 seconds after gun time)
        r.timestart = p_timegun + FLOOR(1000 + RAND() * 59000),

        -- Set checkpoint times (20-25 minutes gap between each)
        r.timecp1 = CASE
            WHEN v_cp_count >= 1 THEN p_timegun + FLOOR(1200000 + RAND() * 300000) -- 20-25 min
            ELSE 0
        END,

        r.timecp2 = CASE
            WHEN v_cp_count >= 2 THEN r.timecp1 + FLOOR(1200000 + RAND() * 300000) -- +20-25 min
            ELSE 0
        END,

        r.timecp3 = CASE
            WHEN v_cp_count >= 3 THEN r.timecp2 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0
        END,

        r.timecp4 = CASE
            WHEN v_cp_count >= 4 THEN r.timecp3 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0
        END,

        r.timecp5 = CASE
            WHEN v_cp_count >= 5 THEN r.timecp4 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0
        END,
        
        r.timecp6 = CASE
            WHEN v_cp_count >= 6 THEN r.timecp5 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0
        END,

        r.timecp7 = CASE
            WHEN v_cp_count >= 7 THEN r.timecp6 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0
        END,

        r.timecp8 = CASE
            WHEN v_cp_count >= 8 THEN r.timecp7 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0
        END,

        r.timecp9 = CASE
            WHEN v_cp_count >= 9 THEN r.timecp8 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0
        END,

        r.timecp10 = CASE
            WHEN v_cp_count >= 10 THEN r.timecp9 + FLOOR(1200000 + RAND() * 300000)
            ELSE 0
        END,

        -- Set finish time (20-30 minutes after last checkpoint or start if no checkpoints)
        r.timefinish = CASE
            WHEN v_cp_count >= 10 THEN r.timecp10 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 9 THEN r.timecp9 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 8 THEN r.timecp8 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 7 THEN r.timecp7 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 6 THEN r.timecp6 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 5 THEN r.timecp5 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 4 THEN r.timecp4 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 3 THEN r.timecp3 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 2 THEN r.timecp2 + FLOOR(1200000 + RAND() * 600000)
            WHEN v_cp_count >= 1 THEN r.timecp1 + FLOOR(1200000 + RAND() * 600000)
            ELSE r.timestart + FLOOR(1200000 + RAND() * 600000)
        END,

        -- Clear status flags
        r.dnf = 0,
        r.dq = 0,
        r.dns = 0,
        r.fs = 0,
        r.nsbf = 0,
        r.nr = 0

    WHERE r.eventid = p_event_id AND r.cat = p_category;

    -- Log completion
    INSERT INTO servicelog(Query, Query_text, log_time)
    VALUES('P_DUMMY_REPORT',
           CONCAT('Generated dummy data for eventid=', p_event_id, ', cat=', p_category, ', participants=', ROW_COUNT()),
           NOW());

END$$

DROP PROCEDURE IF EXISTS `P_DUMMY_REPORT_CLEAR`$$
CREATE PROCEDURE `P_DUMMY_REPORT_CLEAR`(
    IN p_event_id VARCHAR(36),
    IN p_category VARCHAR(10)
)
BEGIN
    -- Reset all race-related fields to 0 or NULL
    UPDATE results r
    SET
        -- Reset times
        r.timegun = 0,
        r.timestart = 0,
        r.timefinish = 0,
        r.timecp1 = 0,
        r.timecp2 = 0,
        r.timecp3 = 0,
        r.timecp4 = 0,
        r.timecp5 = 0,
        r.timecp6 = 0,
        r.timecp7 = 0,
        r.timecp8 = 0,
        r.timecp9 = 0,
        r.timecp10 = 0,

        -- Reset ranks
        r.rank1cat = 0,
        r.rank1mix = 0,
        r.rank1tot = 0,

        -- Reset status flags
        r.nr = 0,
        r.fs = 0,
        r.dnf = 0,
        r.dns = 0,
        r.dq = 0,
        r.nsbf = 0

    WHERE r.eventid = p_event_id AND r.cat = p_category;

    -- Log completion
    INSERT INTO servicelog(Query, Query_text, log_time)
    VALUES('P_DUMMY_REPORT_CLEAR',
           CONCAT('Cleared all race data for eventid=', p_event_id, ', cat=', p_category, ', participants=', ROW_COUNT()),
           NOW());

END$$

DELIMITER ;
