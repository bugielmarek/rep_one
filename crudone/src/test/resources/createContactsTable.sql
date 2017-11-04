DROP TABLE IF EXISTS `contacts`;

CREATE TABLE `contacts` (
  `contact_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(60) DEFAULT NULL,
  `first_name` varchar(60) DEFAULT NULL,
  `last_name` varchar(60) DEFAULT NULL,
  `name` varchar(80) DEFAULT NULL,
  `phone_numbers` varchar(60) DEFAULT NULL,
  `text` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`contact_id`));