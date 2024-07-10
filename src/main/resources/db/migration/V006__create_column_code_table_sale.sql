ALTER TABLE sale ADD code VARCHAR(36) NOT NULL after id;
UPDATE sale set code = UUID();
ALTER TABLE sale ADD CONSTRAINT uk_sale_code unique(code);