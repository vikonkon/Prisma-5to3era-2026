CREATE DATABASE  IF NOT EXISTS `prima` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `prima`;
-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: prima
-- ------------------------------------------------------
-- Server version	8.0.45

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
-- Table structure for table `compra`
--

DROP TABLE IF EXISTS `compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compra` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `iva` int NOT NULL,
  `subtotal` float NOT NULL,
  `total` float NOT NULL,
  `comprador_id` bigint DEFAULT NULL,
  `factura_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK2rqh0j3j2k2f6kd61ye93i5ff` (`factura_id`),
  KEY `FKo6ua33g2njuexrbs735oktojy` (`comprador_id`),
  CONSTRAINT `FKja4y4fno0eo9j57nqoefos89v` FOREIGN KEY (`factura_id`) REFERENCES `facturas` (`id`),
  CONSTRAINT `FKo6ua33g2njuexrbs735oktojy` FOREIGN KEY (`comprador_id`) REFERENCES `comprador` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compra`
--

LOCK TABLES `compra` WRITE;
/*!40000 ALTER TABLE `compra` DISABLE KEYS */;
INSERT INTO `compra` VALUES (1,5250,24999,30248.8,1,1),(2,2730,12999,15728.8,2,2),(3,3885,18500,22385,3,3),(4,3360,15999,19358.8,4,4),(5,4095,19500,23595,5,5),(6,9660,45999,55658.8,6,6),(7,8190,38999,47188.8,7,7),(8,8925,42500,51425,8,8),(9,8610,41000,49610,9,9),(10,8295,39500,47795,10,10),(11,13650,65000,78650,11,11),(12,18690,89000,107690,12,12),(13,10185,48500,58685,13,13),(14,11340,54000,65340,14,14),(15,10920,52000,62920,15,15),(16,13650,65000,78650,16,16),(17,20580,98000,118580,17,17),(18,19320,92000,111320,18,18),(19,14910,71000,85910,19,19),(20,15540,74000,89540,20,20),(21,35383,168492,203875,999,21),(22,19320,92000,111320,27,22),(23,40740,194000,234740,27,23),(24,7245,34499,41744,27,24),(25,131245,624975,756220,27,25),(26,15540,74000,89540,999,26),(27,19425,92500,111925,999,27),(28,8189,38997,47186,999,28),(29,8189,38997,47186,999,29),(30,31499,149993,181492,999,30),(31,31499,149994,181493,999,31),(32,8189,38997,47186,999,32);
/*!40000 ALTER TABLE `compra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `compra_productos`
--

