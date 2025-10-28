package com.ntt.CarService.controller;

import com.ntt.CarService.model.Car;
import com.ntt.CarService.service.CarService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
@CrossOrigin(origins = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Slf4j
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
        log.info("GET /car - Retrieving all cars");
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
    public ResponseEntity<?> getCarById(@PathVariable Long id) throws Exception {
        log.info("GET /car/id/{} - Retrieving car by ID", id);
        try {
            Car car = carService.getCarById(id);
            return ResponseEntity.ok(car);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Creates a new car.
     *
     * @param car The {@link Car} object to be created, passed in the request body.
     * @return A {@link ResponseEntity} with a success message and HTTP status 201 Created.
     */
    @PostMapping("/car")
    public ResponseEntity<Car> createCar(@Valid @RequestBody Car car) {
        log.info("POST /car - Creating a new car");
        Car createdCar = carService.createCar(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
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
    public ResponseEntity<?> updateCar(@PathVariable Long id, @Valid @RequestBody Car updatedCar) {
        log.info("PUT /car/id/{} - Updating car", id);
        try {
            Car savedCar = carService.updateCar(id, updatedCar);
            return ResponseEntity.ok(savedCar);
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
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        log.info("DELETE /car/id/{} - Deleting car", id);
        try {
            Car deletedCar = carService.deleteCarById(id);
            return ResponseEntity.ok(deletedCar);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}