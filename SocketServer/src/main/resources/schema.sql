CREATE SCHEMA chat_users;
CREATE TABLE chat_users.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(100)
);