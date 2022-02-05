-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: test_banking
-- ------------------------------------------------------
-- Server version	8.0.27

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
-- Table structure for table `account_status`
--

DROP TABLE IF EXISTS `account_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_status` (
  `id_status` int NOT NULL AUTO_INCREMENT,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`id_status`),
  UNIQUE KEY `description_UNIQUE` (`description`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_status`
--

LOCK TABLES `account_status` WRITE;
/*!40000 ALTER TABLE `account_status` DISABLE KEYS */;
INSERT INTO `account_status` VALUES (1,'Blocked'),(4,'Pending approval'),(2,'Suspended'),(3,'Unlocked');
/*!40000 ALTER TABLE `account_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `id_accounts` int NOT NULL AUTO_INCREMENT,
  `balance` double NOT NULL DEFAULT '0',
  `account_number` varchar(20) NOT NULL,
  `yearly_interest_rate` double NOT NULL,
  `id_account_status` int NOT NULL,
  PRIMARY KEY (`id_accounts`),
  UNIQUE KEY `account_number` (`account_number`),
  KEY `fk_accounts_account_status_idx` (`id_account_status`),
  CONSTRAINT `fk_accounts_account_status` FOREIGN KEY (`id_account_status`) REFERENCES `account_status` (`id_status`)
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,1000299,'BY000000000000000000',0,3),(2,1500,'BY789009876543345611',1.2,3),(3,5500,'BY888899997777123456',-3,3);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank_cards`
--

DROP TABLE IF EXISTS `bank_cards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bank_cards` (
  `id_bank_cards` int NOT NULL AUTO_INCREMENT,
  `credit_card_number` varchar(45) NOT NULL,
  `cvc_code` varchar(3) NOT NULL,
  `pin_code` varchar(4) NOT NULL,
  `expiration_date` date NOT NULL,
  `registration_date` date NOT NULL,
  `balance` double DEFAULT NULL,
  `overdraft_maximum` double DEFAULT NULL,
  `overdraft_interest_rate` double DEFAULT NULL,
  `id_user` int NOT NULL,
  `id_account` int DEFAULT NULL,
  `id_card_type` int NOT NULL,
  `id_card_status` int NOT NULL,
  PRIMARY KEY (`id_bank_cards`),
  UNIQUE KEY `uc_num_cvc` (`credit_card_number`,`cvc_code`),
  KEY `fk_credit_cards_card_types1_idx` (`id_card_type`),
  KEY `fk_bank_cards_card_status1_idx` (`id_card_status`),
  KEY `fk_bank_cards_accounts1_idx` (`id_account`),
  KEY `fk_bank_cards_users1_idx` (`id_user`),
  CONSTRAINT `fk_bank_cards_accounts1` FOREIGN KEY (`id_account`) REFERENCES `accounts` (`id_accounts`) ON DELETE CASCADE,
  CONSTRAINT `fk_bank_cards_card_status1` FOREIGN KEY (`id_card_status`) REFERENCES `card_status` (`id_status`),
  CONSTRAINT `fk_bank_cards_users1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_users`) ON DELETE CASCADE,
  CONSTRAINT `fk_credit_cards_card_types1` FOREIGN KEY (`id_card_type`) REFERENCES `card_types` (`id_card_type`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank_cards`
--

