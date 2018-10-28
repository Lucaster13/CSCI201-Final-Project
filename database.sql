DROP DATABASE IF EXISTS password_manager;
CREATE DATABASE password_manager;
USE password_manager;
CREATE TABLE user (
	userID int(10) PRIMARY KEY NOT NULL AUTO_INCREMENT,
	username varchar(100) NOT NULL UNIQUE,
	master_pass varchar(250) NOT NULL,
	last_update datetime NOT NULL,
	email varchar(200) NOT NULL UNIQUE,
	phone varchar(25) NOT NULL UNIQUE
);
CREATE TABLE password (
	passwordID int(15) PRIMARY KEY NOT NULL AUTO_INCREMENT,
	userID int(10) NOT NULL,
	app_name varchar(100) NOT NULL,
	encrypted_pass varchar(250) NOT NULL,
	last_update datetime NOT NULL,
	suggested_reset datetime NOT NULL,
	FOREIGN KEY fk1(userID) REFERENCES user(userID)
);
CREATE TABLE security_question (
	security_questionID int(15) PRIMARY KEY NOT NULL AUTO_INCREMENT,
	passwordID int(15) NOT NULL,
	question varchar(500) NOT NULL,
	answer varchar(150) NOT NULL,
	FOREIGN KEY fk2(passwordID) REFERENCES password(passwordID)
);