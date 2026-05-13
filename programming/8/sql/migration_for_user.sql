
CREATE TABLE users (
    id serial PRIMARY KEY,

    login TEXT NOT NULL UNIQUE,
    password text not null,
    salt text not null
);
