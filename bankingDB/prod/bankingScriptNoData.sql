-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema banking
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema banking
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `banking` DEFAULT CHARACTER SET utf8 ;
USE `banking` ;

-- -----------------------------------------------------
-- Table `banking`.`account_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`account_status` (
  `id_status` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_status`),
  UNIQUE INDEX `description_UNIQUE` (`description` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`accounts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`accounts` (
  `id_accounts` INT NOT NULL AUTO_INCREMENT,
  `balance` DOUBLE NOT NULL DEFAULT '0',
  `account_number` VARCHAR(20) NOT NULL,
  `yearly_interest_rate` DOUBLE NOT NULL,
  `id_account_status` INT NOT NULL,
  PRIMARY KEY (`id_accounts`),
  UNIQUE INDEX `account_number` (`account_number` ASC) VISIBLE,
  INDEX `fk_accounts_account_status_idx` (`id_account_status` ASC) VISIBLE,
  CONSTRAINT `fk_accounts_account_status`
    FOREIGN KEY (`id_account_status`)
    REFERENCES `banking`.`account_status` (`id_status`))
ENGINE = InnoDB
AUTO_INCREMENT = 41
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`card_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`card_status` (
  `id_status` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_status`),
  UNIQUE INDEX `status_UNIQUE` (`status` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`user_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`user_roles` (
  `id_roles` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_roles`),
  UNIQUE INDEX `description_UNIQUE` (`description` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`users` (
  `id_users` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(320) NOT NULL,
  `password` VARCHAR(256) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `patronymic` VARCHAR(45) NULL DEFAULT NULL,
  `passport_series` VARCHAR(3) NOT NULL,
  `passport_number` VARCHAR(15) NOT NULL,
  `birthdate` DATE NOT NULL,
  `last_login` DATE NOT NULL,
  `created` DATE NOT NULL,
  `id_role` INT NOT NULL,
  PRIMARY KEY (`id_users`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  INDEX `fk_users_user_roles1_idx` (`id_role` ASC) VISIBLE,
  CONSTRAINT `fk_users_user_roles1`
    FOREIGN KEY (`id_role`)
    REFERENCES `banking`.`user_roles` (`id_roles`))
ENGINE = InnoDB
AUTO_INCREMENT = 21
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`card_types`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`card_types` (
  `id_card_type` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_card_type`),
  UNIQUE INDEX `type_UNIQUE` (`type` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`bank_cards`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`bank_cards` (
  `id_bank_cards` INT NOT NULL AUTO_INCREMENT,
  `credit_card_number` VARCHAR(45) NOT NULL,
  `cvc_code` VARCHAR(3) NOT NULL,
  `pin_code` VARCHAR(4) NOT NULL,
  `expiration_date` DATE NOT NULL,
  `registration_date` DATE NOT NULL,
  `balance` DOUBLE NULL DEFAULT NULL,
  `overdraft_maximum` DOUBLE NULL DEFAULT NULL,
  `overdraft_interest_rate` DOUBLE NULL DEFAULT NULL,
  `id_user` INT NOT NULL,
  `id_account` INT NULL DEFAULT NULL,
  `id_card_type` INT NOT NULL,
  `id_card_status` INT NOT NULL,
  PRIMARY KEY (`id_bank_cards`),
  UNIQUE INDEX `uc_num_cvc` (`credit_card_number` ASC, `cvc_code` ASC) VISIBLE,
  INDEX `fk_credit_cards_card_types1_idx` (`id_card_type` ASC) VISIBLE,
  INDEX `fk_bank_cards_card_status1_idx` (`id_card_status` ASC) VISIBLE,
  INDEX `fk_bank_cards_accounts1_idx` (`id_account` ASC) VISIBLE,
  INDEX `fk_bank_cards_users1_idx` (`id_user` ASC) VISIBLE,
  CONSTRAINT `fk_bank_cards_accounts1`
    FOREIGN KEY (`id_account`)
    REFERENCES `banking`.`accounts` (`id_accounts`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_bank_cards_card_status1`
    FOREIGN KEY (`id_card_status`)
    REFERENCES `banking`.`card_status` (`id_status`),
  CONSTRAINT `fk_bank_cards_users1`
    FOREIGN KEY (`id_user`)
    REFERENCES `banking`.`users` (`id_users`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_credit_cards_card_types1`
    FOREIGN KEY (`id_card_type`)
    REFERENCES `banking`.`card_types` (`id_card_type`))
ENGINE = InnoDB
AUTO_INCREMENT = 42
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`bill_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`bill_status` (
  `id_bill_status` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_bill_status`),
  UNIQUE INDEX `status_UNIQUE` (`status` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`loan_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`loan_status` (
  `id_loan_status` INT NOT NULL AUTO_INCREMENT,
  `status_desc` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_loan_status`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`loans`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`loans` (
  `id_loans` INT NOT NULL AUTO_INCREMENT,
  `single_payment_value` DOUBLE NOT NULL,
  `starting_value` DOUBLE NOT NULL,
  `total_payment_value` DOUBLE NOT NULL,
  `yearly_interest_rate` DOUBLE NOT NULL,
  `date_of_issue` DATE NOT NULL,
  `due_date` DATE NOT NULL,
  `users_id_users` INT NOT NULL,
  `id_status` INT NOT NULL,
  `id_card` INT NULL DEFAULT NULL,
  `id_account` INT NOT NULL,
  PRIMARY KEY (`id_loans`),
  INDEX `fk_loans_users1_idx` (`users_id_users` ASC) VISIBLE,
  INDEX `fk_loans_loan_status` (`id_status` ASC) VISIBLE,
  INDEX `id_account` (`id_account` ASC) VISIBLE,
  INDEX `id_card` (`id_card` ASC) VISIBLE,
  CONSTRAINT `fk_loans_loan_status`
    FOREIGN KEY (`id_status`)
    REFERENCES `banking`.`loan_status` (`id_loan_status`),
  CONSTRAINT `fk_loans_users1`
    FOREIGN KEY (`users_id_users`)
    REFERENCES `banking`.`users` (`id_users`)
    ON DELETE CASCADE,
  CONSTRAINT `loans_ibfk_2`
    FOREIGN KEY (`id_account`)
    REFERENCES `banking`.`accounts` (`id_accounts`)
    ON DELETE CASCADE,
  CONSTRAINT `loans_ibfk_3`
    FOREIGN KEY (`id_card`)
    REFERENCES `banking`.`bank_cards` (`id_bank_cards`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`penalty_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`penalty_type` (
  `id_penalty_type` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_penalty_type`),
  UNIQUE INDEX `type_UNIQUE` (`type` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`penalty_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`penalty_status` (
  `id_penalty_status` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_penalty_status`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`penalties`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`penalties` (
  `id_penalties` INT NOT NULL AUTO_INCREMENT,
  `value` DOUBLE NULL DEFAULT NULL,
  `notice` TEXT NULL DEFAULT NULL,
  `id_payment_account` INT NULL DEFAULT NULL,
  `id_penalty_type` INT NOT NULL,
  `id_user` INT NOT NULL,
  `id_status` INT NOT NULL,
  PRIMARY KEY (`id_penalties`),
  INDEX `fk_penalties_penalty_type1_idx` (`id_penalty_type` ASC) VISIBLE,
  INDEX `id_payment_account` (`id_payment_account` ASC) VISIBLE,
  INDEX `id_status` (`id_status` ASC) VISIBLE,
  INDEX `fk_penalties_users` (`id_user` ASC) VISIBLE,
  CONSTRAINT `fk_penalties_accounts`
    FOREIGN KEY (`id_payment_account`)
    REFERENCES `banking`.`accounts` (`id_accounts`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_penalties_penalty_type1`
    FOREIGN KEY (`id_penalty_type`)
    REFERENCES `banking`.`penalty_type` (`id_penalty_type`),
  CONSTRAINT `fk_penalties_users`
    FOREIGN KEY (`id_user`)
    REFERENCES `banking`.`users` (`id_users`)
    ON DELETE CASCADE,
  CONSTRAINT `penalties_ibfk_3`
    FOREIGN KEY (`id_status`)
    REFERENCES `banking`.`penalty_status` (`id_penalty_status`))
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`bills`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`bills` (
  `id_bills` INT NOT NULL AUTO_INCREMENT,
  `value` DOUBLE NOT NULL,
  `issue_date` DATE NOT NULL,
  `due_date` DATE NULL DEFAULT NULL,
  `notice` VARCHAR(1000) CHARACTER SET 'utf8' NULL DEFAULT NULL,
  `id_user` INT NOT NULL,
  `bearer_id` INT NOT NULL,
  `id_payment_account` INT NOT NULL,
  `id_status` INT NOT NULL,
  `id_penalty` INT NULL DEFAULT NULL,
  `id_loans` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id_bills`),
  INDEX `fk_bills_bill_status1_idx` (`id_status` ASC) VISIBLE,
  INDEX `fk_bills_penalties1_idx` (`id_penalty` ASC) VISIBLE,
  INDEX `fk_bills_users1_idx` (`id_user` ASC) VISIBLE,
  INDEX `fk_bills_accounts1_idx` (`id_payment_account` ASC) VISIBLE,
  INDEX `fk_bills_loans1_idx` (`id_loans` ASC) VISIBLE,
  INDEX `bearer_id` (`bearer_id` ASC) VISIBLE,
  CONSTRAINT `bills_ibfk_1`
    FOREIGN KEY (`bearer_id`)
    REFERENCES `banking`.`users` (`id_users`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_bills_accounts1`
    FOREIGN KEY (`id_payment_account`)
    REFERENCES `banking`.`accounts` (`id_accounts`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_bills_bill_status1`
    FOREIGN KEY (`id_status`)
    REFERENCES `banking`.`bill_status` (`id_bill_status`),
  CONSTRAINT `fk_bills_loans1`
    FOREIGN KEY (`id_loans`)
    REFERENCES `banking`.`loans` (`id_loans`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_bills_penalties1`
    FOREIGN KEY (`id_penalty`)
    REFERENCES `banking`.`penalties` (`id_penalties`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_bills_users1`
    FOREIGN KEY (`id_user`)
    REFERENCES `banking`.`users` (`id_users`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 10
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`operation_types`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`operation_types` (
  `id_operation` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_operation`),
  UNIQUE INDEX `description_UNIQUE` (`description` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 14
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`operations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`operations` (
  `id_operations` INT NOT NULL AUTO_INCREMENT,
  `value` DOUBLE NULL DEFAULT NULL,
  `commission` DOUBLE NULL DEFAULT NULL,
  `operation_date` DATETIME NOT NULL,
  `id_operation_type` INT NOT NULL,
  `id_account` INT NULL DEFAULT NULL,
  `id_target_account` INT NULL DEFAULT NULL,
  `id_bank_card` INT NULL DEFAULT NULL,
  `id_target_bank_card` INT NULL DEFAULT NULL,
  `id_bill` INT NULL DEFAULT NULL,
  `id_penalty` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id_operations`),
  INDEX `fk_operations_operation_types1_idx` (`id_operation_type` ASC) VISIBLE,
  INDEX `fk_operations_accounts1_idx` (`id_account` ASC) VISIBLE,
  INDEX `fk_operations_accounts2_idx` (`id_target_account` ASC) VISIBLE,
  INDEX `fk_operations_bank_cards1_idx` (`id_bank_card` ASC) VISIBLE,
  INDEX `fk_operations_bank_cards2_idx` (`id_target_bank_card` ASC) VISIBLE,
  INDEX `fk_operations_bills` (`id_bill` ASC) VISIBLE,
  INDEX `fk_penalties_operations` (`id_penalty` ASC) VISIBLE,
  CONSTRAINT `fk_operations_accounts1`
    FOREIGN KEY (`id_account`)
    REFERENCES `banking`.`accounts` (`id_accounts`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_operations_accounts2`
    FOREIGN KEY (`id_target_account`)
    REFERENCES `banking`.`accounts` (`id_accounts`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_operations_bank_cards1`
    FOREIGN KEY (`id_bank_card`)
    REFERENCES `banking`.`bank_cards` (`id_bank_cards`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_operations_bank_cards2`
    FOREIGN KEY (`id_target_bank_card`)
    REFERENCES `banking`.`bank_cards` (`id_bank_cards`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_operations_bills`
    FOREIGN KEY (`id_bill`)
    REFERENCES `banking`.`bills` (`id_bills`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_operations_operation_types1`
    FOREIGN KEY (`id_operation_type`)
    REFERENCES `banking`.`operation_types` (`id_operation`),
  CONSTRAINT `fk_penalties_operations`
    FOREIGN KEY (`id_penalty`)
    REFERENCES `banking`.`penalties` (`id_penalties`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 272
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `banking`.`users_has_accounts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banking`.`users_has_accounts` (
  `id_user` INT NOT NULL,
  `id_account` INT NOT NULL,
  PRIMARY KEY (`id_user`, `id_account`),
  INDEX `fk_users_has_accounts_accounts1_idx` (`id_account` ASC) VISIBLE,
  INDEX `fk_users_has_accounts_users1_idx` (`id_user` ASC) VISIBLE,
  CONSTRAINT `fk_users_has_accounts_accounts1`
    FOREIGN KEY (`id_account`)
    REFERENCES `banking`.`accounts` (`id_accounts`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_users_has_accounts_users1`
    FOREIGN KEY (`id_user`)
    REFERENCES `banking`.`users` (`id_users`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
