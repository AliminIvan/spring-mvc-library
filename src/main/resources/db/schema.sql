CREATE TABLE authors
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    nationality VARCHAR(255),
    birthdate   DATE
);

CREATE TABLE books
(
    id             UUID PRIMARY KEY,
    title          VARCHAR(255) NOT NULL,
    isbn           VARCHAR(20)  NOT NULL UNIQUE,
    publication_date DATE
);

CREATE TABLE book_authors
(
    book_id   UUID,
    author_id UUID,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES books (id),
    FOREIGN KEY (author_id) REFERENCES authors (id)
);