package services.car;

import models.cars.CarType;
import services.CrudGenericInterface;

import java.util.List;
import java.util.UUID;

public class CarTypeService implements CrudGenericInterface<CarType> {

    @Override
    public void insert(CarType carType) {
        return;
    }

    @Override
    public List<CarType> getAll() {
        return null;
    }

    @Override
    public CarType getById(UUID id) {
        return null;
    }

    @Override
    public void update(CarType carType) {
        return;
    }

    @Override
    public void delete(UUID id) {
    }
}