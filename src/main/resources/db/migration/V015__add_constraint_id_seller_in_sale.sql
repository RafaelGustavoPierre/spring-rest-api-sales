ALTER TABLE sale ADD COLUMN id_seller bigint;
ALTER TABLE sale ADD CONSTRAINT `sale_seller_fk` FOREIGN KEY (`id_seller`) REFERENCES `seller` (`id`);
ALTER TABLE sale MODIFY id_seller bigint NOT NULL;