LOCK TABLES `bank_cards` WRITE;
/*!40000 ALTER TABLE `bank_cards` DISABLE KEYS */;
INSERT INTO `bank_cards` VALUES (1,'0000111122223333','132','4545','2026-02-01','2022-02-01',NULL,NULL,NULL,2,2,1,1),(2,'4444555566667777','666','1488','2025-03-12','2021-03-12',500,NULL,5,2,NULL,2,1),(3,'9999000099990000','123','5678','2022-02-02','2018-02-02',NULL,NULL,NULL,1,1,1,3),(4,'1234567890098765','777','1555','2024-12-20','2020-12-20',NULL,1200,3,2,1,3,1),(5,'4444555566667777','180','6790','2025-10-17','2021-10-17',NULL,NULL,NULL,1,3,1,2);
/*!40000 ALTER TABLE `bank_cards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill_status`
--

DROP TABLE IF EXISTS `bill_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill_status` (
  `id_bill_status` int NOT NULL AUTO_INCREMENT,
  `status` varchar(45) NOT NULL,
  PRIMARY KEY (`id_bill_status`),
  UNIQUE KEY `status_UNIQUE` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_status`
--

LOCK TABLES `bill_status` WRITE;
/*!40000 ALTER TABLE `bill_status` DISABLE KEYS */;
INSERT INTO `bill_status` VALUES (1,'awaiting payment'),(2,'closed'),(3,'overdue'),(4,'requested');
/*!40000 ALTER TABLE `bill_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bills`
--

DROP TABLE IF EXISTS `bills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bills` (
  `id_bills` int NOT NULL AUTO_INCREMENT,
  `value` double NOT NULL,
  `issue_date` date NOT NULL,
  `due_date` date DEFAULT NULL,
  `notice` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `id_user` int NOT NULL,
  `bearer_id` int NOT NULL,
  `id_payment_account` int NOT NULL,
  `id_status` int NOT NULL,
  `id_penalty` int DEFAULT NULL,
  `id_loans` int DEFAULT NULL,
  PRIMARY KEY (`id_bills`),
  KEY `fk_bills_bill_status1_idx` (`id_status`),
  KEY `fk_bills_penalties1_idx` (`id_penalty`),
  KEY `fk_bills_users1_idx` (`id_user`),
  KEY `fk_bills_accounts1_idx` (`id_payment_account`),
  KEY `fk_bills_loans1_idx` (`id_loans`),
  KEY `bearer_id` (`bearer_id`),
  CONSTRAINT `bills_ibfk_1` FOREIGN KEY (`bearer_id`) REFERENCES `users` (`id_users`) ON DELETE CASCADE,
  CONSTRAINT `fk_bills_accounts1` FOREIGN KEY (`id_payment_account`) REFERENCES `accounts` (`id_accounts`) ON DELETE CASCADE,
  CONSTRAINT `fk_bills_bill_status1` FOREIGN KEY (`id_status`) REFERENCES `bill_status` (`id_bill_status`),
  CONSTRAINT `fk_bills_loans1` FOREIGN KEY (`id_loans`) REFERENCES `loans` (`id_loans`) ON DELETE CASCADE,
  CONSTRAINT `fk_bills_penalties1` FOREIGN KEY (`id_penalty`) REFERENCES `penalties` (`id_penalties`) ON DELETE CASCADE,
  CONSTRAINT `fk_bills_users1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_users`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bills`
--

LOCK TABLES `bills` WRITE;
/*!40000 ALTER TABLE `bills` DISABLE KEYS */;
INSERT INTO `bills` VALUES (1,500,'2022-02-04','2022-06-04','Pay me',2,1,1,1,NULL,NULL),(2,300,'2022-02-03',NULL,'Gib money',1,2,2,1,NULL,NULL),(3,1500,'2022-01-20',NULL,NULL,2,1,1,1,NULL,NULL);
/*!40000 ALTER TABLE `bills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `card_status`
--

DROP TABLE IF EXISTS `card_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `card_status` (
  `id_status` int NOT NULL AUTO_INCREMENT,
  `status` varchar(45) NOT NULL,
  PRIMARY KEY (`id_status`),
  UNIQUE KEY `status_UNIQUE` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card_status`
--

LOCK TABLES `card_status` WRITE;
/*!40000 ALTER TABLE `card_status` DISABLE KEYS */;
INSERT INTO `card_status` VALUES (3,'expired'),(2,'locked'),(4,'pending'),(1,'unlocked');
/*!40000 ALTER TABLE `card_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `card_types`
--

DROP TABLE IF EXISTS `card_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `card_types` (
  `id_card_type` int NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id_card_type`),
  UNIQUE KEY `type_UNIQUE` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card_types`
--

LOCK TABLES `card_types` WRITE;
/*!40000 ALTER TABLE `card_types` DISABLE KEYS */;
INSERT INTO `card_types` VALUES (2,'Credit card'),(1,'Debit card'),(3,'Overdraft card');
/*!40000 ALTER TABLE `card_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loan_status`
--

DROP TABLE IF EXISTS `loan_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loan_status` (
  `id_loan_status` int NOT NULL AUTO_INCREMENT,
  `status_desc` varchar(45) NOT NULL,
  PRIMARY KEY (`id_loan_status`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loan_status`
--

LOCK TABLES `loan_status` WRITE;
/*!40000 ALTER TABLE `loan_status` DISABLE KEYS */;
INSERT INTO `loan_status` VALUES (1,'Closed'),(2,'Pending'),(3,'Overdue');
/*!40000 ALTER TABLE `loan_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loans`
--

DROP TABLE IF EXISTS `loans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loans` (
  `id_loans` int NOT NULL AUTO_INCREMENT,
  `single_payment_value` double NOT NULL,
  `starting_value` double NOT NULL,
  `total_payment_value` double NOT NULL,
  `yearly_interest_rate` double NOT NULL,
  `date_of_issue` date NOT NULL,
  `due_date` date NOT NULL,
  `users_id_users` int NOT NULL,
  `id_status` int NOT NULL,
  `id_card` int DEFAULT NULL,
  `id_account` int NOT NULL,
  PRIMARY KEY (`id_loans`),
  KEY `fk_loans_users1_idx` (`users_id_users`),
  KEY `fk_loans_loan_status` (`id_status`),
  KEY `id_account` (`id_account`),
  KEY `id_card` (`id_card`),
  CONSTRAINT `fk_loans_loan_status` FOREIGN KEY (`id_status`) REFERENCES `loan_status` (`id_loan_status`),
  CONSTRAINT `fk_loans_users1` FOREIGN KEY (`users_id_users`) REFERENCES `users` (`id_users`) ON DELETE CASCADE,
  CONSTRAINT `loans_ibfk_2` FOREIGN KEY (`id_account`) REFERENCES `accounts` (`id_accounts`) ON DELETE CASCADE,
  CONSTRAINT `loans_ibfk_3` FOREIGN KEY (`id_card`) REFERENCES `bank_cards` (`id_bank_cards`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loans`
--

LOCK TABLES `loans` WRITE;
/*!40000 ALTER TABLE `loans` DISABLE KEYS */;
INSERT INTO `loans` VALUES (1,50,500,600,20,'2020-01-09','2021-01-09',2,2,NULL,2),(2,50.417,1000,1210,10,'2021-05-18','2023-05-18',2,1,NULL,3),(3,1045.769,10000,75295.36,40,'2019-02-16','2025-02-16',1,2,NULL,2);
/*!40000 ALTER TABLE `loans` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation_types`
--

DROP TABLE IF EXISTS `operation_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operation_types` (
  `id_operation` int NOT NULL AUTO_INCREMENT,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`id_operation`),
  UNIQUE KEY `description_UNIQUE` (`description`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation_types`
--

LOCK TABLES `operation_types` WRITE;
/*!40000 ALTER TABLE `operation_types` DISABLE KEYS */;
INSERT INTO `operation_types` VALUES (1,'Account block'),(2,'Account suspension'),(3,'Account unlock'),(13,'Accrual'),(11,'Card expiration'),(4,'Card lock'),(5,'Card unlock'),(6,'Transfer account/account'),(7,'Transfer account/card'),(8,'Transfer card/account'),(9,'Transfer card/card');
/*!40000 ALTER TABLE `operation_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operations`
--

DROP TABLE IF EXISTS `operations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operations` (
  `id_operations` int NOT NULL AUTO_INCREMENT,
  `value` double DEFAULT NULL,
  `commission` double DEFAULT NULL,
  `operation_date` datetime NOT NULL,
  `id_operation_type` int NOT NULL,
  `id_account` int DEFAULT NULL,
  `id_target_account` int DEFAULT NULL,
  `id_bank_card` int DEFAULT NULL,
  `id_target_bank_card` int DEFAULT NULL,
  `id_bill` int DEFAULT NULL,
  `id_penalty` int DEFAULT NULL,
  PRIMARY KEY (`id_operations`),
  KEY `fk_operations_operation_types1_idx` (`id_operation_type`),
  KEY `fk_operations_accounts1_idx` (`id_account`),
  KEY `fk_operations_accounts2_idx` (`id_target_account`),
  KEY `fk_operations_bank_cards1_idx` (`id_bank_card`),
  KEY `fk_operations_bank_cards2_idx` (`id_target_bank_card`),
  KEY `fk_operations_bills` (`id_bill`),
  KEY `fk_penalties_operations` (`id_penalty`),
  CONSTRAINT `fk_operations_accounts1` FOREIGN KEY (`id_account`) REFERENCES `accounts` (`id_accounts`) ON DELETE CASCADE,
  CONSTRAINT `fk_operations_accounts2` FOREIGN KEY (`id_target_account`) REFERENCES `accounts` (`id_accounts`) ON DELETE CASCADE,
  CONSTRAINT `fk_operations_bank_cards1` FOREIGN KEY (`id_bank_card`) REFERENCES `bank_cards` (`id_bank_cards`) ON DELETE CASCADE,
  CONSTRAINT `fk_operations_bank_cards2` FOREIGN KEY (`id_target_bank_card`) REFERENCES `bank_cards` (`id_bank_cards`) ON DELETE CASCADE,
  CONSTRAINT `fk_operations_bills` FOREIGN KEY (`id_bill`) REFERENCES `bills` (`id_bills`) ON DELETE CASCADE,
  CONSTRAINT `fk_operations_operation_types1` FOREIGN KEY (`id_operation_type`) REFERENCES `operation_types` (`id_operation`),
  CONSTRAINT `fk_penalties_operations` FOREIGN KEY (`id_penalty`) REFERENCES `penalties` (`id_penalties`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=312 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operations`
--

LOCK TABLES `operations` WRITE;
/*!40000 ALTER TABLE `operations` DISABLE KEYS */;
INSERT INTO `operations` VALUES (1,304,6,'2022-02-03 14:02:00',6,2,1,NULL,NULL,NULL,NULL),(2,NULL,NULL,'2022-01-01 01:03:00',3,3,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `operations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `penalties`
--

DROP TABLE IF EXISTS `penalties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `penalties` (
  `id_penalties` int NOT NULL AUTO_INCREMENT,
  `value` double DEFAULT NULL,
  `notice` text,
  `id_payment_account` int DEFAULT NULL,
  `id_type` int NOT NULL,
  `id_user` int NOT NULL,
  `id_status` int NOT NULL,
  PRIMARY KEY (`id_penalties`),
  KEY `fk_penalties_penalty_type1_idx` (`id_type`),
  KEY `id_payment_account` (`id_payment_account`),
  KEY `id_status` (`id_status`),
  KEY `fk_penalties_users` (`id_user`),
  CONSTRAINT `fk_penalties_accounts` FOREIGN KEY (`id_payment_account`) REFERENCES `accounts` (`id_accounts`) ON DELETE CASCADE,
  CONSTRAINT `fk_penalties_penalty_type1` FOREIGN KEY (`id_type`) REFERENCES `penalty_type` (`id_penalty_type`),
  CONSTRAINT `fk_penalties_users` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_users`) ON DELETE CASCADE,
  CONSTRAINT `penalties_ibfk_3` FOREIGN KEY (`id_status`) REFERENCES `penalty_status` (`id_penalty_status`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `penalties`
--

LOCK TABLES `penalties` WRITE;
/*!40000 ALTER TABLE `penalties` DISABLE KEYS */;
INSERT INTO `penalties` VALUES (1,666,NULL,1,4,2,1),(2,NULL,NULL,NULL,1,2,1),(3,NULL,NULL,NULL,3,2,4);
/*!40000 ALTER TABLE `penalties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `penalty_status`
--

DROP TABLE IF EXISTS `penalty_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `penalty_status` (
  `id_penalty_status` int NOT NULL AUTO_INCREMENT,
  `status` varchar(45) NOT NULL,
  PRIMARY KEY (`id_penalty_status`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `penalty_status`
--

LOCK TABLES `penalty_status` WRITE;
/*!40000 ALTER TABLE `penalty_status` DISABLE KEYS */;
INSERT INTO `penalty_status` VALUES (1,'pending'),(2,'unassigned'),(3,'closed'),(4,'inflicted');
/*!40000 ALTER TABLE `penalty_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `penalty_type`
--

DROP TABLE IF EXISTS `penalty_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `penalty_type` (
  `id_penalty_type` int NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id_penalty_type`),
  UNIQUE KEY `type_UNIQUE` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `penalty_type`
--

LOCK TABLES `penalty_type` WRITE;
/*!40000 ALTER TABLE `penalty_type` DISABLE KEYS */;
INSERT INTO `penalty_type` VALUES (2,'Accounts lock'),(1,'Accounts suspension'),(3,'Cards lock'),(4,'Fee'),(5,'Lawsuit');
/*!40000 ALTER TABLE `penalty_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `id_roles` int NOT NULL AUTO_INCREMENT,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`id_roles`),
  UNIQUE KEY `description_UNIQUE` (`description`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,'Admin'),(4,'Banned'),(3,'Employee'),(2,'User');
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id_users` int NOT NULL AUTO_INCREMENT,
  `email` varchar(320) NOT NULL,
  `password` varchar(256) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `patronymic` varchar(45) DEFAULT NULL,
  `passport_series` varchar(3) NOT NULL,
  `passport_number` varchar(15) NOT NULL,
  `birthdate` date NOT NULL,
  `last_login` date NOT NULL,
  `created` date NOT NULL,
  `id_role` int NOT NULL,
  PRIMARY KEY (`id_users`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `fk_users_user_roles1_idx` (`id_role`),
  CONSTRAINT `fk_users_user_roles1` FOREIGN KEY (`id_role`) REFERENCES `user_roles` (`id_roles`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'default@user.com','$2a$10$mnQkYzs2Aa3.KScPtBRGCesSpJJ9hIyjqHIpTBLHY.WwTfR5Hu77G','Поумолчаниев','Иван','Evstafjevich','DD','1234567','1980-01-01','2022-02-04','2022-02-04',2),(2,'dead@insi.de','$2a$12$sv8YIDZ3bGkAvgtk8mtwg.rxO.HcMo9EK3bJUJ.YXBsVM6e81YDhS','Канеки','Кук',NULL,'GH','0010007','2000-12-20','2022-02-04','2022-02-04',2);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_has_accounts`
--

DROP TABLE IF EXISTS `users_has_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_has_accounts` (
  `id_user` int NOT NULL,
  `id_account` int NOT NULL,
  PRIMARY KEY (`id_user`,`id_account`),
  KEY `fk_users_has_accounts_accounts1_idx` (`id_account`),
  KEY `fk_users_has_accounts_users1_idx` (`id_user`),
  CONSTRAINT `fk_users_has_accounts_accounts1` FOREIGN KEY (`id_account`) REFERENCES `accounts` (`id_accounts`) ON DELETE CASCADE,
  CONSTRAINT `fk_users_has_accounts_users1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_users`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_has_accounts`
--

LOCK TABLES `users_has_accounts` WRITE;
/*!40000 ALTER TABLE `users_has_accounts` DISABLE KEYS */;
INSERT INTO `users_has_accounts` VALUES (1,1),(1,2),(2,2),(1,3),(2,3);
/*!40000 ALTER TABLE `users_has_accounts` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-05 18:03:02
