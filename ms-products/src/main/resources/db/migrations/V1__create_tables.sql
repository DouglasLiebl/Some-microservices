CREATE TABLE tb_manufacturer(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(13) NOT NULL
);


CREATE TABLE tb_products(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
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

CREATE TABLE tb_manufacturer_addresses(
    manufacturer_id BIGSERIAL PRIMARY KEY,
    zipCode VARCHAR(15) NOT NULL,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(10) NOT NULL,
    complement VARCHAR(255),
    district VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(50) NOT NULL,
    FOREIGN KEY (manufacturer_id) REFERENCES tb_manufacturer(id)
);