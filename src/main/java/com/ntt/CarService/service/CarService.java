package com.ntt.CarService.service;

import com.ntt.CarService.model.Car;

import java.util.List;

public interface CarService {
    List<Car> getAllCars();

    Car createCar(Car car);

    Car getCarById(Long id) throws Exception;

    Car updateCar(Long id, Car updatedCar) throws Exception;

    Car deleteCarById(Long id) throws Exception;
}
