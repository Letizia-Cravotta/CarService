package com.ntt.CarService.repository;

import com.ntt.CarService.model.Car;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository interface for managing {@link Car} entities.
 * Extends the {@link JpaRepository} to provide CRUD operations and query methods.
 * This interface interacts with the database for storing, retrieving, updating, and deleting cars.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

}
