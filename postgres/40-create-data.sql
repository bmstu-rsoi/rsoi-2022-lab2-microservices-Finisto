\c libraries
insert into library (id, library_uid, name, city, address)
values (1, '83575e12-7ce0-48ee-9931-51919ff3c9ee',
        'Библиотека имени 7 Непьющих',
        'Москва',
        '2-я Бауманская ул., д.5, стр.1');

insert into books (id, book_uid, name, author, genre, condition)
values (1, 'f7cdc58f-2caf-4b15-9727-f89dcc629b27',
        'Краткий курс C++ в 7 томах',
        'Бьерн Страуструп',
        'Научная фантастика',
        'EXCELLENT');

insert into library_books (book_id, library_id, available_count)
values (1, 1, 1);

\c ratings
insert into rating (id, username, stars)
values (1, 'Test Max', 75)
