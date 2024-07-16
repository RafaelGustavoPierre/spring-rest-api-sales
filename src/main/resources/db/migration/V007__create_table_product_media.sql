CREATE TABLE product_media(
	id bigint NOT NULL AUTO_INCREMENT,
	id_product bigint NOT NULL,
	file_name VARCHAR(150) NOT NULL,
	content_type VARCHAR(80) NOT NULL,
	size int NOT NULL,

	primary key(id),
	constraint media_product_fk foreign key(id_product) references product(id)
) engine=InnoDB default charset=utf8;