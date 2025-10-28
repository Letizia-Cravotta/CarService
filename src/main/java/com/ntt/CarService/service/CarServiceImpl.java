package com.ntt.CarService.service;

import com.ntt.CarService.exceptions.APIException;
import com.ntt.CarService.exceptions.ResourceNotFoundException;
import com.ntt.CarService.model.Car;
import com.ntt.CarService.repository.CarRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
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

    @Autowired
    private MeterRegistry meterRegistry;

    @PostConstruct
    public void init() {
        Gauge.builder("cars_database_total", carRepository, CarRepository::count)
                .description("Total number of cars in the database")
                .register(meterRegistry);
    }

    /**
     * Retrieves a list of all cars.
     *
     * @return A list of all {@link Car} objects.
     */
    @Override
    public List<Car> getAllCars() {
        List<Car> cars = carRepository.findAll();
        if (cars.isEmpty()) {
            throw new APIException("No cars found");
        }
        return cars;
    }

    /**
     * Creates a new car.
     *
     * @param car The {@link Car} object to be created.
     */
    @Override
    public Car createCar(Car car) {
        Car savedCar = carRepository.save(car);
        log.info("Car created successfully with ID: {}", savedCar.getCarId());
        return savedCar;
    }

    /**
     * Retrieves a car by its ID.
     *
     * @param id The ID of the car to retrieve.
     * @return The {@link Car} object if found, otherwise null.
     */
    @Override
    public Car getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Car with ID {} not found", id);
                    return new ResourceNotFoundException("Car", "carId", id);
                });
        log.info("Car with ID {} retrieved successfully", id);
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
    public Car updateCar(Long id, Car updatedCar) {
        Car existingCar = getCarById(id);
        existingCar.setNumberOfWheels(updatedCar.getNumberOfWheels());
        existingCar.setColor(updatedCar.getColor());
        existingCar.setBrand(updatedCar.getBrand());
        Car savedCar = carRepository.save(existingCar);
        log.info("Car with ID {} updated successfully", id);
        return savedCar;
    }

    /**
     * Deletes a car by its ID.
     *
     * @param id The ID of the car to delete.
     * @throws Exception if the car with the specified ID is not found.
     */
    @Override
    public Car deleteCarById(Long id) {
        if (!carRepository.existsById(id)) {
            log.warn("Car with ID {} not found for deletion", id);
            throw new ResourceNotFoundException("Car", "carId", id);
        }
        Car deletedCar = getCarById(id);
        carRepository.deleteById(id);
        log.info("Car with ID {} deleted successfully", id);
        return deletedCar;
    }
}
