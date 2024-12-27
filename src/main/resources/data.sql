INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'), -- 1
       ('Admin', 'admin@gmail.com', '{noop}admin'),  -- 2
       ('Guest', 'guest@gmail.com', '{noop}guest'); -- 3

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),  -- 1
       ('ADMIN', 2), -- 2
       ('USER', 2); -- 3

INSERT INTO RESTAURANT (NAME, EMAIL)
VALUES ('Ресторан Б', 'restaurant_b@yandex.ru'), -- 1
       ('Ресторан А', 'restaurant_a@yandex.ru'), -- 2
       ('Ресторан В', 'restaurant_c@yandex.ru'); -- 3

INSERT INTO MEAL (DESCRIPTION, PRICE, RESTAURANT_ID)
VALUES ('Обед В-1', 310, 3), -- 1
       ('Обед Б-1', 110, 1), -- 2
       ('Обед А-2', 220, 2), -- 3
       ('Обед А-1', 210, 2), -- 4
       ('Обед Б-2', 120, 1), -- 5
       ('Обед В-2', 320, 3), -- 6
       ('Обед А-3', 230, 2), -- 7
       ('Обед Б-3', 130, 1); -- 8
