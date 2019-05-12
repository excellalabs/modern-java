CREATE TABLE BOOK (
  id SERIAL PRIMARY KEY,
  title VARCHAR(50) NOT NULL,
  author VARCHAR(50) NOT NULL,
  year_published INTEGER NOT NULL
);

INSERT INTO BOOK(title, author, year_published) VALUES ('In Search of Lost Time', 'Marcel Proust', 1913);
INSERT INTO BOOK(title, author, year_published) VALUES ('Ulysses', 'James Joyce', 1904);
INSERT INTO BOOK(title, author, year_published) VALUES ('The Great Gatsby', 'F. Scott Fitzgerald', 1925);
INSERT INTO BOOK(title, author, year_published) VALUES ('Hamlet', 'William Shakespeare', 1601);
INSERT INTO BOOK(title, author, year_published) VALUES ('War and Peace', 'Leo Tolstoy', 1869);
INSERT INTO BOOK(title, author, year_published) VALUES ('One Hundred Years of Solitude', 'Gabrielle Marquez', 1969);
INSERT INTO BOOK(title, author, year_published) VALUES ('The Brothers Karmazov', 'Fyodor Dostoyevsky', 1880);
INSERT INTO BOOK(title, author, year_published) VALUES ('Adventures of Huckleberry Finn', 'Mark Twain', 1885);
INSERT INTO BOOK(title, author, year_published) VALUES ('Lolita', 'Vladamir Nabakov', 1955);
INSERT INTO BOOK(title, author, year_published) VALUES ('Alices Adventures in Wonderland', 'Lewis Carrol', 1869);
INSERT INTO BOOK(title, author, year_published) VALUES ('Pride and Prejudice', 'Jane Austin', 1813);
INSERT INTO BOOK(title, author, year_published) VALUES ('The Catcher in the Rye', 'J.D. Salinger', 1945);
INSERT INTO BOOK(title, author, year_published) VALUES ('The Sound and the Fury', 'William Faulkner', 1929);
INSERT INTO BOOK(title, author, year_published) VALUES ('Nineteen Eighty-Four', 'George Orwell', 1949);
INSERT INTO BOOK(title, author, year_published) VALUES ('Gullivers Travels', 'Jonathan Swift', 1726);
INSERT INTO BOOK(title, author, year_published) VALUES ('Catch-22', 'Joseph Heller', 1961);
INSERT INTO BOOK(title, author, year_published) VALUES ('The Grapes of Wrath', 'John Steinbeck', 1939);
INSERT INTO BOOK(title, author, year_published) VALUES ('Jane Eyre', 'Charlotte Bronte', 1847);


