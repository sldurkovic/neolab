/*
SQLyog Enterprise - MySQL GUI v8.14 
MySQL - 5.1.40-community : Database - neolab
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`neolab` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `neolab`;

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `cid` int(255) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `category` */

insert  into `category`(`cid`,`title`) values (1,'category 1');

/*Table structure for table `project_activity` */

DROP TABLE IF EXISTS `project_activity`;

CREATE TABLE `project_activity` (
  `paid` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  PRIMARY KEY (`paid`),
  KEY `pid` (`pid`),
  KEY `uid` (`uid`),
  CONSTRAINT `pid` FOREIGN KEY (`pid`) REFERENCES `projects` (`pid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `uid` FOREIGN KEY (`uid`) REFERENCES `users` (`uid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=latin1;

/*Data for the table `project_activity` */

insert  into `project_activity`(`paid`,`pid`,`uid`) values (1,1,1),(3,1,17),(4,9,1),(5,3,1),(24,28,1),(27,2,17),(29,9,17),(31,28,17),(32,28,13),(34,29,1),(35,2,1),(36,29,17),(37,2,13),(38,6,18);

/*Table structure for table `projects` */

DROP TABLE IF EXISTS `projects`;

CREATE TABLE `projects` (
  `pid` int(255) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `cid` int(255) NOT NULL,
  `date_created` date DEFAULT NULL,
  `created_by` int(255) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;

/*Data for the table `projects` */

insert  into `projects`(`pid`,`title`,`cid`,`date_created`,`created_by`,`description`) values (1,'Projekat 1',1,'2011-08-08',1,'simple project'),(2,'Projekat 2',1,'2011-08-09',1,'simple projects'),(3,'Projekat 3',1,'2011-08-10',1,'simple project'),(4,'Projekat 4',1,'2011-09-10',1,''),(6,'Projekat 5',2,'2011-09-10',1,'opis'),(8,'Projekat 7',2,'2011-09-10',1,'opis 7'),(9,'Projekat 9',1,'2011-09-11',1,'opis\nopis\nopis\nopis'),(28,'Test projekat',2,'2011-09-12',1,'Opis test projekta'),(29,'proba2',1,'2011-09-13',1,'aaaaaaaaaaa');

/*Table structure for table `task_activity` */

DROP TABLE IF EXISTS `task_activity`;

CREATE TABLE `task_activity` (
  `taid` int(255) NOT NULL AUTO_INCREMENT,
  `tid` int(255) DEFAULT NULL,
  `uid` int(255) DEFAULT NULL,
  PRIMARY KEY (`taid`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

/*Data for the table `task_activity` */

insert  into `task_activity`(`taid`,`tid`,`uid`) values (3,2,13),(4,1,1),(5,3,1),(6,8,1),(8,3,17),(9,8,17),(11,7,13),(12,9,17),(13,9,1),(14,10,1);

/*Table structure for table `tasks` */

DROP TABLE IF EXISTS `tasks`;

CREATE TABLE `tasks` (
  `tid` int(255) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `pid` int(255) NOT NULL,
  `date_start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date_end` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `status` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

/*Data for the table `tasks` */

insert  into `tasks`(`tid`,`title`,`pid`,`date_start`,`date_end`,`status`,`description`) values (1,'Task1',1,'2011-09-13 11:17:45','2011-08-25 00:00:00','FINISHED',''),(3,'Test task',1,'2011-09-16 11:25:38','2011-09-16 14:04:50','ACTIVE','simple description'),(5,'asd',2,'2011-09-07 12:31:00','2011-09-07 12:41:00','ACTIVE',''),(6,'new task',2,'2011-09-14 12:30:00','2011-09-15 12:30:00','ACTIVE','description'),(7,'another task',2,'2011-09-14 13:00:00','2011-09-14 13:10:00','ACTIVE','task'),(8,'probni task',29,'2011-09-14 12:32:00','2011-09-14 12:34:00','ACTIVE','task'),(9,'Task',9,'2011-09-16 12:12:00','2011-09-16 13:00:00','ACTIVE','task'),(10,'asd',3,'2011-09-16 18:12:00','2011-09-17 12:23:00','ACTIVE','asd');

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `uid` int(255) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `level` int(10) NOT NULL DEFAULT '3',
  `image` varchar(255) NOT NULL DEFAULT 'profile_default.png',
  `phone` varchar(255) NOT NULL,
  `site` varchar(255) NOT NULL,
  `email_host` varchar(255) NOT NULL,
  `email_port` int(255) NOT NULL DEFAULT '465',
  `email_protocol` varchar(255) NOT NULL DEFAULT 'smtps',
  `email_password` varchar(255) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

/*Data for the table `users` */

insert  into `users`(`uid`,`first_name`,`last_name`,`email`,`password`,`level`,`image`,`phone`,`site`,`email_host`,`email_port`,`email_protocol`,`email_password`) values (1,'Slobodan','Durkovic','sldurkovic@gmail.com','1234',1,'profile_default.png','+381691234123','www.neolab.com','smtp.gmail.com',465,'smtps','s0lmax2126'),(13,'Petar','Petrovic','petar@gmail.com','1234',3,'profile_default.png','','','',465,'smtps',''),(17,'Svetozar','Pavlovic','svetozarpavlovic87@gmail.com','bd6f4b58',3,'profile_default.png','','','',465,'smtps',''),(18,'Slobodan','','sldurkovic@gmail.com','5c25625f',3,'active-back.png','','','',465,'smtps','');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
