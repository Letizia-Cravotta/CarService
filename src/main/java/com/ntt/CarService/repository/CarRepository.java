package com.ntt.CarService.repository;

import com.ntt.CarService.model.Car;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CarRepository {
    public List<Car> cars = new ArrayList<>();
}
