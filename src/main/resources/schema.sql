DROP TABLE IF EXISTS mobile_users;
DROP TABLE IF EXISTS login_users;


CREATE TABLE login_users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(256) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    last_login TIMESTAMP DEFAULT NULL
);