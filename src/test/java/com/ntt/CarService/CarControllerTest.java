package com.ntt.CarService;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ntt.CarService.controller.CarController;
//import com.ntt.CarService.model.Car;
//import com.ntt.CarService.service.CarService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import com.ntt.CarService.exceptions.APIException;
//
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * Unit tests for the CarController class.
// */
//@ExtendWith(MockitoExtension.class)
//class CarControllerTest {
//
//    @Mock
//    private CarService carService;
//
//    @InjectMocks
//    private CarController carController;
//
//    @Test
//    @DisplayName("Should return all cars")
//    void testGetAllCars() {
//        Car car1 = new Car(1L, 4, "Blue", "Ford");
//        Car car2 = new Car(2L, 2, "Red", "Chevrolet");
//        when(carService.getAllCars()).thenReturn(Arrays.asList(car1, car2));
//
//        List<Car> cars = carController.getAllCars();
//        assertEquals(2, cars.size());
//        verify(carService, times(1)).getAllCars();
//    }
//
//    @Test
//    @DisplayName("Should throw APIException when no cars found")
//    void testGetAllCars_NotFound() {
//        when(carService.getAllCars()).thenThrow(new APIException("No cars found"));
//
//        APIException exception = assertThrows(
//                APIException.class,
//                () -> carController.getAllCars()
//        );
//        assertEquals("No cars found", exception.getMessage());
//        verify(carService, times(1)).getAllCars();
//    }
//
//    @Test
//    @DisplayName("Should return car by ID when found")
//    void testGetCarById_Found() throws Exception {
//        Car car = new Car(1L, 4, "Black", "BMW");
//        when(carService.getCarById(1L)).thenReturn(car);
//
//        ResponseEntity<?> response = carController.getCarById(1L);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(car, response.getBody());
//    }
//
//    @Test
//    @DisplayName("Should return 404 when car by ID is not found")
//    void testGetCarById_NotFound() throws Exception {
//        when(carService.getCarById(1L)).thenThrow(new Exception("Car not found"));
//
//        ResponseEntity<?> response = carController.getCarById(1L);
//        assertEquals(404, response.getStatusCodeValue());
//        assertEquals("Car not found", response.getBody());
//    }
//
//    @Test
//    @DisplayName("Should create a new car")
//    void testCreateCar() {
//        Car car = new Car(1L, 4, "White", "Audi");
//        when(carService.createCar(car)).thenReturn(car);
//
//        ResponseEntity<Car> response = carController.createCar(car);
//        assertEquals(201, response.getStatusCodeValue());
//        assertEquals(car, response.getBody());
//        verify(carService, times(1)).createCar(car);
//    }
//
//    @ParameterizedTest
//    @ValueSource(ints = {3, 4, 6, 8})
//    @DisplayName("Should create a new car with valid number of wheels")
//    void testCreateCar_ValidInputNumberOfWheels(int numberOfWheels) throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
//        Car validCar = new Car(null, numberOfWheels, "green", "Toyota"); // Valid number of wheels
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/car")
//                                .contentType("application/json")
//                                .content(objectMapper.writeValueAsString(validCar))
//                )
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//    }
//
//    @ParameterizedTest
//    @ValueSource(ints = {-4, 0, 1, 2, 5, 7, 9, Integer.MAX_VALUE})
//    @DisplayName("Should return 400 bad request when creating a car with invalid number of wheels")
//    void testCreateCar_InvalidInputNumberOfWheels(int numberOfWheels) throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
//        Car invalidCar = new Car(null, numberOfWheels, "green", "Toyota"); // Invalid number of wheels
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/car")
//                                .contentType("application/json")
//                                .content(objectMapper.writeValueAsString(invalidCar))
//                )
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"Green", "Red", "Blue", "Black", "White", "Silver", "Yellow"})
//    @DisplayName("Should create a new car with valid color")
//    void testCreateCar_ValidInputColor(String color) throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
//        Car validCar = new Car(null, 4, color, "Toyota"); // Valid color
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/car")
//                                .contentType("application/json")
//                                .content(objectMapper.writeValueAsString(validCar))
//                )
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"", " ", "       ", "1234", "red123", "blue!", "@green", "white$", "black%", "g-r-e-e-n"})
//    @DisplayName("Should return 400 bad request when creating a car with invalid color")
//    void testCreateCar_InvalidInputColor(String color) throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
//        Car invalidCar = new Car(null, 4, color, "Toyota"); // Invalid color
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/car")
//                                .contentType("application/json")
//                                .content(objectMapper.writeValueAsString(invalidCar))
//                )
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"Toyota", "Chevrolet", "Ford", "Nissan", "Mazda", "Mercedes-Benz", "Land Rover"})
//    @DisplayName("Should create a new car with valid brand name")
//    void testCreateCar_ValidInputBrand(String brandName) throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
//        Car validCar = new Car(null, 4, "green", brandName); // Valid brand name
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/car")
//                                .contentType("application/json")
//                                .content(objectMapper.writeValueAsString(validCar))
//                )
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"", "1234", "Toyota1", "Chevrolet!", "@Ford", "Nissan$", "Mazda%"})
//    @DisplayName("Should return 400 bad request when creating a car with invalid brand name")
//    void testCreateCar_InvalidInputBrand(String brandName) throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
//        Car invalidCar = new Car(null, 4, "green", brandName); // Invalid brand name
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/car")
//                                .contentType("application/json")
//                                .content(objectMapper.writeValueAsString(invalidCar))
//                )
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Should update car when ID exists")
//    void testUpdateCar_Success() throws Exception {
//        Car updatedCar = new Car(1L, 4, "Silver", "Mercedes");
//        when(carService.updateCar(1L, updatedCar)).thenReturn(updatedCar);
//
//        ResponseEntity<?> response = carController.updateCar(1L, updatedCar);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(updatedCar, response.getBody());
//    }
//
//    @Test
//    @DisplayName("Should return 404 when updating a non-existent car")
//    void testUpdateCar_NotFound() throws Exception {
//        Car updatedCar = new Car(1L, 4, "Silver", "Mercedes");
//        doThrow(new Exception("Car not found")).when(carService).updateCar(1L, updatedCar);
//
//        ResponseEntity<?> response = carController.updateCar(1L, updatedCar);
//        assertEquals(404, response.getStatusCodeValue());
//        assertEquals("Car not found", response.getBody());
//    }
//
//    @ParameterizedTest
//    @ValueSource(ints = {3, 4, 6, 8})
//    @DisplayName("Should update car with valid number of wheels")
//    void testUpdateCar_ValidInputNumberOfWheels(int numOfWheels) throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
//        Car validCar = new Car(null, numOfWheels, "green", "Toyota"); // Valid number of wheels
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/car/id/1")
//                                .contentType("application/json")
//                                .content(objectMapper.writeValueAsString(validCar))
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @ParameterizedTest
//    @ValueSource(ints = {-4, 0, 1, 2, 5, 7, 9, Integer.MAX_VALUE})
//    @DisplayName("Should return 400 bad request when updating a car with invalid number of wheels")
//    void testUpdateCar_InvalidInputNumberOfWheels(int numOfWheels) throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
//        Car invalidCar = new Car(null, numOfWheels, "green", "Toyota"); // Invalid number of wheels
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/car/id/1")
//                                .contentType("application/json")
//                                .content(objectMapper.writeValueAsString(invalidCar))
//                )
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"Green", "Red", "Blue", "Black", "White", "Silver", "Yellow"})
//    @DisplayName("Should update car with valid color")
//    void testUpdateCar_ValidInputColor(String color) throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
//        Car validCar = new Car(null, 4, color , "Toyota"); // Valid color
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/car/id/1")
//                                .contentType("application/json")
//                                .content(objectMapper.writeValueAsString(validCar))
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"", "1234", "red123", "blue!", "@green", "white$", "black%", "g-r-e-e-n"})
//    @DisplayName("Should return 400 bad request when updating a car with invalid color")
//    void testUpdateCar_InvalidInputColor(String color) throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
//        Car invalidCar = new Car(null, 4, color , "Toyota"); // Invalid color
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/car/id/1")
//                                .contentType("application/json")
//                                .content(objectMapper.writeValueAsString(invalidCar))
//                )
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"Toyota", "Chevrolet", "Ford", "Nissan", "Mazda", "Mercedes-Benz", "Land Rover"})
//    @DisplayName("Should update car with valid brand name")
//    void testUpdateCar_ValidInputBrand(String brandName) throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
//        Car validCar = new Car(null, 4, "green", brandName); // Valid brand name
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/car/id/1")
//                                .contentType("application/json")
//                                .content(objectMapper.writeValueAsString(validCar))
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"", "1234", "Toyota1", "Chevrolet!", "@Ford", "Nissan$", "Mazda%"})
//    @DisplayName("Should return 400 bad request when updating a car with invalid brand name")
//    void testUpdateCar_InvalidInputBrand(String brandName) throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
//        Car invalidCar = new Car(null, 4, "green", brandName); // Invalid brand name
//        ObjectMapper objectMapper = new ObjectMapper();
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put("/car/id/1")
//                                .contentType("application/json")
//                                .content(objectMapper.writeValueAsString(invalidCar))
//                )
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Should delete car when ID exists")
//    void testDeleteCar_Success() throws Exception {
//        Car carToDelete = new Car(1L, 4, "Blue", "Ford");
//        when(carService.deleteCarById(1L)).thenReturn(carToDelete);
//
//        ResponseEntity<?> response = carController.deleteCar(1L);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(carToDelete, response.getBody());
//    }
//
//    @Test
//    @DisplayName("Should return 404 when deleting a non-existent car")
//    void testDeleteCar_NotFound() throws Exception {
//        doThrow(new Exception("Car not found")).when(carService).deleteCarById(1L);
//
//        ResponseEntity<?> response = carController.deleteCar(1L);
//        assertEquals(404, response.getStatusCodeValue());
//        assertEquals("Car not found", response.getBody());
//    }
//}

