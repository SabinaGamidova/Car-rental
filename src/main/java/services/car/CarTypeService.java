package services.car;

import connection.Transactionable;
import exception.CarRentalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.cars.CarType;
import org.apache.commons.lang3.StringUtils;
import repository.CarTypeRepository;
import services.CrudGenericInterface;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class CarTypeService implements CrudGenericInterface<CarType>, Transactionable {
    CarTypeRepository carTypeRepository;

    @Override
    public CarType insert(CarType carType) {
        log.info("Trying to insert new car type");
        validateCarType(carType);
        CarType type = wrapIntoTransaction(() -> carTypeRepository.insert(carType));
        log.info("Car comfort has been inserted successfully");
        return type;
    }

    @Override
    public List<CarType> getAll() {
        log.info("Trying to get all car types");
        return carTypeRepository.getAll();
    }

    @Override
    public CarType getById(UUID id) {
        log.info("Trying to get car type by id");
        if (Objects.isNull(id)) {
            log.error("Can not find car type. Car type id must be not null");
            throw new CarRentalException("Car type id must be NOT null");
        }
        return carTypeRepository.getById(id);
    }

    @Override
    public CarType update(CarType carType) {
        log.info("Trying to update car type {}", carType);
        validateCarType(carType);
        if (!carTypeRepository.isExistById(carType.getId())) {
            log.warn("Car type with id {} not found", carType.getId());
            throw new CarRentalException("Car type with id %s not found", carType.getId());
        }
        CarType type = wrapIntoTransaction(() -> carTypeRepository.update(carType));
        log.info("Car type has been updated successfully");
        return type;
    }


    @Override
    public boolean delete(UUID id) {
        if (Objects.isNull(id)) {
            log.error("Can not find car type. Car type id must be not null");
            throw new CarRentalException("Can't remove the car type, its id must be NOT null");
        }
        return wrapIntoTransaction(() -> {
            CarType carType = carTypeRepository.getById(id);
            carType.setStatus(Boolean.FALSE);
            carTypeRepository.update(carType);
            return carType.isStatus() != Boolean.TRUE;
        });
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