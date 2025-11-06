CREATE TABLE cars (
    car_id BIGSERIAL PRIMARY KEY,

    number_of_wheels INT NOT NULL,

    color VARCHAR(255) NOT NULL,

    brand VARCHAR(255) NOT NULL
);