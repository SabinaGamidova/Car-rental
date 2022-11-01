package services.car;

import models.cars.Car;
import models.cars.CarComfort;
import models.cars.CarType;

import java.util.List;
import java.util.UUID;


public class CarService implements CarInterface {

    @Override
    public Car insert(Car Car) {
        return null;
    }

    @Override
    public List<Car> getAll() {
        return null;
    }

    @Override
    public Car getById(UUID id) {
        return null;
    }

    @Override
    public Car update(Car car) {
        return null;
    }

    @Override
    public void delete(UUID id) {return;}

    @Override
    public List<Car> getByCarType(CarType carType) {
        return null;
    }

    @Override
    public List<Car> getByCarComfort(CarComfort carComfort) {
        return null;
    }
}