package services.car;

import connection.Transactionable;
import exception.CarRentalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.cars.Car;
import models.cars.CarComfort;
import models.cars.CarType;
import org.apache.commons.lang3.StringUtils;
import repository.CarRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class CarService implements CarInterface, Transactionable {
    private CarRepository carRepository;

    @Override
    public Car insert(Car car) {
        log.info("Trying to insert new car");
        validateCar(car);
        Car insertedCar = wrapIntoTransaction(() -> carRepository.insert(car));
        log.info("Car has been inserted successfully");
        return insertedCar;
    }

    @Override
    public List<Car> getAll() {
        log.info("Trying to get all cars");
        return carRepository.getAll();
    }

    @Override
    public Car getById(UUID id) {
        log.info("Trying to get car by id");
        if (Objects.isNull(id)) {
            log.error("Can not find car. Car id must be not null");
            throw new CarRentalException("Car id must be NOT null");
        }
        return carRepository.getById(id);
    }

    @Override
    public Car update(Car car) {
        log.info("Trying to update car {}", car);
        validateCar(car);
        if (!carRepository.isExistById(car.getId())) {
            log.warn("Car with id {} not found", car.getId());
            throw new CarRentalException("Car with id %s not found", car.getId());
        }
        Car updatedCar = wrapIntoTransaction(() -> carRepository.update(car));
        log.info("Car has been updated successfully");
        return updatedCar;
    }

    @Override
    public boolean delete(UUID id) {
        if (Objects.isNull(id)) {
            log.error("Can not find car. Car id must be not null");
            throw new CarRentalException("Can't remove the car, its id must be NOT null");
        }
        return wrapIntoTransaction(() -> {
            Car car = carRepository.getById(id);
            car.setStatus(Boolean.FALSE);
            carRepository.update(car);
            return car.isStatus() != Boolean.TRUE;
        });
    }

    @Override
    public List<Car> getByCarType(CarType carType) {
        log.info("Trying to get all cars by the same car type");
        validateCarType(carType);
        return carRepository.getByCarType(carType);
    }

    @Override
    public List<Car> getByCarComfort(CarComfort carComfort) {
        log.info("Trying to get all cars by the same car comfort");
        validateCarComfort(carComfort);
        return carRepository.getByCarComfort(carComfort);
    }


    private void validateCar(Car car) {
        if (Objects.isNull(car)) {
            log.error("Car is null {}", car);
            throw new CarRentalException("Car must be NOT null");
        }
        if (StringUtils.isBlank(car.getNumber()) ||
                StringUtils.isBlank(car.getBrand()) ||
                StringUtils.isBlank(car.getModel()) ||
                (car.getDeposit() <= 0 || car.getDeposit() > Integer.MAX_VALUE) ||
                (car.getPrice() <= 0 || car.getPrice() > Integer.MAX_VALUE)) {
            log.error("Car properties have invalid format {}", car);
            throw new CarRentalException("Car has invalid data");
        }
    }


    private void validateCarComfort(CarComfort carComfort) {
        if (Objects.isNull(carComfort)) {
            log.error("Car comfort is null");
            throw new CarRentalException("Car comfort must be NOT null");
        }
        if (StringUtils.isBlank(carComfort.getName()) || StringUtils.isBlank(carComfort.getDescription())) {
            log.error("Car comfort properties have invalid format {}", carComfort);
            throw new CarRentalException("Car comfort has invalid data");
        }
    }


    private void validateCarType(CarType carType) {
        if (Objects.isNull(carType)) {
            log.error("Car type is null {}", carType);
            throw new CarRentalException("Car type must be NOT null");
        }
        if (StringUtils.isBlank(carType.getName())) {
            log.error("Car type properties have invalid format {}", carType);
            throw new CarRentalException("Car type has invalid data");
        }
    }
}