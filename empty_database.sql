DROP DATABASE  IF EXISTS `diploma_process_facilitator`;

CREATE DATABASE  IF NOT EXISTS `diploma_process_facilitator`;
USE `diploma_process_facilitator`;
ALTER DATABASE papier_picker DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `password` char(80) NOT NULL,
  `academic_title` varchar(30),
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int NOT NULL AUTO_INCREMENT,
  `department` varchar(100) NOT NULL,
  `field` varchar(100) DEFAULT NULL,
  `study_mode` varchar(50) DEFAULT NULL,
  `study_group` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

DROP TABLE IF EXISTS `department_user`;
CREATE TABLE `department_user` (
  `user_id` int NOT NULL,
  `department_id` int DEFAULT NULL,

  CONSTRAINT `FK_USER_DEP01` FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`),
  CONSTRAINT `FK_USER_DEP02` FOREIGN KEY (`department_id`) 
  REFERENCES `department` (`id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  
  PRIMARY KEY (`user_id`,`role_id`),
  
  KEY `FK_ROLE_idx` (`role_id`),
  
  CONSTRAINT `FK_USER_05` FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  
  CONSTRAINT `FK_ROLE` FOREIGN KEY (`role_id`) 
  REFERENCES `role` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `diploma_topic`;
CREATE TABLE `diploma_topic` (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` int NOT NULL,
  `promoter_id` int NOT NULL,
  `subject` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL,
  
  PRIMARY KEY (`id`),
  
  CONSTRAINT `FK_DIPLOMA_STUDENT` FOREIGN KEY (`student_id`) 
  REFERENCES `user` (`id`),
  
  CONSTRAINT `FK_DIPLOMA_PROMOTER` FOREIGN KEY (`promoter_id`) 
  REFERENCES `user` (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=1;

SET FOREIGN_KEY_CHECKS = 1;