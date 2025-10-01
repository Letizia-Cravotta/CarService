package com.ntt.CarService.controller;

import com.ntt.CarService.model.Car;
import com.ntt.CarService.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping("/car")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/car/{id}")
    public ResponseEntity<?> getCarById(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        if (car != null)
            return ResponseEntity.ok(car);
        return ResponseEntity.status(404).body("Car with ID " + id + " not found");

    }

    @PostMapping("/car")
    public ResponseEntity<?> createCar(@RequestBody Car car) {
        carService.createCar(car);
        return ResponseEntity.ok("Car created successfully");
    }

    @PutMapping("/car/{id}")
    public ResponseEntity<String> updateCar(@PathVariable Long id, @RequestBody Car updatedCar) {
        if (carService.updateCar(id, updatedCar))
            return ResponseEntity.ok("Car updated successfully");
        return ResponseEntity.ok("Car with ID " + id + " not found");
    }

    @DeleteMapping("/car/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
        if (carService.deleteCar(id))
            return ResponseEntity.ok("Car deleted successfully");
        return ResponseEntity.ok("Car with ID " + id + " not found");
    }

}