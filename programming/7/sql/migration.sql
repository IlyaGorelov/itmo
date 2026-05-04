CREATE SEQUENCE products_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE products (
    id BIGINT PRIMARY KEY DEFAULT nextval('products_id_seq'),

    name TEXT NOT NULL,

    x INTEGER NOT NULL,
    y DOUBLE PRECISION NOT NULL,

    creation_date DATE NOT NULL,

    price DOUBLE PRECISION,
    manufacture_cost INTEGER NOT NULL,

    unit_of_measure TEXT,

    owner_name TEXT,
    height REAL,

    eye_color TEXT,
    hair_color TEXT,
    nationality TEXT,

    loc_x DOUBLE PRECISION,
    loc_y INTEGER,
    loc_z DOUBLE PRECISION,
    loc_name TEXT
);

ALTER SEQUENCE products_id_seq OWNED BY products.id;