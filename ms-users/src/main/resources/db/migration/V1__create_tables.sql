CREATE TABLE tb_users(
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP(6) NOT NULL,
    password VARCHAR(300) NOT NULL,
    role VARCHAR(6) NOT NULL
                     );