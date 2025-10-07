package com.ntt.CarService.service;

import com.ntt.CarService.model.Car;

import java.util.List;

public interface CarService {
    List<Car> getAllCars();

    void createCar(Car car);

    Car getCarById(Long id) throws Exception;

    void updateCar(Long id, Car updatedCar) throws Exception;

    void deleteCarById(Long id) throws Exception;
}
