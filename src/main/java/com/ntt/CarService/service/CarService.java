package com.ntt.CarService.service;

import com.ntt.CarService.model.Car;

import java.util.List;

public interface CarService {
    List<Car> getAllCars();

    void createCar(Car car);

    Car getCarById(Long id);

    boolean updateCar(Long id, Car updatedCar);

    boolean deleteCar(Long id);
}
