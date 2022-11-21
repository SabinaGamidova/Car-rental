CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS role(
	id UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
	name TEXT NOT NULL UNIQUE,
	description TEXT NOT NULL,
	status BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO role(name, description)
VALUES
('User', 'Regular customer'),
('Manager', 'Worker of car rental');

CREATE TABLE IF NOT EXISTS "user"(
	id UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
	name TEXT NOT NULL,
	surname TEXT NOT NULL,
	patronymic TEXT NOT NULL,
	date_of_birth TIMESTAMPTZ NOT NULL,
	email TEXT NOT NULL UNIQUE,
	password TEXT NOT NULL,
	role_id UUID NOT NULL,
	status BOOLEAN NOT NULL DEFAULT TRUE,
	CONSTRAINT role_id_fk FOREIGN KEY (role_id) REFERENCES role (id)
);


CREATE TABLE IF NOT EXISTS transmission_type(
	id UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
	name TEXT NOT NULL UNIQUE,
	status BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO transmission_type(name)
VALUES
('Automatic'),
('Mechanic');


CREATE TABLE IF NOT EXISTS fuel_type(
	id UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
	name TEXT NOT NULL UNIQUE,
	status BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO fuel_type(name)
VALUES
('Petrol'),
('Diesel'),
('Gas'),
('Electricity');


CREATE TABLE IF NOT EXISTS engine(
	id UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
	max_speed INTEGER NOT NULL,
	fuel_type_id UUID NOT NULL,
	transmission_type_id UUID NOT NULL,
	volume DOUBLE PRECISION NOT NULL,
	fuel_consumption DOUBLE PRECISION NOT NULL,
	status BOOLEAN NOT NULL DEFAULT TRUE,
	CONSTRAINT fuel_type_id_fk FOREIGN KEY (fuel_type_id) REFERENCES fuel_type (id),
	CONSTRAINT transmission_type_id_fk FOREIGN KEY (transmission_type_id) REFERENCES transmission_type (id)
);


CREATE TABLE IF NOT EXISTS car_type(
	id UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
	name TEXT NOT NULL,
	status BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO car_type(name)
VALUES
('Sedan'),
('Cabriolet'),
('Crossover');


CREATE TABLE IF NOT EXISTS car_comfort(
	id UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
	name TEXT NOT NULL,
	description TEXT NOT NULL,
	status BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO car_comfort(name, description)
VALUES
('Premium', 'High build quality, high price'),
('Standard', 'Regular, standard build quality, average price'),
('Economy', 'Low build quality, minimum price');


CREATE TABLE IF NOT EXISTS car(
	id UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
	number TEXT NOT NULL,
	brand TEXT NOT NULL,
	model TEXT NOT NULL,
	car_type_id UUID NOT NULL,
	car_comfort_id UUID NOT NULL,
	engine_id UUID NOT NULL,
	price DOUBLE PRECISION NOT NULL,
	deposit DOUBLE PRECISION NOT NULL,
	status BOOLEAN NOT NULL DEFAULT TRUE,
	CONSTRAINT car_type_id_fk FOREIGN KEY (car_type_id) REFERENCES car_type (id),
	CONSTRAINT car_comfort_id_fk FOREIGN KEY (car_comfort_id) REFERENCES car_comfort (id),
	CONSTRAINT engine_id_fk FOREIGN KEY (engine_id) REFERENCES engine (id)
);


CREATE TABLE IF NOT EXISTS "order"(
	id UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
	client_id UUID NOT NULL,
	car_id UUID NOT NULL,
	"from" DATE NOT NULL DEFAULT NOW(),
	"to" DATE NOT NULL DEFAULT NOW(),
	total_price DOUBLE PRECISION NOT NULL,
	status BOOLEAN NOT NULL DEFAULT TRUE,
	CONSTRAINT client_id_fk FOREIGN KEY (client_id) REFERENCES "user" (id),
	CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES car (id)
);

CREATE TABLE IF NOT EXISTS session(
	id UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
	client_id UUID NOT NULL,
	start TIMESTAMPTZ NOT NULL,
	finish TIMESTAMPTZ DEFAULT NULL,
	status BOOLEAN NOT NULL DEFAULT TRUE,
	CONSTRAINT client_id_fk FOREIGN KEY (client_id) REFERENCES "user" (id)
);