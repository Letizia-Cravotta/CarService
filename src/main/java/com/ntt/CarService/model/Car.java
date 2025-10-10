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

    @Min(value = 3, message = "Number of wheels must be at least 3")
    @Max(value = 6, message = "Number of wheels must be at most 6")

    private int numberOfWheels;

    @Pattern(regexp = "^[A-Za-z ]+$", message = "Color must be valid")
    @NotBlank
    private String color;

    @Pattern(regexp = "^[A-Za-z\\- ]+$", message = "Brand must contain only letters, spaces, or hyphens")
    @NotBlank
    private String brand;


}
