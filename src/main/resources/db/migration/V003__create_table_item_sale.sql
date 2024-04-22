CREATE TABLE product_sale (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(30) NOT NULL,
	price DECIMAL(10,2),
	quantity BIGINT NOT NULL,
	id_sale BIGINT NOT NULL,
	id_product BIGINT NOT NULL,

	PRIMARY KEY(id),
	FOREIGN KEY(id_sale) REFERENCES sale(id),
	FOREIGN KEY(id_product) REFERENCES product(id)
);