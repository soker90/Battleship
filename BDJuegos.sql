CREATE DATABASE  IF NOT EXISTS `juegos` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `juegos`;
-- MySQL dump 10.13  Distrib 5.5.43, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: juegos
-- ------------------------------------------------------
-- Server version	5.5.43-0ubuntu0.14.10.1

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
-- Table structure for table `Game`
--

DROP TABLE IF EXISTS `Game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Game` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `playersMin` int(11) DEFAULT NULL,
  `playersMax` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Movimientos`
--

DROP TABLE IF EXISTS `Movimientos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Movimientos` (
  `idmov` int(11) NOT NULL AUTO_INCREMENT,
  `idUser` varchar(45) NOT NULL,
  `idMatch` varchar(45) NOT NULL,
  `nrow` varchar(2) NOT NULL,
  `ncol` varchar(2) NOT NULL,
  PRIMARY KEY (`idmov`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Squares`
--

DROP TABLE IF EXISTS `Squares`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Squares` (
  `idSquares` int(11) NOT NULL AUTO_INCREMENT,
  `idMath` int(11) DEFAULT NULL,
  `col` int(11) DEFAULT NULL,
  `row` int(11) DEFAULT NULL,
  `idUser` int(11) DEFAULT NULL,
  PRIMARY KEY (`idSquares`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) NOT NULL,
  `fechaDeAlta` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ranking`
--

DROP TABLE IF EXISTS `ranking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ranking` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idUser` int(11) DEFAULT NULL,
  `idGame` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UI` (`idUser`,`idGame`),
  KEY `RankingUser_idx` (`idUser`),
  KEY `RankingGame_idx` (`idGame`),
  CONSTRAINT `RankingGame` FOREIGN KEY (`idGame`) REFERENCES `Game` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `RankingUser` FOREIGN KEY (`idUser`) REFERENCES `User` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'juegos'
--

--
-- Dumping routines for database 'juegos'
--
/*!50003 DROP PROCEDURE IF EXISTS `insertarMovimiento` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertarMovimiento`(in pUser varchar(45), in pMatch varchar(45),in pRow varchar(2),in pCol varchar(2),out pExito varchar(40))
BEGIN
DECLARE EXIT HANDLER FOR sqlwarning
	BEGIN
		ROLLBACK;
		set pExito='Warning de acceso a la base de datos';
	END;

	START TRANSACTION;

		
		Insert into Movimientos (idUser, idMatch, nrow, ncol) values (pUser, pMatch,pRow,pCol);

		Set pExito='OK';
	COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insertarRanking` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertarRanking`(in pUser varchar(45), in pGame varchar(2),out pExito varchar(40))
BEGIN

	DECLARE EXIT HANDLER FOR sqlwarning
	BEGIN
		ROLLBACK;
		set pExito='Warning de acceso a la base de datos';
	END;

	START TRANSACTION;

		
		Insert into ranking (idUser, idGame) values (pUser, pGame);

		Set pExito='OK';
	COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insertarUsuario` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertarUsuario`(in pEmail varchar(45), in pPwd varchar(40), out pExito varchar(200), out pQ1 varchar(200), out pQ2 varchar(200))
BEGIN
	DECLARE queryCrearUsuario VARCHAR(200);
	DECLARE queryAsignarPermisos VARCHAR(200);

	DECLARE EXIT HANDLER FOR 1062, 396
	begin
		ROLLBACK;
		set pExito='Error al registrar usuario. ¿Tal vez ya existe?';
	end;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	begin
		ROLLBACK;
		SET pExito='Error de acceso a la base de datos';
	END;
	DECLARE EXIT HANDLER FOR sqlwarning
	BEGIN
		ROLLBACK;
		set pExito='Warning de acceso a la base de datos';
	END;

	START TRANSACTION;

		
		Insert into User (email, fechaDeAlta) values (pEmail, curdate());

		/** En ultimoId guardamos chess1, chess2, etc. 
			LAST_INSERT_ID() devuelve el id autonumérico del último registro insertado **/
		set @ultimoId=concat('juegos', LAST_INSERT_ID());


		/************
		Suponiendo que ultimoId sea chess120, queremos conseguir una instruccion de este estilo: 

		CREATE USER 'chess120'@'localhost' IDENTIFIED BY 'pepe'

		...en donde pepe es la contraseña que se ha pasado como parámetro al procedimiento almacenado;
		************/

		Set @queryCrearUsuario = CONCAT('create User \'' , @ultimoId , '\'@\'%\' identified by \'', pPwd , '\';');

		PREPARE crearUsuario FROM @queryCrearUsuario;
		EXECUTE crearUsuario;

		Set @queryCrearUsuario = CONCAT('create User \'' , @ultimoId , '\'@\'localhost\' identified by \'', pPwd , '\';');

		PREPARE crearUsuario FROM @queryCrearUsuario;
		EXECUTE crearUsuario;


		/*********
		Abajo queremos una instruccion tipo grant SELECT,INSERT,DELETE,UPDATE,EXECUTE on ajedrez.* to 'chess120'@'%'
		**********/	
		Set @queryAsignarPermisos = CONCAT('grant SELECT,INSERT,DELETE,UPDATE,EXECUTE on juegos.* to \'', @ultimoId, '\'@\'%\';');

		PREPARE asignarPermisos FROM @queryAsignarPermisos;
		EXECUTE asignarPermisos;

		/*********
		Abajo queremos una instruccion tipo grant SELECT,INSERT,DELETE,UPDATE,EXECUTE on ajedrez.* to 'chess120'@'%'
		**********/	
		Set @queryAsignarPermisos = CONCAT('grant SELECT,INSERT,DELETE,UPDATE,EXECUTE on juegos.* to \'', @ultimoId, '\'@\'localhost\';');

		PREPARE asignarPermisos FROM @queryAsignarPermisos;
		EXECUTE asignarPermisos;

		Set pExito='OK';
		set pQ1=@queryCrearUsuario;
		set pQ2=@queryAsignarPermisos;
	COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insertarUsuarioGoogle` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insertarUsuarioGoogle`(in pEmail varchar(45), in pPwd varchar(40), out pExito varchar(200), out pQ1 varchar(200), out pQ2 varchar(200))
