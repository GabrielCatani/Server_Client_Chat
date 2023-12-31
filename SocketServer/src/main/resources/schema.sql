CREATE SCHEMA IF NOT EXISTS chat_users;
CREATE TABLE IF NOT EXISTS chat_users.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(100)
);
CREATE TABLE IF NOT EXISTS chat_users.messages (
   id SERIAL PRIMARY KEY,
   sender VARCHAR(100),
   message VARCHAR(250),
   timestamp TIMESTAMP
)