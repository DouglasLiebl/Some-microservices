CREATE TABLE tb_users(
    id BIGSERIAL PRIMARY KEY,
    cpf VARCHAR(11) CONSTRAINT uniqueCpf UNIQUE,
    email VARCHAR(255) CONSTRAINT uniqueEmail UNIQUE,
    created_at TIMESTAMP(6),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255) CHECK (role in ('ADMIN', 'USER')),
    updated_at TIMESTAMP(6)
);

CREATE TABLE tb_refresh_tokens(
    id BIGSERIAL PRIMARY KEY,
    expiry_date TIMESTAMP(6) WITH TIME ZONE,
    refresh_token VARCHAR(255),
    user_id BIGSERIAL CONSTRAINT userId UNIQUE CONSTRAINT userId_refreshToken REFERENCES tb_users
);