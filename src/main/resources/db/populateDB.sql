DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

insert into meals(user_id, dateTime,description,calories)
values ((select id
        from users
        where name = 'User'), to_timestamp('2021-02-10 13:00:00','yyyy-mm-dd hh24:mi:ss'),'Обед',500);

insert into meals(user_id, dateTime,description,calories)
values ((select id
        from users
        where name = 'User'), to_timestamp('2021-02-11 19:00:00','yyyy-mm-dd hh24:mi:ss'),'Ужин',1200);

insert into meals(user_id, dateTime,description,calories)
values ((select id
        from users
        where name = 'User'), to_timestamp('2021-02-11 12:45:00','yyyy-mm-dd hh24:mi:ss'),'Обед',800);

insert into meals(user_id, dateTime,description,calories)
values ((select id
        from users
        where name = 'Admin'), to_timestamp('2021-02-11 10:00:00','yyyy-mm-dd hh24:mi:ss'),'Завтрак',300);

insert into meals(user_id, dateTime,description,calories)
values ((select id
        from users
        where name = 'Admin'), to_timestamp('2021-02-12 13:30:00','yyyy-mm-dd hh24:mi:ss'),'Обед',2300);


