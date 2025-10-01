package com.ntt.CarService.service;

import com.ntt.CarService.model.Car;
import com.ntt.CarService.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    private CarRepository carRepository;
    private Long nextId = 1L;

    @Override
    public List<Car> getAllCars() {
        return carRepository.cars;
    }

    @Override
    public void createCar(Car car) {
        car.setCarId(nextId++);
        carRepository.cars.add(car);
    }

    @Override
    public Car getCarById(Long id) {
        for (Car car : carRepository.cars) {
            if (car.getCarId().equals(id)) {
                return car;
            }
        }
        return null;
    }

    @Override
    public void updateCar(Long id, Car updatedCar) {
        Car existingCar = getCarById(id);
        if (existingCar != null) {
            existingCar.setNumberOfWheels(updatedCar.getNumberOfWheels());
            existingCar.setColor(updatedCar.getColor());
            existingCar.setBrand(updatedCar.getBrand());
        }
    }

    @Override
    public void deleteCar(Long id) {
        Car carToDelete = getCarById(id);
        if (carToDelete != null) {
            carRepository.cars.remove(carToDelete);
        }
    }
}
