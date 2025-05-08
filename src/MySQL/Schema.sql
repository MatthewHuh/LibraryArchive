CREATE DATABASE IF NOT EXISTS LibraryArchive;

USE LibraryArchive;

CREATE TABLE IF NOT EXISTS book_info (
    isbn CHAR(13) NOT NULL,
    author VARCHAR(255) NOT NULL,
    genre VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    release_date DATE NOT NULL,
    PRIMARY KEY (isbn)
    );

CREATE TABLE IF NOT EXISTS members (
    member_id INTEGER NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number CHAR(10) NOT NULL,
    email VARCHAR(62) NOT NULL UNIQUE,
    address VARCHAR(130) NOT NULL,
    hashed_password VARCHAR(255) NOT NULL,
    is_admin BOOLEAN NOT NULL DEFAULT 0,
    PRIMARY KEY (member_id)
    );


CREATE TABLE IF NOT EXISTS libraries (
    library_id INTEGER NOT NULL AUTO_INCREMENT,
    address VARCHAR(250) NOT NULL,
    name VARCHAR(250) NOT NULL,
    PRIMARY KEY (library_id)
    );

CREATE TABLE IF NOT EXISTS books (
    book_id INTEGER NOT NULL AUTO_INCREMENT,
    isbn CHAR(13) NOT NULL,
    library_id INTEGER NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT 1,
    PRIMARY KEY (book_id),
    FOREIGN KEY (isbn) REFERENCES book_info(isbn),
    FOREIGN KEY (library_id) REFERENCES libraries(library_id)
    );

CREATE TABLE IF NOT EXISTS borrow_record (
    borrow_record_id INTEGER NOT NULL AUTO_INCREMENT,
    member_id INTEGER NOT NULL,
    book_id INTEGER NOT NULL,
    return_date DATE NOT NULL,
    is_returned BOOLEAN NOT NULL,
    PRIMARY KEY (borrow_record_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
    );
