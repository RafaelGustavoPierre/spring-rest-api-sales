ALTER TABLE sale DROP FOREIGN KEY sale_seller_fk;
ALTER TABLE sale DROP COLUMN id_seller;

ALTER TABLE sale DROP FOREIGN KEY sale_client_fk;
ALTER TABLE sale DROP COLUMN id_client;

DROP TABLE seller;
DROP TABLE client;