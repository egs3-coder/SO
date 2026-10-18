CREATE TABLE doador (
    id                        BIGSERIAL PRIMARY KEY,
    nome                      VARCHAR(120)     NOT NULL,
    cpf                       VARCHAR(11)      NOT NULL UNIQUE,
    data_nascimento           DATE             NOT NULL,
    sexo                      VARCHAR(10)      NOT NULL,
    peso_kg                   DOUBLE PRECISION NOT NULL,
    tipo_sanguineo            VARCHAR(12)      NOT NULL,
    consentimento_responsavel BOOLEAN          NOT NULL DEFAULT FALSE
);

CREATE TABLE bolsa (
    id             BIGSERIAL PRIMARY KEY,
    tipo_sanguineo VARCHAR(12) NOT NULL,
    data_coleta    DATE        NOT NULL,
    validade       DATE        NOT NULL,
    status         VARCHAR(12) NOT NULL
);

CREATE INDEX idx_bolsa_status_validade ON bolsa (status, validade);

CREATE TABLE doacao (
    id          BIGSERIAL PRIMARY KEY,
    doador_id   BIGINT NOT NULL REFERENCES doador (id),
    data_coleta DATE   NOT NULL,
    bolsa_id    BIGINT NOT NULL UNIQUE REFERENCES bolsa (id)
);

CREATE INDEX idx_doacao_doador ON doacao (doador_id);
