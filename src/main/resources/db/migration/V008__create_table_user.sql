CREATE TABLE user (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(60) NOT NULL,
	email VARCHAR(80) NOT NULL,
	password VARCHAR(255) NOT NULL,

	date_register datetime NOT NULL,

	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT user (name, email, password, date_register) VALUES ("Rafael", "rafaelrestapi+rafael@gmail.com", 123, utc_timestamp);
INSERT user (name, email, password, date_register) VALUES ("Gustavo", "rafaelrestapi+gustavo@gmail.com", 123, utc_timestamp);
INSERT user (name, email, password, date_register) VALUES ("Pierre", "rafaelrestapi+pierre@gmail.com", 123, utc_timestamp);