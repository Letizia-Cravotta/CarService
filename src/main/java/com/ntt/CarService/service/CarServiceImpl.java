package com.ntt.CarService.service;

import com.ntt.CarService.model.Car;
import com.ntt.CarService.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service implementation for managing cars.
 * This class contains the business logic for car operations and interacts with the {@link CarRepository}.
 */
@Service
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
        return carRepository.findAll();
    }

    /**
     * Creates a new car.
     *
     * @param car The {@link Car} object to be created.
     */
    @Override
    public void createCar(Car car) {
        carRepository.save(car);
    }

    /**
     * Retrieves a car by its ID.
     *
     * @param id The ID of the car to retrieve.
     * @return The {@link Car} object if found, otherwise null.
     */
    @Override
    public Car getCarById(Long id) throws Exception {
        return carRepository.findById(id)
                .orElseThrow(() -> new Exception("Car with ID " + id + " not found"));
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
        Car existingCar = getCarById(id);
        existingCar.setNumberOfWheels(updatedCar.getNumberOfWheels());
        existingCar.setColor(updatedCar.getColor());
        existingCar.setBrand(updatedCar.getBrand());
        carRepository.save(existingCar);
    }

    /**
     * Deletes a car by its ID.
     *
     * @param id The ID of the car to delete.
     * @throws Exception if the car with the specified ID is not found.
     */
    @Override
    public void deleteCarById(Long id) throws Exception {
        if(!carRepository.existsById(id))
            throw new Exception("Car with ID " + id + " not found");
        carRepository.deleteById(id);
    }
}
