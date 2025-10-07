package com.ntt.CarService;


import com.ntt.CarService.model.Car;
import com.ntt.CarService.repository.CarRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CarRepository class.
 */
@ExtendWith(MockitoExtension.class)
class CarRepositoryTest {

    @InjectMocks
    private CarRepository carRepository;

    @Test
    @DisplayName("Should add a car and assign a unique ID")
    void testAddCar() {
        Car newCar = new Car(null, 4, "Green", "Toyota");
        assertEquals(0, carRepository.getCars().size(), "Repository should be empty initially.");

        carRepository.addCar(newCar);

        assertEquals(1, carRepository.getCars().size(), "Repository should contain one car after adding.");
        assertNotNull(newCar.getCarId(), "Car ID should not be null after being added.");
        assertEquals(1L, newCar.getCarId(), "The first car added should have an ID of 1.");
    }

    @Test
    @DisplayName("Should return the correct car when ID exists")
    void testGetCarById_whenCarExists() throws Exception {
        Car newCar = new Car(null, 4, "Red", "Honda");
        carRepository.addCar(newCar);

        Car retrievedCar = carRepository.getCarById(1L);

        assertNotNull(retrievedCar, "Retrieved car should not be null.");
        assertEquals(1L, retrievedCar.getCarId(), "The car ID should match.");
        assertEquals(newCar.getColor(), retrievedCar.getColor(), "The colors should match.");
        assertEquals(newCar.getBrand(), retrievedCar.getBrand(), "The makes should match.");
        assertEquals(newCar.getNumberOfWheels(), retrievedCar.getNumberOfWheels(), "The number of wheels should match.");

    }

    @Test
    @DisplayName("Should throw an exception when retrieving a non-existent car")
    void testGetCarById_whenCarDoesNotExist_shouldThrowException() throws Exception {
        assertThrows(Exception.class, () -> carRepository.getCarById(999L), "Expected an exception when retrieving a non-existent car ID.");
    }

    @Test
    @DisplayName("Should successfully update a car when ID exists")
    void testUpdateCar_Success() throws Exception {
        Car existingCar = new Car(1L, 4, "Red", "Honda");
        Car updatedCar = new Car(1L, 6, "Black", "BMW");

        carRepository.addCar(existingCar);
        carRepository.updateCar(1L, updatedCar);

        assertEquals("Black", existingCar.getColor());
        assertEquals("BMW", existingCar.getBrand());
        assertEquals(6, existingCar.getNumberOfWheels());
    }

    @Test
    @DisplayName("Should throw an exception when updating a non-existent car")
    void testUpdateCar_NotFound() throws Exception {
        Car updatedCar = new Car(1L, 4, "Black", "BMW");

        assertThrows(Exception.class, () -> {
            carRepository.updateCar(1L, updatedCar);
        });
    }

    @Test
    @DisplayName("Should successfully delete a car when ID exists")
    void testDeleteCarById_whenCarExists_shouldRemoveCar() throws Exception {
        Car newCar = new Car(null, 4, "Blue", "Ford");
        carRepository.addCar(newCar);

        carRepository.deleteCarById(1L);

        assertEquals(0, carRepository.getCars().size(), "Repository should be empty after deletion.");
        assertThrows(Exception.class, () -> carRepository.getCarById(1L));
    }

    @Test
    @DisplayName("Should throw an exception when deleting a non-existent car")
    void testDeleteCarById_whenCarDoesNotExist_shouldThrowException() {

        Exception exception = assertThrows(Exception.class, () -> {
            carRepository.deleteCarById(99L);
        });

        String expectedMessage = "Car with ID 99 not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "The exception message is not as expected.");
    }
}

