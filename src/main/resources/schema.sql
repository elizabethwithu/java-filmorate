create table if not exists GENRE
(
    GENRE_ID   INTEGER,
    GENRE_NAME CHARACTER VARYING,
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create table if not exists MPA_RATING
(
    RATING_ID   INTEGER auto_increment,
    RATING_NAME CHARACTER VARYING,
    constraint MPA_RATING_PK
        primary key (RATING_ID)
);

create table if not exists FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    CHARACTER LARGE OBJECT,
    DESCRIPTION  CHARACTER LARGE OBJECT,
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    RATING_ID    INTEGER,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_MPA_RATING_RATING_ID_FK
        foreign key (RATING_ID) references MPA_RATING
            on update cascade on delete cascade
);

create table if not exists FILM_GENRE
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRE_PK
        primary key (FILM_ID, GENRE_ID),
    constraint FILM_GENRE_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint FILM_GENRE_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
            on delete cascade
);

create table if not exists USERS
(
    USER_ID   INTEGER auto_increment,
    LOGIN     CHARACTER VARYING,
    USER_NAME CHARACTER VARYING,
    EMAIL     CHARACTER VARYING,
    BIRTHDAY  DATE,
    constraint "USERS_pk"
        primary key (USER_ID)
);

create table if not exists FAVORITE_FILMS
(
    USER_ID INTEGER not null,
    FILM_ID INTEGER not null,
    constraint FAVORITE_FILMS_PK
        primary key (USER_ID, FILM_ID),
    constraint FAVORITE_FILMS_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint FAVORITE_FILMS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade
);

create table if not exists FRIENDS
(
    USER_SENDER_ID INTEGER not null,
    FRIEND_ID      INTEGER not null,
    STATUS         BOOLEAN,
    constraint FRIENDS_PK
        primary key (USER_SENDER_ID, FRIEND_ID),
    constraint FRIENDS_USERS_USER_ID_FK
        foreign key (USER_SENDER_ID) references USERS
            on delete cascade,
    constraint FRIENDS_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
            on delete cascade
);