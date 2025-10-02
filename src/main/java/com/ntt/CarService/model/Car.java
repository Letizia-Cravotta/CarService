package com.ntt.CarService.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a car with its properties.
 */
@Getter
@Setter
@AllArgsConstructor
public class Car {
    private Long carId;
    private int numberOfWheels;
    private String color;
    private String brand;

}
