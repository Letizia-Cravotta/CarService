package com.ntt.CarService;

import com.ntt.CarService.controller.CarController;
import com.ntt.CarService.model.Car;
import com.ntt.CarService.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCars() {
        Car car1 = new Car(1L , 4, "Blue", "Ford");
        Car car2 = new Car(2L , 2, "Red", "Chevrolet");
        when(carService.getAllCars()).thenReturn(Arrays.asList(car1, car2));

        List<Car> cars = carController.getAllCars();
        assertEquals(2, cars.size());
        verify(carService, times(1)).getAllCars();
    }

    @Test
    void testGetCarById_Found() throws Exception {
        Car car = new Car(1L , 4, "Black", "BMW");
        when(carService.getCarById(1L)).thenReturn(car);

        ResponseEntity<?> response = carController.getCarById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(car, response.getBody());
    }

    @Test
    void testGetCarById_NotFound() throws Exception {
        when(carService.getCarById(1L)).thenThrow(new Exception("Car not found"));

        ResponseEntity<?> response = carController.getCarById(1L);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Car not found", response.getBody());
    }

    @Test
    void testCreateCar() {
        Car car = new Car(1L , 4, "White", "Audi");
        ResponseEntity<?> response = carController.createCar(car);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Car created successfully", response.getBody());
        verify(carService, times(1)).createCar(car);
    }

    @Test
    void testUpdateCar_Success() throws Exception {
        Car updatedCar = new Car(1L , 4, "Silver", "Mercedes");
        doNothing().when(carService).updateCar(1L, updatedCar);

        ResponseEntity<String> response = carController.updateCar(1L, updatedCar);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Car updated successfully", response.getBody());
    }

    @Test
    void testUpdateCar_NotFound() throws Exception {
        Car updatedCar = new Car(1L , 4, "Silver", "Mercedes");
        doThrow(new Exception("Car not found")).when(carService).updateCar(1L, updatedCar);

        ResponseEntity<String> response = carController.updateCar(1L, updatedCar);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Car not found", response.getBody());
    }

    @Test
    void testDeleteCar_Success() throws Exception {
        doNothing().when(carService).deleteCarById(1L);

        ResponseEntity<String> response = carController.deleteCar(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Car deleted successfully", response.getBody());
    }

    @Test
    void testDeleteCar_NotFound() throws Exception {
        doThrow(new Exception("Car not found")).when(carService).deleteCarById(1L);

        ResponseEntity<String> response = carController.deleteCar(1L);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Car not found", response.getBody());
    }
}

