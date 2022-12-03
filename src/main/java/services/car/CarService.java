package services.car;

import connection.Transactionable;
import exception.CarRentalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.cars.Car;
import models.order.Order;
import org.apache.commons.lang3.StringUtils;
import repository.CarRepository;
import repository.OrderRepository;
import util.DateTimeUtil;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class CarService implements CarInterface, Transactionable {
    private final CarRepository carRepository;
    private final OrderRepository orderRepository;


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
    public List<Car> getAvailable(Date from, Date to) {
        log.info("Trying to get available cars");
        DateTimeUtil.validateWithToday(from);
        DateTimeUtil.validateWithToday(to);
        DateTimeUtil.validateDates(from, to);
        List<Order> orders = orderRepository.getOrdersBetweenDates(from, to);
        return carRepository.getAll()
                .stream()
                .filter(car -> isAvailableCar(orders, car))
                .collect(Collectors.toList());
    }

    private boolean isAvailableCar(List<Order> orders, Car car){
        return orders.stream().noneMatch(order -> order.getCarId().equals(car.getId()));
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
    public List<Car> getByCarType(UUID carTypeId) {
        log.info("Trying to get all cars by the same car type");
        if (Objects.isNull(carTypeId)) {
            log.error("Can not find car type. Car type id must be not null");
            throw new CarRentalException("Car type id must be NOT null");
        }
        return carRepository.getByCarType(carTypeId);
    }

    @Override
    public List<Car> getByCarComfort(UUID carComfortId) {
        log.info("Trying to get all cars by the same car comfort");
        if (Objects.isNull(carComfortId)) {
            log.error("Can not find car comfort. Car comfort id must be not null");
            throw new CarRentalException("Car comfort id must be NOT null");
        }
        return carRepository.getByCarComfort(carComfortId);
    }


    private void validateCar(Car car) {
        if (Objects.isNull(car)) {
            log.error("Car is null {}", car);
            throw new CarRentalException("Car must be NOT null");
        }
        if (StringUtils.isBlank(car.getNumber()) ||
                StringUtils.isBlank(car.getBrand()) ||
                StringUtils.isBlank(car.getModel()) ||
                (car.getDeposit() <= 0 || car.getDeposit() > CarMaxPrice.MAX_DEPOSIT.getValue()) ||
                (car.getPrice() <= 0 || car.getPrice() > CarMaxPrice.MAX_PRICE.getValue())) {
            log.error("Car properties have invalid format {}", car);
            throw new CarRentalException("Car has invalid data");
        }
    }
}