DROP TABLE IF EXISTS stats CASCADE;
CREATE TABLE stats (
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app       VARCHAR(50)                       NOT NULL,
    uri       VARCHAR(200)                      NOT NULL,
    ip        VARCHAR(50)                       NOT NULL,
    timestamp TIMESTAMP     WITHOUT TIME ZONE   NOT NULL
);