CREATE TABLE IF NOT EXISTS users (
    id          SERIAL  PRIMARY KEY,
    name        TEXT    NOT NULL,
    email       TEXT    NOT NULL,
    password    TEXT    NOT NULL,
    status      TEXT    NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS user_email_active_unique_idx
ON users (email)
WHERE status = 'ACTIVE';

CREATE TABLE IF NOT EXISTS amount_limits (
    user_id     BIGINT          NOT NULL    REFERENCES users (id),
    limit_type  TEXT            NOT NULL,
    amount      DECIMAL(19, 2)  NOT NULL,
    limit_at    TIMESTAMPTZ     NOT NULL,
    PRIMARY KEY (user_id, limit_type)
);

CREATE TABLE IF NOT EXISTS balances (
    user_id     BIGINT          NOT NULL    REFERENCES users (id),
    currency    VARCHAR(3)      NOT NULL,
    amount      DECIMAL(19, 2)  NOT NULL,
    PRIMARY KEY (user_id, currency)
);

CREATE TABLE IF NOT EXISTS quotes (
    id              SERIAL          PRIMARY KEY,
    from_user_id    BIGINT          NOT NULL        REFERENCES users (id),
    to_user_id      BIGINT          NOT NULL        REFERENCES users (id),
    from_currency   VARCHAR(3)      NOT NULL,
    to_currency     VARCHAR(3)      NOT NULL,
    from_amount     DECIMAL(19, 2)  NOT NULL,
    to_amount       DECIMAL(19, 2)  NOT NULL,
    fee             DECIMAL(19, 2)  NOT NULL,
    created_at      TIMESTAMPTZ     NOT NULL        DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS transfers (
    id              SERIAL          PRIMARY KEY,
    quote_id        BIGINT          NOT NULL        REFERENCES quotes (id),
    transferred_at  TIMESTAMPTZ     NOT NULL        DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS withdrawals (
    id              SERIAL          PRIMARY KEY,
    user_id         BIGINT          NOT NULL        REFERENCES users (id),
    currency        VARCHAR(3)      NOT NULL,
    amount          DECIMAL(19, 2)  NOT NULL,
    withdrawn_at    TIMESTAMPTZ     NOT NULL        DEFAULT NOW()
);
