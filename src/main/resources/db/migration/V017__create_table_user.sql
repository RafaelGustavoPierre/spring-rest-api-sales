CREATE TABLE `user` (
	id BIGINT AUTO_INCREMENT,
	email VARCHAR(255) NOT NULL,
	name VARCHAR(100) NOT NULL,
	password VARCHAR(100) NOT NULL,
	date_register DATETIME NOT NULL,
	PRIMARY KEY (id)
);

INSERT INTO `user` (email, name, password, date_register) VALUES ("rafaelrestapi+rafael@gmail.com", "Rafael", "$2a$12$m9907DqXWiPa.rkQRlnh9OGRp0BamkTmORZOo2/PgtXpALCuL6EbG", utc_timestamp);
INSERT INTO `user` (email, name, password, date_register) VALUES ("rafaelrestapi+telasco@gmail.com", "Telasco", "$2a$12$m9907DqXWiPa.rkQRlnh9OGRp0BamkTmORZOo2/PgtXpALCuL6EbG", utc_timestamp);
INSERT INTO `user` (email, name, password, date_register) VALUES ("rafaelrestapi+paulinho@gmail.com", "Paulinho", "$2a$12$m9907DqXWiPa.rkQRlnh9OGRp0BamkTmORZOo2/PgtXpALCuL6EbG", utc_timestamp);