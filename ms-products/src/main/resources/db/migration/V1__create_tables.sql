CREATE TABLE tb_manufacturer(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(13) NOT NULL UNIQUE
);


CREATE TABLE tb_products(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price NUMERIC(38, 2) NOT NULL,
    manufacturer_id BIGSERIAL NOT NULL,
    FOREIGN KEY (manufacturer_id) REFERENCES tb_manufacturer(id)
);

CREATE TABLE tb_categories(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE tb_products_categories(
    product_id BIGSERIAL,
    category_id BIGSERIAL,
    FOREIGN KEY (product_id) REFERENCES tb_products(id),
    FOREIGN KEY (category_id) REFERENCES tb_categories(id),
    PRIMARY KEY (product_id, category_id)
);