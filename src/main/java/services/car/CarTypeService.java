package services.car;

import models.cars.CarType;
import services.CrudGenericInterface;

import java.util.List;
import java.util.UUID;

public class CarTypeService implements CrudGenericInterface<CarType> {

    @Override
    public CarType insert(CarType carType) {
        return null;
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
    public CarType update(CarType carType) {
        return null;
    }

    @Override
    public void delete(UUID id) {
        return;
    }
}