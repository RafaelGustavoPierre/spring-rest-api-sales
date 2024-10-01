CREATE TABLE seller (
	id bigint NOT NULL AUTO_INCREMENT,
	name varchar(60) NOT NULL,
	email varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	date_register datetime NOT NULL,
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO seller (name, email, password, date_register) VALUES ("Chico Vendedor", "rafaelrestapi+chicovendedor@gmail.com", "$2a$12$d4c15TdN06.JBBqdO2djf.tZXPDgtjaSWQaVfyRTzBb8QsIhwbsjS", NOW());
INSERT INTO seller (name, email, password, date_register) VALUES ("Chuvisco Vendedor", "rafaelrestapi+chuviscovendedor@gmail.com", "$2a$12$d4c15TdN06.JBBqdO2djf.tZXPDgtjaSWQaVfyRTzBb8QsIhwbsjS", NOW());
INSERT INTO seller (name, email, password, date_register) VALUES ("Telasco Vendedor", "rafaelrestapi+telascovendedor@gmail.com", "$2a$12$d4c15TdN06.JBBqdO2djf.tZXPDgtjaSWQaVfyRTzBb8QsIhwbsjS", NOW());