CREATE TABLE invoices
(
    id              BIGSERIAL PRIMARY KEY ,
    subscription_id BIGINT                                  NOT NULL,
    description     VARCHAR(255),
    amount          DECIMAL                                 NOT NULL,
    status          VARCHAR(255)                            NOT NULL,
    created_date      date                                  NOT NULL
);

CREATE TABLE payments
(
    id         BIGSERIAL PRIMARY KEY,
    payment_id VARCHAR(255),
    invoice_id BIGINT                                       NOT NULL,
    payment_method VARCHAR(255)                             NOT NULL,
    status     VARCHAR(255)                                 NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE                  NOT NULL,
    idempotence_key VARCHAR(255)                            NOT NULL
);

CREATE TABLE plans
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255)                              NOT NULL,
    description   TEXT,
    price         DECIMAL                                   NOT NULL,
    billing_cycle VARCHAR(255)                              NOT NULL,
    max_users     BIGINT                                    NOT NULL
);

CREATE TABLE subscriptions
(
    id       BIGSERIAL PRIMARY KEY,
    owner_id BIGINT                                         NOT NULL,
    plan_id  BIGINT                                         NOT NULL,
    status   VARCHAR(255)                                   NOT NULL,
    start_date date,
    end_date   date
);

CREATE TABLE users
(
    id              BIGSERIAL PRIMARY KEY,
    subscription_id BIGINT,
    login           VARCHAR(255)                            NOT NULL,
    name            VARCHAR(255)                            NOT NULL,
    email           VARCHAR(255)                            NOT NULL,
    password        VARCHAR(255)                            NOT NULL,
    CONSTRAINT uniq_email UNIQUE(email)
);