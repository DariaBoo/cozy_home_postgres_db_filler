DROP TABLE IF EXISTS inventory.basket_records CASCADE;
CREATE TABLE inventory.basket_records
(
    id SERIAL,
    product_color_id INT references inventory.product_color (id),
    quantity INT,
    user_id VARCHAR NOT NULL,
    PRIMARY KEY(id)
);
