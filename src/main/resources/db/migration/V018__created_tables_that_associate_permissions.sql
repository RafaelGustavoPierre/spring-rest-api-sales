CREATE TABLE `user_group` (
	user_id BIGINT NOT NULL,
	group_id BIGINT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `group` (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `group_permission` (
	group_id BIGINT NOT NULL,
	permission_id BIGINT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `permission` (
	id BIGINT NOT NULL AUTO_INCREMENT,
	description VARCHAR(80) NOT NULL,
	name VARCHAR(40) NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE user_group ADD CONSTRAINT `fk_user_group_group` FOREIGN KEY (group_id) REFERENCES `group` (id);
ALTER TABLE user_group ADD CONSTRAINT `fk_user_group_user` FOREIGN KEY (user_id) REFERENCES `user` (id);

ALTER TABLE group_permission ADD CONSTRAINT `fk_group_permission_group` FOREIGN KEY (group_id) REFERENCES `group` (id);
ALTER TABLE group_permission ADD CONSTRAINT `fk_group_permission_permission` FOREIGN KEY (permission_id) REFERENCES `permission` (id);

INSERT INTO `group` (name) VALUES ("Seller");
INSERT INTO `group` (name) VALUES ("Client");

INSERT INTO permission (description, name) VALUES ("Allows you to edit Sales", "EDIT_SALE");
INSERT INTO permission (description, name) VALUES ("Allows you to consult Sales", "CONSULT_SALE");
INSERT INTO permission (description, name) VALUES ("Allows you to edit Products", "EDIT_PRODUCT");
INSERT INTO permission (description, name) VALUES ("Allows you to consult Products", "CONSULT_PRODUCT");

INSERT INTO group_permission VALUES (1, 1);
INSERT INTO group_permission VALUES (1, 2);
INSERT INTO group_permission VALUES (1, 3);
INSERT INTO group_permission VALUES (1, 4);
INSERT INTO group_permission VALUES (2, 2);

INSERT INTO user_group VALUES (1, 1);
INSERT INTO user_group VALUES (2, 2);
INSERT INTO user_group VALUES (3, 2);