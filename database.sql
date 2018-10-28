DROP DATABASE IF EXISTS password_manager;
CREATE DATABASE password_protector;
USE password_protecteor;
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
    username varchar(100) NOT NULL,
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
INSERT INTO user (username, master_pass, last_update, email, phone) VALUES
	("user1","$_dasd2131dadar2","2018-10-25","user1@test.com","(555) 555-5555"),
    ("user2","$_aetwgfsdfarqda","2018-10-27","user2@test.com","(555) 555-5556"),
    ("user3","$_asdbaiuhg272ca","2018-10-28","user3@test.com","(555) 555-5557");
INSERT INTO password (userID, username, app_name, encrypted_pass, last_update, suggested_reset) VALUES
	(1,"user1","Google","asd21fs92","2018-10-20","2018-11-20"),
    (1,"user1-","Facebook","adq2easda","2018-09-30","2018-10-30"),
    (1,"user1","Twitter","adfad21edasd","2018-01-01","2018-02-01"),
    (2,"user2","USC","asd21fs92","2018-08-01","2019-08-01"),
    (2,"user2","Google","adq2easda","2017-08-02","2018-08-02"),
    (3,"user3","MySpace","asd21fs92","2000-01-20","2000-07-20");
INSERT INTO security_question (passwordID, question, answer) VALUES
    (1, "Favorite color?", "Blue"),
    (1, "Favorite month?", "June"),
    (1, "Favorite CS201 CP?", "Judy"),
    (2, "Favorite color?", "Green"),
    (3, "Name of first pet?", "Max");
SELECT * FROM user;
SELECT * FROM password;
SELECT * FROM security_question;