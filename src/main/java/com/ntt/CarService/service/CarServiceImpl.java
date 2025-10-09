package com.ntt.CarService.service;

import com.ntt.CarService.model.Car;
import com.ntt.CarService.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service implementation for managing cars.
 * This class contains the business logic for car operations and interacts with the {@link CarRepository}.
 */
@Service
@Slf4j
public class CarServiceImpl implements CarService {
    @Autowired
    private CarRepository carRepository;

    /**
     * Retrieves a list of all cars.
     *
     * @return A list of all {@link Car} objects.
     */
    @Override
    public List<Car> getAllCars() {
        log.info("Retrieving all cars from the database");
        return carRepository.findAll();
    }

    /**
     * Creates a new car.
     *
     * @param car The {@link Car} object to be created.
     */
    @Override
    public void createCar(Car car) {
        log.info("Attempting to create a new car: {}", car);
        carRepository.save(car);
        log.info("Car created successfully with ID: {}", car.getCarId());
    }

    /**
     * Retrieves a car by its ID.
     *
     * @param id The ID of the car to retrieve.
     * @return The {@link Car} object if found, otherwise null.
     */
    @Override
    public Car getCarById(Long id) throws Exception {
        log.info("Attempting to retrieve car with ID: {}", id);
        Car car = carRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Car with ID {} not found", id);
                    return new Exception("Car with ID " + id + " not found");
                });
        log.info("Car with ID {} retrieved successfully: {}", id, car);
        return car;
    }

    /**
     * Updates an existing car's details.
     *
     * @param id         The ID of the car to update.
     * @param updatedCar The {@link Car} object containing updated details.
     * @throws Exception if the car with the specified ID is not found.
     */
    @Override
    public void updateCar(Long id, Car updatedCar) throws Exception {
        log.info("Attempting to update car with ID: {} and updatedCar: {}", id, updatedCar);
        Car existingCar = getCarById(id);
        existingCar.setNumberOfWheels(updatedCar.getNumberOfWheels());
        existingCar.setColor(updatedCar.getColor());
        existingCar.setBrand(updatedCar.getBrand());
        carRepository.save(existingCar);
        log.info("Car with ID {} updated successfully", id);
    }

    /**
     * Deletes a car by its ID.
     *
     * @param id The ID of the car to delete.
     * @throws Exception if the car with the specified ID is not found.
     */
    @Override
    public void deleteCarById(Long id) throws Exception {
        log.info("Attempting to delete car with ID: {}", id);
        if(!carRepository.existsById(id)) {
            log.warn("Car with ID {} not found for deletion", id);
            throw new Exception("Car with ID " + id + " not found");
        }
        carRepository.deleteById(id);
        log.info("Car with ID {} deleted successfully", id);
    }
}