BEGIN
	DECLARE queryCrearUsuario VARCHAR(200);
	DECLARE queryAsignarPermisos VARCHAR(200);

	DECLARE EXIT HANDLER FOR 1062, 396
	begin
		ROLLBACK;
		set pExito='Error al registrar usuario. ¿Tal vez ya existe?';
	end;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	begin
		ROLLBACK;
		SET pExito='Error de acceso a la base de datos';
	END;
	DECLARE EXIT HANDLER FOR sqlwarning
	BEGIN
		ROLLBACK;
		set pExito='Warning de acceso a la base de datos';
	END;

	START TRANSACTION;

		
		Insert into User (email, fechaDeAlta) values (pEmail, curdate());

		/** En ultimoId guardamos chess1, chess2, etc. 
			LAST_INSERT_ID() devuelve el id autonumérico del último registro insertado **/
		set @ultimoId=concat('jugadorGoogle', LAST_INSERT_ID());


		/************
		Suponiendo que ultimoId sea chess120, queremos conseguir una instruccion de este estilo: 

		CREATE USER 'chess120'@'localhost' IDENTIFIED BY 'pepe'

		...en donde pepe es la contraseña que se ha pasado como parámetro al procedimiento almacenado;
		************/

		Set @queryCrearUsuario = CONCAT('create User \'' , @ultimoId , '\'@\'%\' identified by \'', pPwd , '\';');

		PREPARE crearUsuario FROM @queryCrearUsuario;
		EXECUTE crearUsuario;

		Set @queryCrearUsuario = CONCAT('create User \'' , @ultimoId , '\'@\'localhost\' identified by \'', pPwd , '\';');

		PREPARE crearUsuario FROM @queryCrearUsuario;
		EXECUTE crearUsuario;


		/*********
		Abajo queremos una instruccion tipo grant SELECT,INSERT,DELETE,UPDATE,EXECUTE on ajedrez.* to 'chess120'@'%'
		**********/	
		Set @queryAsignarPermisos = CONCAT('grant SELECT,INSERT,DELETE,UPDATE,EXECUTE on juegos.* to \'', @ultimoId, '\'@\'%\';');

		PREPARE asignarPermisos FROM @queryAsignarPermisos;
		EXECUTE asignarPermisos;

		/*********
		Abajo queremos una instruccion tipo grant SELECT,INSERT,DELETE,UPDATE,EXECUTE on ajedrez.* to 'chess120'@'%'
		**********/	
		Set @queryAsignarPermisos = CONCAT('grant SELECT,INSERT,DELETE,UPDATE,EXECUTE on juegos.* to \'', @ultimoId, '\'@\'localhost\';');

		PREPARE asignarPermisos FROM @queryAsignarPermisos;
		EXECUTE asignarPermisos;

		Set pExito='OK';
		set pQ1=@queryCrearUsuario;
		set pQ2=@queryAsignarPermisos;
	COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-05-09 14:39:32
