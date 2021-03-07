-- MariaDB dump 10.19  Distrib 10.5.9-MariaDB, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: messageboard
-- ------------------------------------------------------
-- Server version	10.5.9-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attachment` (
  `attachment_uuid` char(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `attachment_name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`attachment_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attachment`
--

LOCK TABLES `attachment` WRITE;
/*!40000 ALTER TABLE `attachment` DISABLE KEYS */;
INSERT INTO `attachment` VALUES ('3cff7959-b715-44f6-b72e-77c11ef83580','TIM图片20191212125559.jpg');
/*!40000 ALTER TABLE `attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `like_num` int(11) DEFAULT NULL,
  `dislike_num` int(11) DEFAULT NULL,
  `content` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pub_date` datetime DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,5,3,'asdasdsa','2021-02-03 16:32:08',2),(2,5,2,'1adasd','2021-02-03 16:32:19',3),(3,11,3,'3','2021-02-03 16:34:31',2),(4,5,4,'sfsf','2021-02-03 16:34:43',2),(5,5,5,'sdfsdfsdfdsf','2021-02-03 16:34:54',3),(6,5,122,'ssdfsdf','2021-02-03 16:35:07',2),(7,5,34242,'sdfdsfs','2021-02-03 16:35:16',2),(8,4,222,'sdsfsdf','2021-02-03 16:35:27',3),(9,112,23,'33','2021-02-03 16:35:34',3),(10,4,33,'44','2021-02-03 16:35:43',2),(11,4,44,'55','2021-02-03 16:35:51',2),(12,2,1,'大苏打','2021-02-05 20:59:33',3),(13,1,1,'💻🐱','2021-03-07 00:01:28',3);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `keyword`
--

DROP TABLE IF EXISTS `keyword`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `keyword` (
  `keyword_id` int(11) NOT NULL AUTO_INCREMENT,
  `keyword` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`keyword_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `keyword`
--

LOCK TABLES `keyword` WRITE;
/*!40000 ALTER TABLE `keyword` DISABLE KEYS */;
/*!40000 ALTER TABLE `keyword` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reply`
--

DROP TABLE IF EXISTS `reply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reply` (
  `reply_id` int(11) NOT NULL AUTO_INCREMENT,
  `from_user_id` int(11) NOT NULL,
  `to_comment_id` int(11) NOT NULL,
  `publish_date` datetime DEFAULT NULL,
  `content` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`reply_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reply`
--

LOCK TABLES `reply` WRITE;
/*!40000 ALTER TABLE `reply` DISABLE KEYS */;
INSERT INTO `reply` VALUES (1,2,1,'2021-02-05 01:50:19','sdasdad'),(4,3,1,'2021-02-05 02:13:31','123'),(5,3,1,'2021-02-05 02:13:54','asd'),(7,3,12,'2021-02-05 21:17:23','阿萨德'),(8,3,12,'2021-02-06 21:41:27','啊是哒是哒'),(16,3,1,'2021-03-07 18:51:28','123'),(17,3,1,'2021-03-07 18:51:30','123'),(18,3,1,'2021-03-07 18:51:32','123'),(19,3,1,'2021-03-07 18:51:35','啊所打动'),(20,3,1,'2021-03-07 18:51:37','飞洒发生'),(21,3,3,'2021-03-07 19:28:35','12'),(25,3,13,'2021-03-07 20:10:30','啊是的撒的'),(26,3,13,'2021-03-07 20:10:33','😀😀'),(27,3,13,'2021-03-07 20:11:11','阿斯顿阿丁😀😀');
/*!40000 ALTER TABLE `reply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_joined` datetime NOT NULL,
  `is_superuser` tinyint(4) NOT NULL DEFAULT 0,
  `signature` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatar_uuid` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_username_uindex` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (2,'kale2','zwG70YtbHqjE5Shz:a1b29cfce22345280e0574160b346ca9600781551b1eff11eb13bfc12ae8830c','2021-01-31 21:56:02',0,'这个人很懒, 什么都没有留下','3cff7959-b715-44f6-b72e-77c11ef83580'),(3,'kale','zwG70YtbHqjE5Shz:a1b29cfce22345280e0574160b346ca9600781551b1eff11eb13bfc12ae8830c','2021-01-31 22:10:24',1,'阿萨德撒大撒大撒大撒','3cff7959-b715-44f6-b72e-77c11ef83580'),(4,'123','Sax8B4eKsThnW4qS:132668f5916aa48598a3f572e2bc692446288278c34d5387189eb449d7a060aa','2021-02-09 15:23:53',0,'这个人很懒, 什么都没有留下',NULL),(5,'1234','XOE4oTqooFc9gvkl:b482684e05ee44a31525fc8b5df101c3a6fc74b34e6eeeceb7d92cf50c2c8b31','2021-02-09 15:24:29',0,'这个人很懒, 什么都没有留下',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-03-07 20:30:20
