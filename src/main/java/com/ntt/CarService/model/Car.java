package com.ntt.CarService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Represents a car with its properties.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;


    private int numberOfWheels;

    @Pattern(regexp = "^[A-Za-z ]+$", message = "Color must be valid")
    @NotBlank
    private String color;

    @Pattern(regexp = "^[A-Za-z\\- ]+$", message = "Brand must contain only letters, spaces, or hyphens")
    @NotBlank
    private String brand;


    @AssertTrue(message = "Number of wheels must be 3, 4, or 6")
    public boolean isValidNumberOfWheels() {
        return numberOfWheels == 3 || numberOfWheels == 4 || numberOfWheels == 6 || numberOfWheels == 8;
    }
}
