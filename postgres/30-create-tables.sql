\c reservations
CREATE TABLE reservation
(
    id              SERIAL PRIMARY KEY,
    reservation_uid uuid UNIQUE NOT NULL,
    username        VARCHAR(80) NOT NULL,
    book_uid        uuid        NOT NULL,
    library_uid     uuid        NOT NULL,
    status          VARCHAR(20) NOT NULL
        CHECK (status IN ('RENTED', 'RETURNED', 'EXPIRED')),
    start_date      TIMESTAMP   NOT NULL,
    till_date       TIMESTAMP   NOT NULL
);
GRANT ALL PRIVILEGES ON TABLE reservation TO program;

\c libraries
CREATE TABLE library
(
    id          SERIAL PRIMARY KEY,
    library_uid uuid UNIQUE  NOT NULL,
    name        VARCHAR(80)  NOT NULL,
    city        VARCHAR(255) NOT NULL,
    address     VARCHAR(255) NOT NULL
);

CREATE TABLE books
(
    id        SERIAL PRIMARY KEY,
    book_uid  uuid UNIQUE  NOT NULL,
    name      VARCHAR(255) NOT NULL,
    author    VARCHAR(255),
    genre     VARCHAR(255),
    condition VARCHAR(20) DEFAULT 'EXCELLENT'
        CHECK (condition IN ('EXCELLENT', 'GOOD', 'BAD'))
);

CREATE TABLE library_books
(
    id       SERIAL PRIMARY KEY,
    book_id         INT REFERENCES books (id),
    library_id      INT REFERENCES library (id),
    available_count INT NOT NULL
);
GRANT ALL PRIVILEGES ON TABLE library TO program;
GRANT ALL PRIVILEGES ON TABLE books TO program;
GRANT ALL PRIVILEGES ON TABLE library_books TO program;

\c ratings
CREATE TABLE rating
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(80) NOT NULL,
    stars    INT         NOT NULL
        CHECK (stars BETWEEN 0 AND 100)
);
GRANT ALL PRIVILEGES ON TABLE rating TO program;

