package com.ntt.CarService.controller;

import com.ntt.CarService.model.Car;
import com.ntt.CarService.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing cars.
 * Provides endpoints for creating, retrieving, updating, and deleting cars.
 */
@RestController
public class CarController {

    @Autowired
    private CarService carService;

    /**
     * Retrieves a list of all cars.
     *
     * @return A list of {@link Car} objects.
     */
    @GetMapping("/car")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    /**
     * Retrieves a specific car by its ID.
     *
     * @param id The ID of the car to retrieve.
     * @return A {@link ResponseEntity} containing the {@link Car} if found (HTTP 200 OK),
     * or an error message if not found (HTTP 404 Not Found).
     */
    @GetMapping("/car/id/{id}")
    public ResponseEntity<?> getCarById(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        if (car != null)
            return ResponseEntity.ok(car);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car with ID " + id + " not found");

    }

    /**
     * Creates a new car.
     *
     * @param car The {@link Car} object to be created, passed in the request body.
     * @return A {@link ResponseEntity} with a success message and HTTP status 201 Created.
     */
    @PostMapping("/car")
    public ResponseEntity<?> createCar(@RequestBody Car car) {
        carService.createCar(car);
        return ResponseEntity.status(HttpStatus.CREATED).body("Car created successfully");
    }

    /**
     * Updates an existing car identified by its ID.
     *
     * @param id         The ID of the car to update.
     * @param updatedCar The {@link Car} object containing the updated information, passed in the request body.
     * @return A {@link ResponseEntity} with a success message (HTTP 200 OK),
     * or an error message if the car to update is not found (HTTP 404 Not Found).
     */
    @PutMapping("/car/id/{id}")
    public ResponseEntity<String> updateCar(@PathVariable Long id, @RequestBody Car updatedCar) {
        try {
            carService.updateCar(id, updatedCar);
            return ResponseEntity.ok("Car updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    /**
     * Deletes a car by its ID.
     *
     * @param id The ID of the car to delete.
     * @return A {@link ResponseEntity} with a success message (HTTP 200 OK),
     * or an error message if the car to delete is not found (HTTP 404 Not Found).
     */
    @DeleteMapping("/car/id/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
        try {
            carService.deleteCarById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok("Car deleted successfully");
    }

}