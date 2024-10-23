ALTER TABLE sale DROP CONSTRAINT `sale_seller_fk`;
ALTER TABLE sale DROP CONSTRAINT `sale_client_fk`;

DROP TABLE seller;
DROP TABLE client;

ALTER TABLE sale DROP COLUMN id_seller;
ALTER TABLE sale DROP COLUMN id_client;

ALTER TABLE sale DROP FOREIGN KEY FKa3snnn1kxdye45qhqb6pfv0jg;
ALTER TABLE sale DROP FOREIGN KEY FKbkfcbr32i74mp2hf53g5cvd6y;

ALTER TABLE sale DROP INDEX FKa3snnn1kxdye45qhqb6pfv0jg;
ALTER TABLE sale DROP INDEX FKbkfcbr32i74mp2hf53g5cvd6y;