DROP TABLE IF EXISTS `compra_productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compra_productos` (
  `compra_id` bigint NOT NULL,
  `producto_id` bigint NOT NULL,
  KEY `FKlap7o9x5q7y81cwid229rbgd1` (`producto_id`),
  KEY `FKn2nx3frx0c1qs2sy0q7tuk6pi` (`compra_id`),
  CONSTRAINT `FKlap7o9x5q7y81cwid229rbgd1` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `FKn2nx3frx0c1qs2sy0q7tuk6pi` FOREIGN KEY (`compra_id`) REFERENCES `compra` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compra_productos`
--

LOCK TABLES `compra_productos` WRITE;
/*!40000 ALTER TABLE `compra_productos` DISABLE KEYS */;
INSERT INTO `compra_productos` VALUES (1,1),(2,2),(3,3),(4,4),(5,5),(6,16),(7,17),(8,18),(9,19),(10,20),(11,31),(12,32),(13,33),(14,34),(15,35),(16,51),(17,52),(18,53),(19,54),(20,55),(21,2),(21,2),(21,2),(21,2),(21,2),(21,3),(21,3),(21,3),(21,4),(21,4),(21,4),(22,53),(23,33),(23,33),(23,33),(23,33),(24,3),(24,4),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(25,1),(26,3),(26,3),(26,3),(26,3),(27,3),(27,3),(27,3),(27,3),(27,3),(28,2),(28,2),(28,2),(29,2),(29,2),(29,2),(30,6),(30,6),(30,6),(30,6),(30,6),(30,7),(30,7),(31,1),(31,1),(31,1),(31,1),(31,1),(31,1),(32,2),(32,2),(32,2);
/*!40000 ALTER TABLE `compra_productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comprador`
--

DROP TABLE IF EXISTS `comprador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comprador` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apellido` varchar(255) DEFAULT NULL,
  `contrasena` varchar(255) DEFAULT NULL,
  `correo` varchar(255) DEFAULT NULL,
  `cuit` bigint DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `rol` varchar(20) DEFAULT 'CLIENTE',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comprador`
--

LOCK TABLES `comprador` WRITE;
/*!40000 ALTER TABLE `comprador` DISABLE KEYS */;
INSERT INTO `comprador` VALUES (1,'González','hash_segura_01','carlos.gonzalez@example.com',2035896321,'Av. San Martín 1450, Mendoza','Carlos','CLIENTE'),(2,'Pérez','hash_segura_02','maria.perez@example.com',1738912345,'Calle Mitre 420, Mendoza','María','CLIENTE'),(3,'Rodríguez','hash_segura_03','juan.rodriguez@example.com',2041238965,'Belgrano 890, Godoy Cruz','Juan','CLIENTE'),(4,'Fernández','hash_segura_04','lucia.fernandez@example.com',1736547891,'Las Heras 312, Mendoza','Lucía','CLIENTE'),(5,'Gómez','hash_segura_05','matias.gomez@example.com',2039874561,'España 745, Guaymallén','Matías','CLIENTE'),(6,'López','hash_segura_06','sofia.lopez@example.com',1742159873,'San Lorenzo 120, Mendoza','Sofia','CLIENTE'),(7,'Martínez','hash_segura_07','agustin.martinez@example.com',2037485962,'Paso de los Andes 950, Godoy Cruz','Agustín','CLIENTE'),(8,'Díaz','hash_segura_08','camila.diaz@example.com',1740123987,'Pedro Molina 333, Guaymallén','Camila','CLIENTE'),(9,'Álvarez','hash_segura_09','joaquin.alvarez@example.com',2038492015,'Necochea 560, Mendoza','Joaquín','CLIENTE'),(10,'Romero','hash_segura_10','valentina.romero@example.com',1739485123,'Chile 810, Mendoza','Valentina','CLIENTE'),(11,'Sosa','hash_segura_11','lucas.sosa@example.com',2041589632,'Videla Aranda 1500, Maipú','Lucas','CLIENTE'),(12,'Torres','hash_segura_12','martina.torres@example.com',1737849201,'Maza 210, Maipú','Martina','CLIENTE'),(13,'Ruiz','hash_segura_13','nicolas.ruiz@example.com',2036214587,'San Martín Sur 780, Godoy Cruz','Nicolás','CLIENTE'),(14,'Benítez','hash_segura_14','florencia.benitez@example.com',1741025896,'Libertad 1120, Guaymallén','Florencia','CLIENTE'),(15,'Ramírez','hash_segura_15','thiago.ramirez@example.com',2043125896,'Aristóbulo del Valle 450, Luján de Cuyo','Thiago','CLIENTE'),(16,'Flores','hash_segura_16','julieta.flores@example.com',1738596201,'Sáenz Peña 300, Luján de Cuyo','Julieta','CLIENTE'),(17,'Acosta','hash_segura_17','benjamin.acosta@example.com',2039148526,'Roca 640, San Martín','Benjamín','CLIENTE'),(18,'Medina','hash_segura_18','catalina.medina@example.com',1742589631,'Alvear 510, San Martín','Catalina','CLIENTE'),(19,'Castro','hash_segura_19','facundo.castro@example.com',2037984512,'9 de Julio 920, Mendoza','Facundo','CLIENTE'),(20,'Vargas','hash_segura_20','milagros.vargas@example.com',1740891234,'Mitre 1340, San Rafael','Milagros','CLIENTE'),(21,'Moran','barrio123','zoejulietam2009@gmail.com',1234567890,'argentina','Zoe','CLIENTE'),(25,'BRESSIA','barrio123','zoejulietam@gmail.com',12310604971,'mendozA','tomA','CLIENTE'),(26,'Rave','12345','tbressia@gmail.com',11111111111,'Balcones del cerro Arco','Yamila','CLIENTE'),(27,'martinez','1234','nache@gmail.com',12345678901,'las heras','nache','CLIENTE'),(999,'Gerencia','prisma_admin_123','gerente@prisma.com',20111111112,'Sede Central','Admin','GERENTE');
/*!40000 ALTER TABLE `comprador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comprador_producto`
--

DROP TABLE IF EXISTS `comprador_producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comprador_producto` (
  `comprador_id` bigint NOT NULL,
  `producto_id` bigint NOT NULL,
  KEY `FKpluty2u606gyqxh3k1fw8rtxy` (`producto_id`),
  KEY `FKcbnebp88c011fsgneqvgqabqv` (`comprador_id`),
  CONSTRAINT `FKcbnebp88c011fsgneqvgqabqv` FOREIGN KEY (`comprador_id`) REFERENCES `comprador` (`id`),
  CONSTRAINT `FKpluty2u606gyqxh3k1fw8rtxy` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comprador_producto`
--

LOCK TABLES `comprador_producto` WRITE;
/*!40000 ALTER TABLE `comprador_producto` DISABLE KEYS */;
INSERT INTO `comprador_producto` VALUES (1,1),(2,2),(3,3),(4,4),(5,5),(6,16),(7,17),(8,18),(9,19),(10,20),(11,31),(12,32),(13,33),(14,34),(15,35),(16,51),(17,52),(18,53),(19,54),(20,55);
/*!40000 ALTER TABLE `comprador_producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facturas`
--

DROP TABLE IF EXISTS `facturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facturas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `iva` int NOT NULL,
  `nomcli` varchar(255) DEFAULT NULL,
  `subtotal` float NOT NULL,
  `total` float NOT NULL,
  `nomcomprador` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facturas`
--

LOCK TABLES `facturas` WRITE;
/*!40000 ALTER TABLE `facturas` DISABLE KEYS */;
INSERT INTO `facturas` VALUES (1,5250,NULL,24999,30248.8,'Carlos González'),(2,2730,NULL,12999,15728.8,'María Pérez'),(3,3885,NULL,18500,22385,'Juan Rodríguez'),(4,3360,NULL,15999,19358.8,'Lucía Fernández'),(5,4095,NULL,19500,23595,'Matías Gómez'),(6,9660,NULL,45999,55658.8,'Sofia López'),(7,8190,NULL,38999,47188.8,'Agustín Martínez'),(8,8925,NULL,42500,51425,'Camila Díaz'),(9,8610,NULL,41000,49610,'Joaquín Álvarez'),(10,8295,NULL,39500,47795,'Valentina Romero'),(11,13650,NULL,65000,78650,'Lucas Sosa'),(12,18690,NULL,89000,107690,'Martina Torres'),(13,10185,NULL,48500,58685,'Nicolás Ruiz'),(14,11340,NULL,54000,65340,'Florencia Benítez'),(15,10920,NULL,52000,62920,'Thiago Ramírez'),(16,13650,NULL,65000,78650,'Julieta Flores'),(17,20580,NULL,98000,118580,'Benjamín Acosta'),(18,19320,NULL,92000,111320,'Catalina Medina'),(19,14910,NULL,71000,85910,'Facundo Castro'),(20,15540,NULL,74000,89540,'Milagros Vargas'),(21,35383,NULL,168492,203875,'Admin Gerencia'),(22,19320,NULL,92000,111320,'nache martinez'),(23,40740,NULL,194000,234740,'nache martinez'),(24,7245,NULL,34499,41744,'nache martinez'),(25,131245,NULL,624975,756220,'nache martinez'),(26,15540,NULL,74000,89540,'Admin Gerencia'),(27,19425,NULL,92500,111925,'Admin Gerencia'),(28,8189,NULL,38997,47186,'Admin Gerencia'),(29,8189,NULL,38997,47186,'Admin Gerencia'),(30,31499,NULL,149993,181492,'Admin Gerencia'),(31,31499,NULL,149994,181493,'Admin Gerencia'),(32,8189,NULL,38997,47186,'Admin Gerencia');
/*!40000 ALTER TABLE `facturas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `URLimagen` varchar(255) DEFAULT NULL,
  `cantidad` int NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `precio` float NOT NULL,
  `activo` bit(1) DEFAULT NULL,
  `categoria` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,'https://i.pinimg.com/736x/fc/75/77/fc7577b177e10180191c875557f55246.jpg',14,'Remera strapless ajustada color verde, ideal para días cálidos.','Remera Strapless Verde',24999,NULL,'remeras'),(2,'https://i.pinimg.com/736x/da/94/2c/da942c3eded3070f62c3948e5067d9ef.jpg',20,'Top corto sin mangas con patrón a cuadrillé en tonos marrones.','Top Marrón a Cuadrille',12999,NULL,'remeras'),(3,'https://i.pinimg.com/736x/42/89/a8/4289a8a3293fa243dd59c130eba2e46e.jpg',10,'Remera gris de corte clásico con estampado frontal de calavera.','Remera de Calavera Gris',18500,NULL,'remeras'),(4,'https://i.pinimg.com/736x/3e/14/e4/3e14e486ae4c25a7a18af3b7a474157b.jpg',11,'Remera urbana con estampado tipográfico Times Out.','Remera estampada Times Out',15999,NULL,'remeras'),(5,'https://i.pinimg.com/736x/3d/5a/bc/3d5abcf116a8155bd4e7e145fa4a8f3b.jpg',24,'Remera estilo vintage con detalle de cuello en contraste.','Remera Vintage con Cuello',19500,NULL,'remeras'),(6,'https://i.pinimg.com/736x/d2/b5/15/d2b51567a3d3daee2721ced1ae51248b.jpg',0,'Remera color beige con diseño de hombros descubiertos.','Remera Beige de Hombros Descubiertos',22999,NULL,'remeras'),(7,'https://i.pinimg.com/736x/5a/46/0c/5a460c4bd2f15767d25f4d7d7145cb4c.jpg',20,'Remera azul de algodón con estampado gráfico en el centro.','Remera Estampada Azul',17499,NULL,'remeras'),(8,'https://i.pinimg.com/736x/52/79/ec/5279eca596fd395cca2689160fe30e3f.jpg',12,'Remera oversize color marrón con estampado de telaraña/araña.','Remera Oversize Marron con Estampa de Araña',16500,NULL,'remeras'),(9,'https://i.pinimg.com/1200x/61/5b/b8/615bb8dafc281223a8a505760ce2f574.jpg',14,'Remera de manga larga con diseño gráfico de Radiohead.','Remera Manga Larga Radiohead',26000,NULL,'remeras'),(10,'https://i.pinimg.com/1200x/b2/90/5c/b2905c1ed5b69e234913d5348e416069.jpg',35,'Remera clásica de manga corta en color amarillo vibrante.','Remera Manga Corta Amarilla',12999,NULL,'remeras'),(11,'https://i.pinimg.com/736x/ab/63/6a/ab636a8a034e186e0138274788c86c7f.jpg',10,'Polo clásico a rayas o detalles en azul y blanco para mujer.','Polo Azul y Blanco de Mujer',19999,NULL,NULL),(12,'https://i.pinimg.com/736x/43/03/e7/4303e73a447cdbd4e22094734776978c.jpg',16,'Remera deportiva inspirada en la selección de Italia.','Remera Deportiva Italia',21500,NULL,'remeras'),(13,'https://i.pinimg.com/736x/3d/d2/c6/3dd2c693a44108727b4f09a8dae761b3.jpg',25,'Remera de manga larga con patrón clásico a rayas.','Remera Manga Larga a Rayas',13999,NULL,'remeras'),(14,'https://i.pinimg.com/736x/74/66/9d/74669d89d7b6f9d4ed504af36b36a966.jpg',10,'Camisa estilizada en tono gris oscuro, corte moderno.','Camisa Gris Oscuro Estilizada',20500,NULL,'remeras'),(15,'https://i.pinimg.com/1200x/f0/e7/1d/f0e71dabd3408bc494b9415aa2fa04d6.jpg',20,'Remera básica con patrón a cuadros en tonos verdes.','Remera Basica Verde a Cuadros',12999,NULL,'remeras'),(16,'https://i.pinimg.com/1200x/ad/e4/db/ade4dbc1c9f7c01f9a7e15713d72f12c.jpg',20,'Pantalón de jean corte wide fit en color celeste claro.','Pantalón de Jean Wide Celeste',45999,NULL,'pantalones'),(17,'https://i.pinimg.com/736x/5e/bf/85/5ebf857a45f676b530f99ea176b9c872.jpg',25,'Pantalón de jean corte wide fit en color azul clásico.','Pantalón de Jean Wide Azul',38999,NULL,'pantalones'),(18,'https://i.pinimg.com/736x/98/72/d9/9872d90e9b3530373acddb9cb8b88d41.jpg',15,'Pantalón de mezclilla con corte flare (pata de elefante) en color azul.','Pantalón Flare Azul',30000,NULL,'pantalones'),(19,'https://i.pinimg.com/736x/0a/97/0e/0a970e2c605ba4343170063e779dad39.jpg',30,'Short casual en color marrón, tela ligera y cómoda.','Short Marrón',41000,NULL,'pantalones'),(20,'https://i.pinimg.com/736x/8f/8d/40/8f8d40682f37bc1534c0a6c24480d17b.jpg',18,'Falda de diseño vintage, ideal para combinar con prendas oversize.','Falda Vintage',39500,NULL,NULL),(31,'https://i.pinimg.com/1200x/c8/ad/04/c8ad048b90372da2076c6845802e6f31.jpg',20,'Chaqueta estilo bomber vintage en tonos marrones.','Chaqueta Bomber Vintage Marrón ',65000,NULL,NULL),(32,'https://i.pinimg.com/736x/b5/ab/56/b5ab56c8e5ee3c70d7cc170d6796eab2.jpg',15,'Campera abrigada color marrón vintage con detalles en corderoy.','Campera Marrón Vintage con Corderoy',89000,NULL,'abrigos'),(33,'https://i.pinimg.com/1200x/44/4f/26/444f2685c4ac2e28f5c0fd238bce7478.jpg',21,'Buzo amplio y cómodo de corte oversize en color azul.','Buzo Azul Oversize',48500,NULL,'abrigos'),(34,'https://i.pinimg.com/736x/de/7e/fc/de7efce413234063d23687105aadda36.jpg',18,'Campera tejida de lana en color beige, estilo cárdigan.','Campera de Lana Beige',54000,NULL,'abrigos'),(35,'https://i.pinimg.com/736x/71/3a/d5/713ad595e5bbed0cbe1c6a92acd302dc.jpg',22,'Campera clásica de jean en tono gris lavado.','Campera de Jean Gris',52000,NULL,'pantalones'),(51,'https://i.pinimg.com/736x/04/52/6d/04526d4aad8fef106e393176f555fb62.jpg',30,'Zapatillas urbanas de corte clásico en color blanco total.','Zapatillas Urbanas Blancas Clásicas',65000,NULL,'calzado'),(52,'https://i.pinimg.com/736x/59/b0/91/59b091d4a1df126f29d5a1ca7b41a048.jpg',20,'Borcegos clásicos de cuero en color negro con cordones.','Borcegos Negros Clásicos',98000,NULL,'calzado'),(53,'https://i.pinimg.com/736x/93/6a/b9/936ab90423f84753aec871efd6f6621f.jpg',0,'Zapatillas clásicas de estilo deportivo en tonos marrones.','Zapatillas Adidas Clasicas Marrónes',92000,NULL,'calzado'),(54,'https://i.pinimg.com/736x/3b/fc/a7/3bfca736a6363041470fdb0d0692df15.jpg',25,'Zapatilla urbana de lona con detalle de estrella en color rojo.','Zapatilla Urbana Estrella Roja',71000,NULL,'calzado'),(55,'https://i.pinimg.com/736x/cd/3f/01/cd3f01c40d7754d14913dd7fa1539ccf.jpg',17,'Zapatilla urbana de lona con detalle de estrella en color azul.','Zapatilla Urbana Estrella Azul',74000,NULL,'calzado'),(56,'https://i.pinimg.com/736x/71/ac/b7/71acb7b43a9b2a5bc15397402423f0e5.jpg',0,'Bermuda de algodón con estampado militar talle único, unisex.','Bermuda Con Estampado Militar',2000,_binary '\0','pantalones'),(57,'https://i.pinimg.com/736x/71/ac/b7/71acb7b43a9b2a5bc15397402423f0e5.jpg',0,'Bermuda de algodón con estampado militar talle único, unisex.','Bermuda Con Estampado Militar',20000,_binary '\0','pantalones'),(58,'https://i.pinimg.com/736x/71/ac/b7/71acb7b43a9b2a5bc15397402423f0e5.jpg',18,'Bermuda de algodón con estampado militar talle único, unisex.','Bermuda Con Estampado Militar',20000,_binary '\0','pantalones'),(59,'https://i.pinimg.com/736x/71/ac/b7/71acb7b43a9b2a5bc15397402423f0e5.jpg',10,'Bermuda de algodón con estampado militar talle único, unisex.','Bermuda Con Estampado Militar',20000,_binary '','pantalones'),(60,'https://i.pinimg.com/736x/0b/23/51/0b2351d3e0faaefb13ac99794c57a9ca.jpg',15,'Remera marrón manga larga de algodón Dickies Talle M','Remera Manga Larga Marrón',15000,_binary '','remeras'),(61,'',2,'','remera azul',20000,_binary '\0','remeras');
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-22 16:01:05
