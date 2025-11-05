CREATE TABLE cars (
    -- Spring Boot converts 'carId' to 'car_id'
    -- GenerationType.IDENTITY maps to BIGSERIAL for a Long in Postgres
    car_id BIGSERIAL PRIMARY KEY,

    -- 'numberOfWheels' becomes 'number_of_wheels'
    number_of_wheels INT NOT NULL,

    -- @NotBlank becomes NOT NULL
    color VARCHAR(255) NOT NULL,

    -- @NotBlank becomes NOT NULL
    brand VARCHAR(255) NOT NULL
);