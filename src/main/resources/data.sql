MERGE INTO film_mpa KEY (film_mpa_id)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

MERGE INTO genre KEY (genre_id)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

-- MERGE INTO films KEY (film_id)
--     VALUES (1, 'Любовь и голуби', 'Фильм про любовь и голубей', '2021-12-12', 120, 3, 3),
--            (2, 'Властелин колец', 'Про хоббитов', '2021-12-12', 210, 3, 1);