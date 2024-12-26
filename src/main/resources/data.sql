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
