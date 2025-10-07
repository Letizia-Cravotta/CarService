package com.ntt.CarService;

import com.ntt.CarService.model.Car;
import com.ntt.CarService.repository.CarRepository;
import com.ntt.CarService.service.CarServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the CarService class.
 */
@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    @DisplayName("Should return all cars")
    void testGetAllCars() {
        Car car1 = new Car(1L, 4, "Green", "Toyota");
        Car car2 = new Car(2L, 4, "Red", "Honda");
        List<Car> expectedCars = List.of(car1, car2);

        when(carRepository.getCars()).thenReturn(expectedCars);

        List<Car> actualCars = carService.getAllCars();

        assertNotNull(actualCars);
        assertEquals(2, actualCars.size());
        assertEquals("Toyota", actualCars.getFirst().getBrand());
    }

    @Test
    @DisplayName("Should return empty list when no cars are found")
    void testGetAllCars_Empty() {
        when(carRepository.getCars()).thenReturn(List.of());

        List<Car> actualCars = carService.getAllCars();

        assertNotNull(actualCars);
        assertTrue(actualCars.isEmpty());
    }

    @Test
    @DisplayName("Should return car by ID when found")
    void testGetCarById_Found() throws Exception {
        Car car = new Car(1L, 4, "Blue", "Ford");
        when(carRepository.getCarById(1L)).thenReturn(car);

        Car result = carService.getCarById(1L);

        assertNotNull(result);
        assertEquals("Ford", result.getBrand());
    }

    @Test
    @DisplayName("Should throw exception when car by ID is not found")
    void testGetCarById_NotFound() throws Exception {
        when(carRepository.getCarById(1L)).thenThrow(new Exception("Car with ID 1 not found"));

        assertThrows(Exception.class, () -> carService.getCarById(1L));
    }

    @Test
    @DisplayName("Should create a new car")
    void testCreateCar() {
        Car car = new Car(null, 2, "Yellow", "Fiat");
        doNothing().when(carRepository).addCar(car);

        carService.createCar(car);

        verify(carRepository, times(1)).addCar(car);
    }

    @Test
    @DisplayName("Should update car when ID exists")
    void testUpdateCar_Success() throws Exception {
        Car updatedCar = new Car(1L, 4, "Black", "BMW");
        doNothing().when(carRepository).updateCar(1L, updatedCar);

        carService.updateCar(1L, updatedCar);

        verify(carRepository, times(1)).updateCar(1L, updatedCar);
    }

    @Test
    @DisplayName("Should throw exception when updating a non-existent car")
    void testUpdateCar_NotFound() throws Exception {
        Car updatedCar = new Car(1L, 4, "Black", "BMW");
        doThrow(new Exception("Car with ID 1 not found")).when(carRepository).updateCar(1L, updatedCar);

        Exception exception = assertThrows(Exception.class, () -> carService.updateCar(1L, updatedCar));
        assertEquals("Car with ID 1 not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should delete car when ID exists")
    void testDeleteCarById_Success() throws Exception {
        doNothing().when(carRepository).deleteCarById(1L);

        carService.deleteCarById(1L);

        verify(carRepository, times(1)).deleteCarById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting a non-existent car")
    void testDeleteCarById_NotFound() throws Exception {
        doThrow(new Exception("Car with ID 1 not found")).when(carRepository).deleteCarById(1L);

        Exception exception = assertThrows(Exception.class, () -> carService.deleteCarById(1L));
        assertEquals("Car with ID 1 not found", exception.getMessage());
    }

}