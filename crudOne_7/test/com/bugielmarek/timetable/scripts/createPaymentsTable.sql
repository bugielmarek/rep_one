CREATE TABLE `payments` (
  `payment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `deadline` date NOT NULL,
  `name` varchar(80) NOT NULL,
  `syg_number` varchar(10) NOT NULL,
  `case_type` varchar(4) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `FKj94hgy9v5fw1munb90tar2eje` (`user_id`),
  CONSTRAINT `FKj94hgy9v5fw1munb90tar2eje` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL);