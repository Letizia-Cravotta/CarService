package com.ntt.CarService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntt.CarService.controller.CarController;
import com.ntt.CarService.model.Car;
import com.ntt.CarService.service.CarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the CarController class.
 */
@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    @Test
    @DisplayName("Should return all cars")
    void testGetAllCars() {
        Car car1 = new Car(1L, 4, "Blue", "Ford");
        Car car2 = new Car(2L, 2, "Red", "Chevrolet");
        when(carService.getAllCars()).thenReturn(Arrays.asList(car1, car2));

        List<Car> cars = carController.getAllCars();
        assertEquals(2, cars.size());
        verify(carService, times(1)).getAllCars();
    }

    @Test
    @DisplayName("Should return empty list when no cars are found")
    void testGetAllCars_NotFound() {
        when(carService.getAllCars()).thenReturn(List.of());

        List<Car> cars = carController.getAllCars();
        assertTrue(cars.isEmpty());
        verify(carService, times(1)).getAllCars();
    }

    @Test
    @DisplayName("Should return car by ID when found")
    void testGetCarById_Found() throws Exception {
        Car car = new Car(1L, 4, "Black", "BMW");
        when(carService.getCarById(1L)).thenReturn(car);

        ResponseEntity<?> response = carController.getCarById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(car, response.getBody());
    }

    @Test
    @DisplayName("Should return 404 when car by ID is not found")
    void testGetCarById_NotFound() throws Exception {
        when(carService.getCarById(1L)).thenThrow(new Exception("Car not found"));

        ResponseEntity<?> response = carController.getCarById(1L);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Car not found", response.getBody());
    }

    @Test
    @DisplayName("Should create a new car")
    void testCreateCar() {
        Car car = new Car(1L, 4, "White", "Audi");
        when(carService.createCar(car)).thenReturn(car);

        ResponseEntity<Car> response = carController.createCar(car);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(car, response.getBody());
        verify(carService, times(1)).createCar(car);
    }
    @Test
    @DisplayName("Should return 400 bad request when creating a car with invalid number of wheels")
    void testCreateCar_InvalidInputNumberOfWheels() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
        Car invalidCar = new Car(null, -4, "green", "Toyota"); // Invalid number of wheels
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/car")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(invalidCar))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 bad request when creating a car with invalid color")
    void testCreateCar_InvalidInputColor() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
        Car invalidCar = new Car(null, 4, "dosrje49!", "Toyota"); // Invalid color
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/car")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(invalidCar))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    @DisplayName("Should return 400 bad request when creating a car with invalid brand name")
    void testCreateCar_InvalidInputBrand() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
        Car invalidCar = new Car(null, 4, "green", "Toyota!"); // Invalid brand name
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/car")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(invalidCar))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Should update car when ID exists")
    void testUpdateCar_Success() throws Exception {
        Car updatedCar = new Car(1L, 4, "Silver", "Mercedes");
        doNothing().when(carService).updateCar(1L, updatedCar);

        ResponseEntity<String> response = carController.updateCar(1L, updatedCar);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Car updated successfully", response.getBody());
    }

    @Test
    @DisplayName("Should return 404 when updating a non-existent car")
    void testUpdateCar_NotFound() throws Exception {
        Car updatedCar = new Car(1L, 4, "Silver", "Mercedes");
        doThrow(new Exception("Car not found")).when(carService).updateCar(1L, updatedCar);

        ResponseEntity<String> response = carController.updateCar(1L, updatedCar);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Car not found", response.getBody());
    }

    @Test
    @DisplayName("Should return 400 bad request when updating a car with invalid number of wheels")
    void testUpdateCar_InvalidInputNumberOfWheels() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
        Car invalidCar = new Car(null, -4, "green", "Toyota"); // Invalid number of wheels
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/car/id/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(invalidCar))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 bad request when updating a car with invalid color")
    void testUpdateCar_InvalidInputColor() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
        Car invalidCar = new Car(null, 4, "dosrje49!", "Toyota"); // Invalid color
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/car/id/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(invalidCar))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    @DisplayName("Should return 400 bad request when updating a car with invalid number of wheels")
    void testUpdateCar_InvalidInputBrand() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
        Car invalidCar = new Car(null, 4, "green", "Toyota!"); // Invalid brand name
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/car/id/1")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(invalidCar))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Should delete car when ID exists")
    void testDeleteCar_Success() throws Exception {
        doNothing().when(carService).deleteCarById(1L);

        ResponseEntity<String> response = carController.deleteCar(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Car deleted successfully", response.getBody());
    }

    @Test
    @DisplayName("Should return 404 when deleting a non-existent car")
    void testDeleteCar_NotFound() throws Exception {
        doThrow(new Exception("Car not found")).when(carService).deleteCarById(1L);

        ResponseEntity<String> response = carController.deleteCar(1L);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Car not found", response.getBody());
    }
}

