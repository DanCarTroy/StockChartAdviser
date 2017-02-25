CREATE DATABASE `stock_adviser_db`;

CREATE TABLE `user` (
  `user_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_login` VARCHAR(60) NOT NULL,
  `user_pass` VARCHAR(64) NOT NULL,
  `first_name` VARCHAR(50) NULL,
  `last_name` VARCHAR(50) NULL,
  `user_email` VARCHAR(110) NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `userID_UNIQUE` (`user_id` ASC),
  UNIQUE INDEX `user_login_UNIQUE` (`user_login` ASC),
  UNIQUE INDEX `user_email_UNIQUE` (`user_email` ASC))
ENGINE = InnoDB;

INSERT INTO `stock_adviser_db`.`user` (`user_login`, `user_pass`) VALUES ('team10', 'comp354');