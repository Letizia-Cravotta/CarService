package com.ntt.CarService;


import com.ntt.CarService.model.Car;
import com.ntt.CarService.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CarRepository class.
 */
class CarRepositoryTest {

    private CarRepository carRepository;

    /**
     * This method runs before each test. It ensures that we start
     * with a fresh, empty repository for every test case, preventing
     * tests from interfering with each other.
     */
    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
    }

    @Test
    @DisplayName("Should add a car and assign a unique ID")
    void testAddCar() {
        // Arrange: Create a new car without an ID
        Car newCar = new Car(null, 4, "Green", "Toyota");
        assertEquals(0, carRepository.getCars().size(), "Repository should be empty initially.");

        // Act: Add the car to the repository
        carRepository.addCar(newCar);

        // Assert: Verify the car was added and an ID was assigned
        assertEquals(1, carRepository.getCars().size(), "Repository should contain one car after adding.");
        assertNotNull(newCar.getCarId(), "Car ID should not be null after being added.");
        assertEquals(1L, newCar.getCarId(), "The first car added should have an ID of 1.");
    }

    @Test
    @DisplayName("Should return the correct car when ID exists")
    void testGetCarById() throws Exception {
        // Arrange: Add a car to the repository first
        Car newCar = new Car(null, 4, "Red", "Honda");
        carRepository.addCar(newCar);

        // Act: Try to retrieve the car using its assigned ID
        Car retrievedCar = carRepository.getCarById(1L);

        // Assert: Check that the correct car was returned
        assertNotNull(retrievedCar, "Retrieved car should not be null.");
        assertEquals(1L, retrievedCar.getCarId(), "The car ID should match.");
        assertEquals(newCar.getColor(), retrievedCar.getColor(), "The colors should match.");
        assertEquals(newCar.getBrand(), retrievedCar.getBrand(), "The makes should match.");
        assertEquals(newCar.getNumberOfWheels(), retrievedCar.getNumberOfWheels(), "The number of wheels should match.");

    }

    @Test
    @DisplayName("Should return null when car ID does not exist")
    void testGetCarById_whenCarDoesNotExist_shouldReturnNull() throws Exception {
        // Arrange: The repository is empty (thanks to @BeforeEach)

        // Act: Try to retrieve a car with an ID that doesn't exist
        Car retrievedCar = carRepository.getCarById(999L);

        // Assert: The result should be null
        assertNull(retrievedCar, "Retrieved car should be null for a non-existent ID.");

    }

    @Test
    @DisplayName("Should successfully delete a car when ID exists")
    void testDeleteCarById_whenCarExists_shouldRemoveCar() throws Exception {
        // Arrange: Add a car to the repository
        Car newCar = new Car(null, 4, "Blue", "Ford");
        carRepository.addCar(newCar);

        // Act: Delete the car
        carRepository.deleteCarById(1L);

        // Assert: The car should no longer be in the repository
        assertEquals(0, carRepository.getCars().size(), "Repository should be empty after deletion.");
        assertThrows(Exception.class, () -> carRepository.getCarById(1L));
    }

    @Test
    @DisplayName("Should throw an exception when deleting a non-existent car")
    void testDeleteCarById_whenCarDoesNotExist_shouldThrowException() {
        // Arrange: The repository is empty.

        // Act & Assert: Verify that calling deleteCarById throws the expected exception
        Exception exception = assertThrows(Exception.class, () -> {
            carRepository.deleteCarById(99L);
        });

        // Optionally, you can also check the exception message
        String expectedMessage = "Car with ID 99 not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "The exception message is not as expected.");
    }
}

