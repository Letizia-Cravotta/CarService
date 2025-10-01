package com.ntt.CarService.model;

public class Car {
    private Long carId;
    private int numberOfWheels;
    private String color;
    private String brand;

    public Car(Long carId, int numberOfWheels, String color, String brand) {
        this.carId = carId;
        this.numberOfWheels = numberOfWheels;
        this.color = color;
        this.brand = brand;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public int getNumberOfWheels() {
        return numberOfWheels;
    }

    public void setNumberOfWheels(int numberOfWheels) {
        this.numberOfWheels = numberOfWheels;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
