package services.car;

import models.cars.Car;
import models.cars.CarComfort;
import models.cars.CarType;
import services.CrudGenericInterface;

import java.util.List;

public interface CarInterface extends CrudGenericInterface<Car> {

    List<Car> getByCarType(CarType carType);

    List<Car> getByCarComfort(CarComfort carComfort);
}
