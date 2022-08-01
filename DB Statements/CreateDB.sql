CREATE SCHEMA recipedb;

CREATE TABLE `recipedb`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NOT NULL,
  `password` VARCHAR(4000) NOT NULL,
  `dispname` VARCHAR(100) NULL,
  `role` VARCHAR(45) NULL DEFAULT 'User',
  `enabled` TINYINT NULL DEFAULT 1,
  PRIMARY KEY (`id`));
