package com.ntt.CarService.repository;

import com.ntt.CarService.model.Car;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing {@link Car} entities in an in-memory list.
 * This class handles the storage, retrieval, and deletion of cars.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

}
