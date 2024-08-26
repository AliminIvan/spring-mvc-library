INSERT INTO authors (id, name, nationality, birthdate)
VALUES (RANDOM_UUID(), 'J.K. Rowling', 'British', '1965-07-31'),
       (RANDOM_UUID(), 'J.R.R. Tolkien', 'British', '1892-01-03'),
       (RANDOM_UUID(), 'George R.R. Martin', 'American', '1948-09-20');

INSERT INTO books (id, title, isbn, publication_date)
VALUES (RANDOM_UUID(), 'Harry Potter and the Philosopher''s Stone', '9780747532699', '1997-06-26'),
       (RANDOM_UUID(), 'The Lord of the Rings', '9780618640157', '1954-07-29'),
       (RANDOM_UUID(), 'A Game of Thrones', '9780553103540', '1996-08-06');


INSERT INTO book_authors (book_id, author_id)
SELECT b.id, a.id
FROM books b,
     authors a
WHERE b.title = 'Harry Potter and the Philosopher''s Stone'
  AND a.name = 'J.K. Rowling';

INSERT INTO book_authors (book_id, author_id)
SELECT b.id, a.id
FROM books b,
     authors a
WHERE b.title = 'The Lord of the Rings'
  AND a.name = 'J.R.R. Tolkien';

INSERT INTO book_authors (book_id, author_id)
SELECT b.id, a.id
FROM books b,
     authors a
WHERE b.title = 'A Game of Thrones'
  AND a.name = 'George R.R. Martin';