package controller;

import com.ntt.CarService.model.Car;
import com.ntt.CarService.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping("/car")
    public List<Car> getAllCars() {
        return carRepository.cars;
    }

}
