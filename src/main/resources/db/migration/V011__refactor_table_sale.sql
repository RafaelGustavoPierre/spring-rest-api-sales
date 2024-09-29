ALTER TABLE sale DROP FOREIGN KEY `sale_user_fk`;
ALTER TABLE sale DROP KEY `sale_user_fk`;

ALTER TABLE sale DROP COLUMN id_user;
ALTER TABLE sale ADD COLUMN id_client bigint NOT NULL;

ALTER TABLE sale ADD CONSTRAINT `sale_client_fk` FOREIGN KEY (`id_client`) REFERENCES `client` (`id`);