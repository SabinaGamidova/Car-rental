package services.car;

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
        CarType c = wrapIntoTransaction(() -> carTypeRepository.insert(carType));
        log.info("Car comfort has been inserted successfully");
        return c;
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
        try {
            return carTypeRepository.getById(id);
        } catch (CarRentalException exception) {
            log.warn("Car type id {} not found", id);
            throw new CarRentalException("Car type by such an id must be NOT null");
        }
    }

    @Override
    public CarType update(CarType carType) {
        log.info("Trying to update car type {}", carType);
        if(!isCarTypeByIdExist(carType)){
            log.warn("Car type with id {} not found", carType.getId());
            throw new CarRentalException("Car type by such an id must be NOT null");
        }
        validateCarType(carType);
        CarType c = wrapIntoTransaction(() -> carTypeRepository.update(carType));
        log.info("Car type has been updated successfully");
        return c;
    }

    @Override
    public void delete(UUID id) {
        if (Objects.isNull(id)) {
            log.error("Can not find car type. Car type id must be not null");
            throw new CarRentalException("Can't remove the car type, its id must be NOT null");
        }
        try {
            CarType carType = carTypeRepository.getById(id);
            wrapIntoTransaction(() -> {
                carTypeRepository.delete(carType.getId());
            });
        } catch (CarRentalException exception) {
            log.warn("Car type with id {} not found", id);
            throw new CarRentalException("Car type by such an id must be NOT null");
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

    private boolean isCarTypeByIdExist(CarType carType){
        return carTypeRepository.getAll().stream().anyMatch(c -> c.getId() == carType.getId());
    }
}