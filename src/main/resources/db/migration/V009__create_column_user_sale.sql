ALTER TABLE sale ADD id_user BIGINT NOT NULL;
ALTER TABLE sale ADD CONSTRAINT sale_user_fk FOREIGN KEY (id_user) REFERENCES user(id);