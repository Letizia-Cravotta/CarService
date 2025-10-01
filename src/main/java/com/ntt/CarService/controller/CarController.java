package com.ntt.CarService.controller;

import com.ntt.CarService.model.Car;
import com.ntt.CarService.repository.CarRepository;
import com.ntt.CarService.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Car getCarById(@PathVariable Long id){
        return carService.getCarById(id);
    }

    @PostMapping("/car")
    public String createCar(@RequestBody Car car) {
        carService.createCar(car);
        return "Car created successfully";
    }

}
