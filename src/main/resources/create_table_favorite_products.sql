DROP TABLE IF EXISTS inventory.favorite_products CASCADE;
CREATE TABLE inventory.favorite_products
(
    id SERIAL,
    product_color_id INT references inventory.product_color (id),
    user_id VARCHAR NOT NULL,
    PRIMARY KEY(id)
);
