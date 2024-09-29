CREATE TABLE client (
	id bigint NOT NULL AUTO_INCREMENT,
	name varchar(60) NOT NULL,
	email varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	date_register datetime NOT NULL,
	PRIMARY KEY(id)
) ENGINE=InnoDB CHARSET=utf8mb4;

INSERT INTO client (name, email, password, date_register) VALUES ("João Cliente", "rafaelrestapi+joaocliente@gmail.com", "$2a$12$d4c15TdN06.JBBqdO2djf.tZXPDgtjaSWQaVfyRTzBb8QsIhwbsjS", NOW());
INSERT INTO client (name, email, password, date_register) VALUES ("Pedro Cliente", "rafaelrestapi+pedrocliente@gmail.com", "$2a$12$d4c15TdN06.JBBqdO2djf.tZXPDgtjaSWQaVfyRTzBb8QsIhwbsjS", NOW());
INSERT INTO client (name, email, password, date_register) VALUES ("Geovana Cliente", "rafaelrestapi+geovanacliente@gmail.com", "$2a$12$d4c15TdN06.JBBqdO2djf.tZXPDgtjaSWQaVfyRTzBb8QsIhwbsjS", NOW());