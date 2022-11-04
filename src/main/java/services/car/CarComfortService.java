package services.car;

import connection.Transactionable;
import exception.CarRentalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.cars.Car;
import models.cars.CarComfort;
import org.apache.commons.lang3.StringUtils;
import repository.CarComfortRepository;
import services.CrudGenericInterface;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class CarComfortService implements CrudGenericInterface<CarComfort>, Transactionable {
    private CarComfortRepository carComfortRepository;

    @Override
    public CarComfort insert(CarComfort carComfort) {
        log.info("Trying to insert new car comfort");
        validateCarComfort(carComfort);
        CarComfort c = wrapIntoTransaction(() -> carComfortRepository.insert(carComfort));
        log.info("Car comfort has been inserted successfully");
        return c;
    }

    @Override
    public List<CarComfort> getAll() {
        log.info("Trying to get all car comforts");
        return carComfortRepository.getAll();
    }

    @Override
    public CarComfort getById(UUID id) {
        log.info("Trying to get car comfort by id");
        if (Objects.isNull(id)) {
            log.error("Can not find car comfort. Car comfort id must be not null");
            throw new CarRentalException("Car comfort id must be NOT null");
        }
        try {
            return carComfortRepository.getById(id);
        } catch (CarRentalException exception) {
            log.warn("Car comfort with id {} not found", id);
            throw new CarRentalException("Car comfort by SUCH an id must be NOT null");
        }
    }

    @Override
    public CarComfort update(CarComfort carComfort) {
        log.info("Trying to update car comfort {}", carComfort);
        if(!isCarComfortByIdExist(carComfort)){
            log.warn("Car comfort with id {} not found", carComfort.getId());
            throw new CarRentalException("Car comfort by such an id must be NOT null");
        }
        validateCarComfort(carComfort);
        CarComfort c = wrapIntoTransaction(() -> carComfortRepository.update(carComfort));
        log.info("Car comfort has been updated successfully");
        return c;
    }

    @Override
    public void delete(UUID id) {
        if (Objects.isNull(id)) {
            log.error("Can not find car comfort. Car comfort id must be not null");
            throw new CarRentalException("Can't remove the car comfort, its id must be NOT null");
        }
        try {
            CarComfort carComfort = carComfortRepository.getById(id);
            wrapIntoTransaction(() -> {
                carComfortRepository.delete(carComfort.getId());
            });
        } catch (CarRentalException exception) {
            log.warn("Car with id {} not found", id);
            throw new CarRentalException("Car by SUCH an id must be NOT null");
        }
    }


    private void validateCarComfort(CarComfort carComfort) {
        if (Objects.isNull(carComfort)) {
            log.error("Car comfort is null {}", carComfort);
            throw new CarRentalException("Car comfort must be NOT null");
        }
        if (StringUtils.isBlank(carComfort.getName()) || StringUtils.isBlank(carComfort.getDescription())) {
            log.error("Car comfort properties have invalid format {}", carComfort);
            throw new CarRentalException("Car comfort has invalid data");
        }
    }

    private boolean isCarComfortByIdExist(CarComfort carComfort){
        return carComfortRepository.getAll().stream().anyMatch(c -> c.getId() == carComfort.getId());
    }
}