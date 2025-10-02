package com.ntt.CarService.repository;

import com.ntt.CarService.model.Car;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing {@link Car} entities in an in-memory list.
 * This class handles the storage, retrieval, and deletion of cars.
 */
@Repository
public class CarRepository {
    @Getter
    private List<Car> cars = new ArrayList<>();
    private Long nextId = 1L;

    /**
     * Retrieves a car by its ID.
     *
     * @param id The ID of the car to retrieve.
     * @return The {@link Car} object if found, otherwise null.
     */
    public Car getCarById(Long id) {
        for (Car car : cars) {
            if (car.getCarId().equals(id)) {
                return car;
            }
        }
        return null;
    }

    /**
     * Adds a new car to the repository and assigns it a unique ID.
     *
     * @param car The {@link Car} object to add.
     */
    public void addCar(Car car) {
        car.setCarId(nextId++);
        cars.add(car);
    }

    /**
     * Deletes a car by its ID.
     *
     * @param id The ID of the car to delete.
     * @throws Exception if the car with the specified ID is not found.
     */
    public void deleteCarById(long id) throws Exception {
        Car carToDelete = getCarById(id);
        if (carToDelete == null) {
            throw new Exception("Car with ID " + id + " not found");
        }
        getCars().remove(carToDelete);

    }

}
