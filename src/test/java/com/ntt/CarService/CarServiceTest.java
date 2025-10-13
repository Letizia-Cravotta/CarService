package com.ntt.CarService;

import com.ntt.CarService.exceptions.APIException;
import com.ntt.CarService.exceptions.ResourceNotFoundException;
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
import java.util.Optional;

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
    @DisplayName("Should return all cars when existing")
    void testGetAllCars() {
        Car car1 = new Car(1L, 4, "Green", "Toyota");
        Car car2 = new Car(2L, 4, "Red", "Honda");

        when(carRepository.findAll()).thenReturn(List.of(car1, car2));

        List<Car> actualCars = carService.getAllCars();

        assertNotNull(actualCars);
        assertEquals(2, actualCars.size());
        assertEquals("Toyota", actualCars.getFirst().getBrand());
    }

    @Test
    @DisplayName("Should throw APIException when no cars are found")
    void testGetAllCars_Empty() {
        when(carRepository.findAll()).thenReturn(List.of());

        assertThrows(APIException.class, () -> carService.getAllCars());
    }

    @Test
    @DisplayName("Should return car by ID when found")
    void testGetCarById_Found() {
        Car car = new Car(1L, 4, "Blue", "Ford");
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        Car result = carService.getCarById(1L);

        assertNotNull(result);
        assertEquals("Ford", result.getBrand());
    }

    @Test
    @DisplayName("Should throw exception when car by ID is not found")
    void testGetCarById_NotFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> carService.getCarById(1L));
    }

    @Test
    @DisplayName("Should create a new car")
    void testCreateCar_Success() {
        Car car = new Car(null, 2, "Yellow", "Fiat");
        when(carRepository.save(car)).thenReturn(car);


        carService.createCar(car);

        verify(carRepository, times(1)).save(car);
    }

    @Test
    @DisplayName("Should update car when ID exists")
    void testUpdateCar_Success() {
        Long carId = 1L;
        Car existingCar = new Car(carId, 4, "Red", "Toyota");
        Car updatedCar = new Car(carId, 2, "Blue", "Honda");

        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        when(carRepository.save(any(Car.class))).thenReturn(existingCar);

        carService.updateCar(carId, updatedCar);

        assertEquals(2, existingCar.getNumberOfWheels());
        assertEquals("Blue", existingCar.getColor());
        assertEquals("Honda", existingCar.getBrand());
        verify(carRepository, times(1)).save(existingCar);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when car ID does not exist")
    void testUpdateCar_NotFound() {
        Long carId = 1L;
        Car updatedCar = new Car(carId, 2, "Blue", "Honda");

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> carService.updateCar(carId, updatedCar));
        assertTrue(exception.getMessage().contains("not found"));
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    @DisplayName("Should delete car when ID exists")
    void testDeleteCarById_Success() {
        when(carRepository.existsById(1L)).thenReturn(true);

        carService.deleteCarById(1L);

        verify(carRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting a non-existent car")
    void testDeleteCarById_NotFound() {
        when(carRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> carService.deleteCarById(1L));
        assertTrue(exception.getMessage().contains("not found"));
    }


}