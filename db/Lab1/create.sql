-- creating domains
CREATE DOMAIN scientist_age AS INTEGER
CHECK(
	VALUE > 18
	AND VALUE < 100
);

-- creating enums
CREATE TYPE gender_enum AS ENUM ('Man', 'Female');
CREATE TYPE education_level_enum AS ENUM ('Bachelor', 'Master', 'Doctor');
CREATE TYPE bone_condition_enum AS ENUM ('Bad', 'Good', 'Excellent');

-- creating tables
CREATE TABLE place (
	id SERIAL PRIMARY KEY,
	coordinates POINT NOT NULL,
	country TEXT NOT NULL
);

CREATE TABLE bone_type (
	id SERIAL PRIMARY KEY,
	bone_type TEXT NOT NULL
);

CREATE TABLE education (
	id SERIAL PRIMARY KEY,
	education_level education_level_enum NOT NULL,
	receiving_date DATE NOT NULL,
	receiving_place TEXT NOT NULL
);

CREATE TABLE animal (
	id SERIAL PRIMARY KEY,
	name TEXT NOT NULL
);

CREATE TABLE reptile (
	id SERIAL PRIMARY KEY,
	name TEXT NOT NULL,
	successor_id INTEGER REFERENCES animal(id) NOT  NULL
);

CREATE TABLE bone (
	id SERIAL PRIMARY KEY,
	bone_type_id INTEGER REFERENCES bone_type(id) NOT  NULL,
	reptile_id INTEGER REFERENCES reptile(id) NOT  NULL,
	condition bone_condition_enum NOT NULL
);

CREATE TABLE scientist (
	id SERIAL PRIMARY KEY,
	name TEXT NOT NULL,
	age scientist_age NOT NULL,
	education_id INTEGER REFERENCES education(id) NOT NULL,
	gender gender_enum NOT NULL,
	UNIQUE(education_id)
);

CREATE TABLE research (
	id SERIAL PRIMARY KEY,
	finded_bone_id INTEGER REFERENCES bone(id) NOT NULL
);

CREATE TABLE trip (
	id SERIAL PRIMARY KEY,
	departure_date DATE NOT NULL,
	place_id INTEGER REFERENCES place(id) NOT NULL,
	scientist_id INTEGER REFERENCES scientist(id) NOT NULL,
	research_id INTEGER REFERENCES research(id) NOT NULL
);