package services.car;

import models.cars.Car;
import models.cars.CarComfort;
import models.cars.CarType;
import services.CrudGenericInterface;

import java.util.List;
import java.util.UUID;

public interface CarInterface extends CrudGenericInterface<Car> {

    List<Car> getByCarType(UUID carTypeId);

    List<Car> getByCarComfort(UUID carComfortId);
}
