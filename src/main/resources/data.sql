delete from GENRE;
delete from MPA_RATING;
delete from FILMS;
alter table FILMS alter column FILM_ID restart with 1;
delete from FILM_GENRE;
delete from USERS;
alter table USERS alter column USER_ID restart with 1;
delete from FRIENDS;
delete from FAVORITE_FILMS;

insert into GENRE values (1, 'Комедия'),
                         (2, 'Драма'),
                         (3, 'Мультфильм'),
                         (4, 'Триллер'),
                         (5, 'Документальный'),
                         (6, 'Боевик');

insert into MPA_RATING values (1, 'G');
insert into MPA_RATING values (2, 'PG');
insert into MPA_RATING values (3, 'PG-13');
insert into MPA_RATING values (4, 'R');
insert into MPA_RATING values (5, 'NC-17');

