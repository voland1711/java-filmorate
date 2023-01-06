CREATE TABLE IF NOT EXISTS film_mpa
(
    film_mpa_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    mpa_name    varchar(5) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    user_id       INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_email    varchar(255) NOT NULL,
    user_login    varchar(255) NOT NULL,
    user_name     varchar(255),
    user_birthday date,
    CONSTRAINT constr_user CHECK (user_email LIKE '%@%' AND user_login NOT LIKE '% %' AND
                                  user_birthday <= CURRENT_TIMESTAMP())
);

CREATE TABLE IF NOT EXISTS genre
(
    genre_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name varchar(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS films
(
    film_id      INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name_films   varchar(50) NOT NULL,
    description  varchar(200),
    release_date date,
    duration     INTEGER,
    rate         INTEGER,
    film_mpa_id  INTEGER,
--     film_mpa_id  INTEGER REFERENCES film_mpa (film_mpa_id),
    CONSTRAINT constr_film CHECK (name_films <> '' AND duration > 0),
    CONSTRAINT constr_release_date CHECK (release_date > '1895-12-27')
);

CREATE TABLE IF NOT EXISTS film_like
(
    film_id INTEGER,
    user_id INTEGER,
    PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  INTEGER,
    genre_id INTEGER,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id               INTEGER,
    friend_user_id        INTEGER,
    friendships_status_id INTEGER,
    event_timestamp       date,
    PRIMARY KEY (user_id, friend_user_id)
);