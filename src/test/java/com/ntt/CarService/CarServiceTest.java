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
import static org.mockito.Mockito.*;

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
        assertEquals("Toyota", actualCars.getFirst().getBrand());
    }

    @Test
    void testGetCarById_Found() throws Exception {
        Car car = new Car(1L, 4, "Blue", "Ford");
        when(carRepository.getCarById(1L)).thenReturn(car);

        Car result = carService.getCarById(1L);

        assertNotNull(result);
        assertEquals("Ford", result.getBrand());
    }

    @Test
    void testGetCarById_NotFound() throws Exception {
        when(carRepository.getCarById(1L)).thenThrow(new Exception("Car with ID 1 not found"));

        assertThrows(Exception.class, () -> carService.getCarById(1L));
    }

    @Test
    void testCreateCar() {
        Car car = new Car(null, 2, "Yellow", "Fiat");
        doNothing().when(carRepository).addCar(car);

        carService.createCar(car);

        verify(carRepository, times(1)).addCar(car);
    }

    @Test
    void testUpdateCar_Success() throws Exception {
        Car updatedCar = new Car(1L, 4, "Black", "BMW");
        doNothing().when(carRepository).updateCar(1L, updatedCar);

        carService.updateCar(1L, updatedCar);

        verify(carRepository, times(1)).updateCar(1L, updatedCar);
    }

    @Test
    void testUpdateCar_NotFound() throws Exception {
        Car updatedCar = new Car(1L, 4, "Black", "BMW");
        doThrow(new Exception("Car with ID 1 not found")).when(carRepository).updateCar(1L, updatedCar);

        Exception exception = assertThrows(Exception.class, () -> carService.updateCar(1L, updatedCar));
        assertEquals("Car with ID 1 not found", exception.getMessage());
    }

    @Test
    void testDeleteCarById_Success() throws Exception {
        doNothing().when(carRepository).deleteCarById(1L);

        carService.deleteCarById(1L);

        verify(carRepository, times(1)).deleteCarById(1L);
    }

    @Test
    void testDeleteCarById_NotFound() throws Exception {
        doThrow(new Exception("Car with ID 1 not found")).when(carRepository).deleteCarById(1L);

        Exception exception = assertThrows(Exception.class, () -> carService.deleteCarById(1L));
        assertEquals("Car with ID 1 not found", exception.getMessage());
    }

}