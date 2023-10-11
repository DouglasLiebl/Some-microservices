CREATE DATABASE mydb;

USE mydb;

CREATE TABLE tb_regioes(
    cod_regiao BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_regiao VARCHAR(45),
    obs_regiao VARCHAR(120)
);

CREATE TABLE tb_estados(
    cod_estado BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_estado VARCHAR(45) NOT NULL,
    sigla_estaddo VARCHAR(2),
    cod_regiao BIGINT NOT NULL,
    FOREIGN KEY (cod_regiao) REFERENCES tb_regioes(cod_regiao)
);

CREATE TABLE tb_cidades(
    cod_cidade BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_cidade VARCHAR(45) NOT NULL,
    abrev_cidade VARCHAR(45),
    cod_estado BIGINT NOT NULL,
    FOREIGN KEY (cod_estado) REFERENCES tb_estados(cod_estado)
);

CREATE TABLE tb_clientes(
    cod_cliente BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_cliente VARCHAR(45) NOT NULL,
    nasc_cliente DATE NOT NULL,
    cpf_cliente VARCHAR(11) NOT NULL UNIQUE,
    rg_cliente VARCHAR(9) NOT NULL UNIQUE,
    emissorrg_cliente VARCHAR(10) NOT NULL,
    sexo_cliente VARCHAR(1) NOT NULL,
    fone_cliente VARCHAR(10),
    cel_cliente VARCHAR(10) NOT NULL,
    email_cliente VARCHAR(50) NOT NULL UNIQUE,
    rua_cliente VARCHAR(60) NOT NULL,
    numero_cliente VARCHAR(10),
    complemento_cliente VARCHAR(15),
    bairro_cliente VARCHAR(25) NOT NULL,
    cep_cliente VARCHAR(8) NOT NULL,
    cod_cidade BIGINT NOT NULL,
    FOREIGN KEY (cod_cidade) REFERENCES tb_cidades(cod_cidade)
);

CREATE TABLE tb_transportadora(
    cod_transportadora BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_transportadora VARCHAR(45) NOT NULL,
    cnpj_transportadora VARCHAR(14) NOT NULL UNIQUE,
    ie_transportadora VARCHAR(9) NOT NULL,
    fone_transportadora VARCHAR(10),
    email_transportadora VARCHAR(50) NOT NULL,
    rua_transportadora VARCHAR(60),
    numero_transportadora VARCHAR(10),
    complemento_transportadora VARCHAR(15),
    bairro_transportadora VARCHAR(25) NOT NULL,
    cep_transportadora VARCHAR(8) NOT NULL,
    cod_cidade BIGINT NOT NULL,
    FOREIGN KEY (cod_cidade) REFERENCES tb_cidades(cod_cidade)
);

CREATE TABLE tb_pedidos(
    cod_pedido BIGINT PRIMARY KEY AUTO_INCREMENT,
    data_pedido DATE NOT NULL,
    data_entrega_pedido DATE NOT NULL,
    obs_pedido VARCHAR(120),
    vlr_frete_pedido NUMERIC(38, 2),
    cod_cliente BIGINT NOT NULL,
    cod_transportadora BIGINT NOT NULL,
    FOREIGN KEY (cod_cliente) REFERENCES tb_clientes(cod_cliente),
    FOREIGN KEY (cod_transportadora) REFERENCES tb_transportadora(cod_transportadora)
);

CREATE TABLE tb_marca(
    cod_marca BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_marca VARCHAR(45) NOT NULL,
    obs_marca VARCHAR(120)
);

CREATE TABLE tb_categorias(
    cod_categoria BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_categoria VARCHAR(45) NOT NULL,
    descricao_categoria VARCHAR(120)
);

CREATE TABLE tb_subcategorias(
    cod_subcategoria BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_subcategoria VARCHAR(45) NOT NULL,
    desc_categoria VARCHAR(120),
    cod_categoria BIGINT NOT NULL,
    FOREIGN KEY (cod_categoria) REFERENCES tb_categorias(cod_categoria)
);

CREATE TABLE tb_produto(
    cod_produto BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_produto VARCHAR(45) NOT NULL,
    comp_produto VARCHAR(45),
    desc_produto VARCHAR(45),
    preco_produto VARCHAR(45) NOT NULL,
    importacao_produto BOOLEAN NOT NULL,
    cod_marca BIGINT NOT NULL,
    cod_subcategoria BIGINT NOT NULL,
    FOREIGN KEY (cod_marca) REFERENCES tb_marca(cod_marca),
    FOREIGN KEY (cod_subcategoria) REFERENCES tb_subcategorias(cod_subcategoria)
);

CREATE TABLE tb_itens_pedido(
    cod_pedido BIGINT NOT NULL,
    cod_produto BIGINT NOT NULL,
    qtde_produto INT NOT NULL,
    FOREIGN KEY (cod_pedido) REFERENCES tb_pedidos(cod_pedido),
    FOREIGN KEY (cod_produto) REFERENCES tb_produto(cod_produto)
);

CREATE TABLE tb_bancooperadora(
    cod_bancooperadora BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_bancooperadora VARCHAR(45) NOT NULL,
    abrev_bancooperadora VARCHAR(45)
);

CREATE TABLE tb_pagamento(
    cod_pagamento BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_pagamento VARCHAR(45),
    cod_bancooperadora BIGINT NOT NULL,
    FOREIGN KEY (cod_bancooperadora) REFERENCES tb_bancooperadora(cod_bancooperadora)
);

CREATE TABLE tb_parcelas(
    cod_parcela BIGINT PRIMARY KEY AUTO_INCREMENT,
    num_parcela INT NOT NULL,
    vencimento_parcela DATE NOT NULL,
    valor_parcela VARCHAR(45),
    cod_pedido BIGINT NOT NULL,
    cod_pagamento BIGINT NOT NULL,
    FOREIGN KEY (cod_pedido) REFERENCES tb_pedidos(cod_pedido),
    FOREIGN KEY (cod_pagamento) REFERENCES tb_pagamento(cod_pagamento)
);