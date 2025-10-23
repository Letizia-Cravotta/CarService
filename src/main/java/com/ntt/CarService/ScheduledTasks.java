package com.ntt.CarService;

import com.ntt.CarService.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTasks {

    @Autowired
    private CarRepository carRepository;

    @Scheduled(cron = "*/10 * * * * *")
    public void runEvery10seconds() {
    log.info("Cars in database: {}", carRepository.count());
    }
}
