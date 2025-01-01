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

INSERT INTO MENU (ISSUED_DATE, RESTAURANT_ID)
VALUES ('2024-12-01', 3), -- 1
       ('2024-12-01', 1), -- 2
       ('2024-12-01', 2), -- 3

       ('2024-12-02', 1), -- 4
       ('2024-12-02', 3), -- 5
       ('2024-12-02', 2), -- 6

       (CURRENT_DATE, 1),          -- 7
       (CURRENT_DATE, 2); -- 8

INSERT INTO MENU_ITEM (MENU_ID, MEAL_ID)
VALUES (1, 1), -- 1    Обед В-1
       (2, 8), -- 2    Обед Б-3
       (2, 2), -- 3    Обед Б-1
       (2, 5), -- 4    Обед Б-2
       (3, 3), -- 5    Обед А-2
       (3, 4), -- 6    Обед А-1

       (4, 5), -- 7    Обед Б-2
       (4, 2), -- 8    Обед Б-1
       (5, 6), -- 9    Обед В-2
       (6, 7), -- 10   Обед А-3
       (6, 4), -- 11   Обед А-1

       (7, 5), -- 12   Обед Б-2
       (7, 2), -- 13   Обед Б-1
       (7, 8), -- 14   Обед Б-3
       (8, 7), -- 15   Обед А-3
       (8, 4); -- 16   Обед А-1

INSERT INTO VOTE (VOTED_DATE, VOTED_TIME, RESTAURANT_ID, USER_ID)
VALUES ('2024-12-01', '09:00:00', 1, 2), -- 1
       ('2024-12-01', '10:00:00', 1, 1), -- 2

       ('2024-12-02', '10:00:00', 3, 2), -- 3
       ('2024-12-02', '10:00:00', 3, 1), -- 4

       (CURRENT_DATE, '09:00:00', 1, 1), -- 5
       (CURRENT_DATE, '10:00:00', 2, 2); -- 6
