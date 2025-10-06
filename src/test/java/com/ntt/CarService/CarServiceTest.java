package com.ntt.CarService;

import com.ntt.CarService.model.Car;
import com.ntt.CarService.repository.CarRepository;
import com.ntt.CarService.service.CarServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

// 1. Enable Mockito extension for JUnit 5
@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    // 2. Create a mock of the dependency
    @Mock
    private CarRepository carRepository;

    // 3. Create an instance of the class to test and inject the mocks into it
    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void testGetAllCars() {
        // Arrange: Define what the mock repository should do
        Car car1 = new Car(1L, 4, "Green", "Toyota");
        Car car2 = new Car(2L, 4, "Red", "Honda");
        List<Car> expectedCars = List.of(car1, car2);

        // Tell the mock: "When getCars() is called, return our predefined list"
        when(carRepository.getCars()).thenReturn(expectedCars);

        // Act: Call the method we are testing
        List<Car> actualCars = carService.getAllCars();

        // Assert: Check if the result is what we expect
        assertNotNull(actualCars);
        assertEquals(2, actualCars.size());
        assertEquals("Toyota", actualCars.get(0).getBrand());
    }
}