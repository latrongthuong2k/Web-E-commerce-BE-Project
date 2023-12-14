-- MySQL dump 10.13  Distrib 8.0.34, for macos13 (arm64)
--
-- Host: 127.0.0.1    Database: MD4-DB
-- ------------------------------------------------------
-- Server version	8.0.34
# CREATE DATABASE IF NOT EXISTS  ;
# USE ecommerce_db;
# docker exec -i mysql-ecommerce-container: mysql -u root -pthuong191020 <database_name> < /file.sql
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

--
-- Table structure for table `bills`
--

DROP TABLE IF EXISTS `bills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bills` (
  `id` int NOT NULL AUTO_INCREMENT,
  `approve_by` int DEFAULT NULL,
  `approve_date` datetime(6) DEFAULT NULL,
  `bill_status` enum('APPROVE','CANCEL','CREATE') DEFAULT NULL,
  `create_by` int NOT NULL,
  `create_date` datetime(6) NOT NULL,
  `sale_price` decimal(15,2) NOT NULL,
  `quantity` int DEFAULT NULL,
  `bill_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa8qbmvkm08518tekr5svcf816` (`bill_id`),
  CONSTRAINT `FKa8qbmvkm08518tekr5svcf816` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bills`
--

LOCK TABLES `bills` WRITE;
/*!40000 ALTER TABLE `bills` DISABLE KEYS */;
/*!40000 ALTER TABLE `bills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carts`
--

DROP TABLE IF EXISTS `carts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_64t7ox312pqal3p7fg9o503c2` (`user_id`),
  CONSTRAINT `FKb5o626f86h46m4s7ms6ginnop` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts`
--

LOCK TABLES `carts` WRITE;
/*!40000 ALTER TABLE `carts` DISABLE KEYS */;
/*!40000 ALTER TABLE `carts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(30) NOT NULL,
  `parent_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `this_category_id_fk` (`parent_id`),
  CONSTRAINT `this_category_id_fk` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Cate1',NULL),(3,'cate2',1),(4,'cate3',3),(5,'cate4',4);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_types`
--

DROP TABLE IF EXISTS `client_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client_types` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_types`
--

LOCK TABLES `client_types` WRITE;
/*!40000 ALTER TABLE `client_types` DISABLE KEYS */;
INSERT INTO `client_types` VALUES (1,'MALE'),(2,'CHILD');
/*!40000 ALTER TABLE `client_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `colors`
--

DROP TABLE IF EXISTS `colors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `colors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `color_name` varchar(255) NOT NULL,
  `color_image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `colors`
--

LOCK TABLES `colors` WRITE;
/*!40000 ALTER TABLE `colors` DISABLE KEYS */;
INSERT INTO `colors` VALUES (1,'RED','RedUrl');
/*!40000 ALTER TABLE `colors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `id` int NOT NULL AUTO_INCREMENT,
  `last_update_at` datetime(6) DEFAULT NULL,
  `stock_quantity` int DEFAULT NULL,
  `warehouse_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKix9yxgetau1y25hhnv42gsiok` (`warehouse_id`),
  CONSTRAINT `FKix9yxgetau1y25hhnv42gsiok` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`),
  CONSTRAINT `check_quantity_greater_zero` CHECK ((`stock_quantity` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1,NULL,10,3),(2,NULL,47,3),(3,NULL,46,3),(4,NULL,21,3),(5,NULL,46,3),(6,NULL,73,3),(7,NULL,26,3),(8,NULL,84,3),(9,NULL,70,3),(10,NULL,68,3),(11,NULL,97,3),(12,NULL,39,3),(13,NULL,70,3),(14,NULL,33,3),(15,NULL,63,3),(16,NULL,50,3),(17,NULL,68,3),(18,NULL,63,3),(19,NULL,51,3),(20,NULL,66,3),(21,NULL,64,3),(22,NULL,99,3),(23,NULL,67,3),(24,NULL,59,3),(25,NULL,44,3);
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `status` varchar(20) NOT NULL,
  `total_price` decimal(15,2) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `user_id` int NOT NULL,
  `shipping_address_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  KEY `FK2dntnxe9677sy06ujhix6o2bp` (`shipping_address_id`),
  CONSTRAINT `FK2dntnxe9677sy06ujhix6o2bp` FOREIGN KEY (`shipping_address_id`) REFERENCES `shipping_address` (`id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` int NOT NULL,
  `payment_type` varchar(20) NOT NULL,
  `status` varchar(20) NOT NULL,
  `order_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlouu98csyullos9k25tbpk4va` (`order_id`),
  CONSTRAINT `FKlouu98csyullos9k25tbpk4va` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product-term-image`
--

DROP TABLE IF EXISTS `product-term-image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product-term-image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_image_key` varchar(255) NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1h2ftfr7k90ui1svqti2ldxot` (`product_id`),
  CONSTRAINT `FK1h2ftfr7k90ui1svqti2ldxot` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product-term-image`
--

LOCK TABLES `product-term-image` WRITE;
/*!40000 ALTER TABLE `product-term-image` DISABLE KEYS */;
/*!40000 ALTER TABLE `product-term-image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_cart_details`
--

DROP TABLE IF EXISTS `product_cart_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_cart_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `comment` tinytext,
  `quantity` int NOT NULL,
  `cart_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cart_id_fk` (`cart_id`),
  KEY `product_id_fk` (`product_id`),
  CONSTRAINT `cart_id_fk` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`),
  CONSTRAINT `product_id_fk` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_cart_details`
--

LOCK TABLES `product_cart_details` WRITE;
/*!40000 ALTER TABLE `product_cart_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_cart_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_client_type`
--

DROP TABLE IF EXISTS `product_client_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_client_type` (
  `product_id` int NOT NULL,
  `client_type_id` int NOT NULL,
  KEY `product_Id_client_type_fk` (`product_id`),
  KEY `client_type_id_fk` (`client_type_id`),
  CONSTRAINT `client_type_id_fk` FOREIGN KEY (`client_type_id`) REFERENCES `client_types` (`id`),
  CONSTRAINT `product_Id_client_type_fk` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_client_type`
--

LOCK TABLES `product_client_type` WRITE;
/*!40000 ALTER TABLE `product_client_type` DISABLE KEYS */;
INSERT INTO `product_client_type` VALUES (49,1),(49,2),(50,1),(50,2),(52,1),(52,2),(53,1),(53,2),(54,1),(54,2),(55,1),(55,2),(51,1),(51,2);
/*!40000 ALTER TABLE `product_client_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_color`
--

DROP TABLE IF EXISTS `product_color`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_color` (
  `product_id` int NOT NULL,
  `color_id` int NOT NULL,
  PRIMARY KEY (`product_id`,`color_id`),
  KEY `FKpk6riqswj8f8ldulumm9hmpq` (`color_id`),
  CONSTRAINT `FKjs0ht7btbgt5u0jpossmgvfk5` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKpk6riqswj8f8ldulumm9hmpq` FOREIGN KEY (`color_id`) REFERENCES `colors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_color`
--

LOCK TABLES `product_color` WRITE;
/*!40000 ALTER TABLE `product_color` DISABLE KEYS */;
INSERT INTO `product_color` VALUES (49,1),(50,1),(51,1),(52,1),(53,1),(54,1),(55,1);
/*!40000 ALTER TABLE `product_color` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_images`
--

DROP TABLE IF EXISTS `product_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_images` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_image_key` varchar(255) NOT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKghwsjbjo7mg3iufxruvq6iu3q` (`product_id`),
  CONSTRAINT `FKghwsjbjo7mg3iufxruvq6iu3q` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_images`
--

LOCK TABLES `product_images` WRITE;
/*!40000 ALTER TABLE `product_images` DISABLE KEYS */;
INSERT INTO `product_images` VALUES (32,'9457908d-7a2e-4b61-b1b5-3ac207b96f83',51);
/*!40000 ALTER TABLE `product_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_size`
--

DROP TABLE IF EXISTS `product_size`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_size` (
  `product_id` int NOT NULL,
  `size_id` int NOT NULL,
  PRIMARY KEY (`product_id`,`size_id`),
  KEY `FK1yl8bbmokvonm64131xlscnci` (`size_id`),
  CONSTRAINT `FK1yl8bbmokvonm64131xlscnci` FOREIGN KEY (`size_id`) REFERENCES `sizes` (`id`),
  CONSTRAINT `FK9qjgp0xvl5jfetdt683i7wqwr` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_size`
--

LOCK TABLES `product_size` WRITE;
/*!40000 ALTER TABLE `product_size` DISABLE KEYS */;
INSERT INTO `product_size` VALUES (49,1),(50,1),(51,1),(52,1),(53,1),(54,1),(55,1);
/*!40000 ALTER TABLE `product_size` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_tag`
--

DROP TABLE IF EXISTS `product_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_tag` (
  `product_id` int NOT NULL,
  `tag_id` int NOT NULL,
  PRIMARY KEY (`product_id`,`tag_id`),
  KEY `FKgp9r9cogdtnjbwn6rw2dv0o00` (`tag_id`),
  CONSTRAINT `FKeybx0aj781vhg3j2wn8utdfvv` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKgp9r9cogdtnjbwn6rw2dv0o00` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_tag`
--

LOCK TABLES `product_tag` WRITE;
/*!40000 ALTER TABLE `product_tag` DISABLE KEYS */;
INSERT INTO `product_tag` VALUES (49,1),(50,1),(51,1),(52,1),(53,1),(54,1),(55,1),(49,2),(50,2);
/*!40000 ALTER TABLE `product_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_wishlist`
--

DROP TABLE IF EXISTS `product_wishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_wishlist` (
  `product_id` int NOT NULL,
  `wishlist_id` int NOT NULL,
  PRIMARY KEY (`product_id`,`wishlist_id`),
  KEY `FK435fli90vjmposcec44b9bdb9` (`wishlist_id`),
  CONSTRAINT `FK26rsn98tq46ncyuo9hq867r9t` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FK435fli90vjmposcec44b9bdb9` FOREIGN KEY (`wishlist_id`) REFERENCES `wish_list` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_wishlist`
--

LOCK TABLES `product_wishlist` WRITE;
/*!40000 ALTER TABLE `product_wishlist` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_wishlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `created_by` int NOT NULL,
  `status` int NOT NULL,
  `description` tinytext,
  `last_modified_by` int DEFAULT NULL,
  `price` decimal(15,2) NOT NULL,
  `discount_price` decimal(15,2) DEFAULT NULL,
  `product_name` varchar(50) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `discount_expiration` datetime(6) DEFAULT NULL,
  `category_id` int NOT NULL,
  `suppliers_id` int NOT NULL,
  `inventory_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `category_id_fk` (`category_id`),
  KEY `supplier_id_fk` (`suppliers_id`),
  KEY `inventory_id` (`inventory_id`),
  CONSTRAINT `category_id_fk` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `inventory_id` FOREIGN KEY (`inventory_id`) REFERENCES `inventory` (`id`),
  CONSTRAINT `supplier_id_fk` FOREIGN KEY (`suppliers_id`) REFERENCES `supplier` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (49,'2023-11-29 18:56:33.706876',16,0,'adadadad',NULL,99.00,NULL,'Táocc',NULL,NULL,1,1,1),(50,'2023-11-29 18:59:43.087907',16,0,'bbbb',NULL,99.00,NULL,'Táo91a',NULL,NULL,1,1,1),(51,'2023-11-29 19:00:26.266541',16,0,'adadad',16,12.00,NULL,'Táo','2023-11-29 20:13:45.519307',NULL,1,1,1),(52,'2023-11-29 19:02:36.906220',16,0,'adsdsad',NULL,99.00,NULL,'Táo2',NULL,NULL,1,1,1),(53,'2023-11-29 19:05:59.921549',16,0,'bbbbb\n',NULL,99.00,NULL,'Táo đặt biệt',NULL,NULL,1,1,1),(54,'2023-11-29 19:07:29.656156',16,0,'bbbbb\n',NULL,99.00,NULL,'Táo đặt biệt',NULL,NULL,1,1,1),(55,'2023-11-29 19:07:35.843814',16,0,'bbbbb\n',NULL,99.00,NULL,'Táo đặt biệt',NULL,NULL,1,1,1);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `purchases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchases` (
  `id` int NOT NULL AUTO_INCREMENT,
  `purchase_price` decimal(15,2) NOT NULL,
  `quantity` int DEFAULT NULL,
  `bill_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg55fh6g6qv49saarkqcrmxsj8` (`bill_id`),
  KEY `FKcacbvw28fu31rv1vrhnkcbe28` (`product_id`),
  CONSTRAINT `FKcacbvw28fu31rv1vrhnkcbe28` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKg55fh6g6qv49saarkqcrmxsj8` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `purchases` WRITE;
/*!40000 ALTER TABLE `purchases` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` int NOT NULL AUTO_INCREMENT,
  `column_name` int NOT NULL,
  `comment` tinytext,
  `created_at` datetime(6) NOT NULL,
  `rating` int NOT NULL,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcgy7qjc1r99dp117y9en6lxye` (`user_id`),
  KEY `FKpl51cejpw4gy5swfar8br9ngi` (`product_id`),
  CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKpl51cejpw4gy5swfar8br9ngi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sale_price` decimal(15,2) NOT NULL,
  `quantity` int DEFAULT NULL,
  `bill_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs5o28sak3pq0rkfuj7639p8i8` (`bill_id`),
  KEY `FKkxc13g7l4ioljxqyoo15nh051` (`product_id`),
  CONSTRAINT `FKkxc13g7l4ioljxqyoo15nh051` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKs5o28sak3pq0rkfuj7639p8i8` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shipping_address`
--

DROP TABLE IF EXISTS `shipping_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipping_address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(100) NOT NULL,
  `city` varchar(30) NOT NULL,
  `country` varchar(25) NOT NULL,
  `state` varchar(30) NOT NULL,
  `zip_code` varchar(15) NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKilmh1is3sdv0h8xdsrh0yn5f3` (`user_id`),
  CONSTRAINT `FKilmh1is3sdv0h8xdsrh0yn5f3` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipping_address`
--

LOCK TABLES `shipping_address` WRITE;
/*!40000 ALTER TABLE `shipping_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `shipping_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sizes`
--

DROP TABLE IF EXISTS `sizes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sizes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `size_value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sizes`
--

LOCK TABLES `sizes` WRITE;
/*!40000 ALTER TABLE `sizes` DISABLE KEYS */;
INSERT INTO `sizes` VALUES (1,'XL');
/*!40000 ALTER TABLE `sizes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contact_info` varchar(255) DEFAULT NULL,
  `supplier_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES (1,'abc','admin');
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES (1,'sale'),(2,'hot');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `expired` bit(1) NOT NULL,
  `revoked` bit(1) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `token_type` enum('BEARER') DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pddrhgwxnms2aceeku9s2ewy5` (`token`),
  KEY `FKj8rfw4x0wjjyibfqq566j4qng` (`user_id`),
  CONSTRAINT `user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
INSERT INTO `token` VALUES (1,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nMTRAZ21haWwuY29tIiwiaWF0IjoxNjk5Njg3NTI4LCJleHAiOjE2OTk3NzM5Mjh9.hCCtEIQ_GikhA_zSjwKzuDePb0U_gE9zG38vLWwtcaw','BEARER',10),(2,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nMTRAZ21haWwuY29tIiwiaWF0IjoxNjk5Njg3NzM4LCJleHAiOjE3MDIyNzk3Mzh9.vtb0l1Yb2v2DhZxPf_22j4FgljZa3p14VfodbAl93AQ','BEARER',10),(3,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nMTRAZ21haWwuY29tIiwiaWF0IjoxNjk5Njg3ODQwLCJleHAiOjE3MDIyNzk4NDB9.ULpiGzgmfql3ZOav_Yq3Vw5ZxTbcHvTQZgVd5WiIBPQ','BEARER',10),(4,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nOUBnbWFpbC5jb20iLCJpYXQiOjE2OTk3MzM3MDcsImV4cCI6MTY5OTgyMDEwN30.Fw78jSc_vplKezKSeJl-Au7VXL72AQbR7h0gcVKr56A','BEARER',11),(5,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nOEBnbWFpbC5jb20iLCJpYXQiOjE2OTk3MzM3MzMsImV4cCI6MTY5OTgyMDEzM30.Yz_S_FmMKa4f5W0U-3DXPkWOs3Chz3E0Q_Le0-I-UjE','BEARER',12),(6,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nN0BnbWFpbC5jb20iLCJpYXQiOjE2OTk3MzM5MDMsImV4cCI6MTY5OTgyMDMwM30.7DY4HWAhFpUKugTl6dmsi8oh5c5IQ4_7D_hhsAd_7f4','BEARER',13),(7,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nNkBnbWFpbC5jb20iLCJpYXQiOjE2OTk3MzQyMDUsImV4cCI6MTY5OTgyMDYwNX0.Jof1qqnt1m3NbNzAPKDamEjBTiapW2B56LhJEyw__B0','BEARER',14),(8,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nNUBnbWFpbC5jb20iLCJpYXQiOjE2OTk3MzQ0NjksImV4cCI6MTY5OTgyMDg2OX0.PHL5P1URUetsx1T4RnViqPpliRKSO0LM83AyruTn4BI','BEARER',15),(9,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nNEBnbWFpbC5jb20iLCJpYXQiOjE2OTk3MzQ3NjIsImV4cCI6MTY5OTgyMTE2Mn0.Pd1jMEJj8BXd-ay1Rk1h7nisEie0fwNp9VfVTESVks0','BEARER',16),(10,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nN0BnbWFpbC5jb20iLCJpYXQiOjE2OTk4NzU2NTYsImV4cCI6MTcwMjQ2NzY1Nn0.4nu1wMzX98Yo2UPghFyml8NOhyAeVdP0Ukbxc4V9a-Q','BEARER',13),(11,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nN0BnbWFpbC5jb20iLCJpYXQiOjE2OTk5OTYzMTYsImV4cCI6MTcwMjU4ODMxNn0.hKasM_8bz2p9CILneV0iqxfoZvdeJJNbBSa6vi16VSg','BEARER',13),(12,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nN0BnbWFpbC5jb20iLCJpYXQiOjE3MDAxNjg2NDksImV4cCI6MTcwMjc2MDY0OX0.EpXtzVh7fV1vnvL9ldh_yfli1WssN4oDDD8k3PEurD8','BEARER',13),(13,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nNEBnbWFpbC5jb20iLCJpYXQiOjE3MDA3OTM5NDUsImV4cCI6MTcwMzM4NTk0NX0.wPO7-Rj-09h0Ir_ghX2eSShHdaiywO1rBxhvsJSjRlY','BEARER',16),(18,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nNEBnbWFpbC5jb20iLCJpYXQiOjE3MDA3OTUyNTMsImV4cCI6MTcwMzM4NzI1M30.KE9Rd18eV7KrI_QdGY82peQvshmriKy-cFKylt_J94k','BEARER',16),(19,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nNEBnbWFpbC5jb20iLCJpYXQiOjE3MDA3OTUyOTEsImV4cCI6MTcwMzM4NzI5MX0.UsPHR0kgXRWsJSDxND83wdoFXpEo5D_8nBwoPcO2HlA','BEARER',16),(20,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nNEBnbWFpbC5jb20iLCJpYXQiOjE3MDA3OTUzNzksImV4cCI6MTcwMzM4NzM3OX0.0zOsHQoIAsACmSGzGZEiycOU_vgQM72FUCp9xTxiSgU','BEARER',16),(21,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nNEBnbWFpbC5jb20iLCJpYXQiOjE3MDA4MjU2MTAsImV4cCI6MTcwMzQxNzYxMH0.V3itipLRUqtdpsrGqkpfumiub4YKLWTjMvcqw7sz35M','BEARER',16),(22,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nNEBnbWFpbC5jb20iLCJpYXQiOjE3MDA4MjU2MTEsImV4cCI6MTcwMzQxNzYxMX0.cv8m6cvGtEPkVU0fFUsi4d4y-m3c2ABSgxvNkTioEvo','BEARER',16),(23,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXRyb25ndGh1b25nNEBnbWFpbC5jb20iLCJpYXQiOjE3MDA4MjcxODAsImV4cCI6MTcwMzQxOTE4MH0.EB2VRK3Um5R7_L6VyB6AwxYsymM8-dvLfBaPwaf0XAc','BEARER',16);
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_images`
--

DROP TABLE IF EXISTS `user_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_images` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_image_key` varchar(255) NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_image_fk` (`user_id`),
  CONSTRAINT `user_id_image_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_images`
--

LOCK TABLES `user_images` WRITE;
/*!40000 ALTER TABLE `user_images` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `update_at` datetime(6) DEFAULT NULL,
  `role` enum('ADMIN','MANAGER','USER') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_users_email` (`email`),
  UNIQUE KEY `uc_users_password` (`password`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (10,'thuong','latrong','latrongthuong14@gmail.com','$2a$10$tXl/Fz1MVSlZ8Eggmgf30u6Ygh0MLLjbX2bnMZfKiaOalbxV2/xf6','2023-11-11 16:25:28.701414',NULL,'USER'),(11,'thuong','latrong','latrongthuong9@gmail.com','$2a$10$vsneWD0J/q.QHYX59DFfse8wtKrYYkG2/0WL2yqC/i9ESG35YjqQS','2023-11-12 05:15:07.142043',NULL,'ADMIN'),(12,'thuong','latrong','latrongthuong8@gmail.com','$2a$10$AFQ.9M2mdQhr2zNrYnaCnOxVjLYAvdOgLqu6WpBwPTf/irxG6fP0O','2023-11-12 05:15:33.197327',NULL,'ADMIN'),(13,'thuong','latrong','latrongthuong7@gmail.com','$2a$10$XF3ulUyK1uVDcz0esZ8uw.33LsdgQyNYxg/oEisBYyhtvJXVheDDq','2023-11-12 05:18:23.791280',NULL,'ADMIN'),(14,'thuong','latrong','latrongthuong6@gmail.com','$2a$10$vXUVAfU2dI7MgWnubwyNGOOcOi1Ha1Ibunmn1mvvDFOUpfHAy3gce','2023-11-12 05:23:25.106270',NULL,'ADMIN'),(15,'thuong','latrong','latrongthuong5@gmail.com','$2a$10$TlnYoZCeRQv4k/H6.19/dep3Ij5PYFfn2d083KKeQONjScJUNFhA.','2023-11-12 05:27:49.703312',NULL,'ADMIN'),(16,'thuong','latrong','latrongthuong4@gmail.com','$2a$10$XFn5Fk/WIn30KyzvI0Zqw.pj0Gc9bTfLfuR7O0w5ivIMd/3PSHSmm','2023-11-12 05:32:42.118854',NULL,'ADMIN');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouse`
--

DROP TABLE IF EXISTS `warehouse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouse` (
  `id` int NOT NULL AUTO_INCREMENT,
  `warehouse_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouse`
--

LOCK TABLES `warehouse` WRITE;
/*!40000 ALTER TABLE `warehouse` DISABLE KEYS */;
INSERT INTO `warehouse` VALUES (1,'WHsp1'),(2,'WHsp2'),(3,'WHsp3'),(4,'Admin');
/*!40000 ALTER TABLE `warehouse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wish_list`
--

DROP TABLE IF EXISTS `wish_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wish_list` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r1w2h67p25icr918heol9jisu` (`user_id`),
  CONSTRAINT `FKit8ap20bpapw291y78egje6f3` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wish_list`
--

LOCK TABLES `wish_list` WRITE;
/*!40000 ALTER TABLE `wish_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `wish_list` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-30 11:30:25
