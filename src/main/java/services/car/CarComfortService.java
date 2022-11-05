package services.car;

import connection.Transactionable;
import exception.CarRentalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.cars.CarComfort;
import org.apache.commons.lang3.StringUtils;
import repository.CarComfortRepository;
import services.CrudGenericInterface;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class CarComfortService implements CrudGenericInterface<CarComfort>, Transactionable {
    private CarComfortRepository carComfortRepository;

    @Override
    public CarComfort insert(CarComfort carComfort) {
        log.info("Trying to insert new car comfort");
        validateCarComfort(carComfort);
        CarComfort comfort = wrapIntoTransaction(() -> carComfortRepository.insert(carComfort));
        log.info("Car comfort has been inserted successfully");
        return comfort;
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
        return carComfortRepository.getById(id);
    }

    @Override
    public CarComfort update(CarComfort carComfort) {
        log.info("Trying to update car comfort {}", carComfort);
        validateCarComfort(carComfort);
        if(!carComfortRepository.isExistById(carComfort.getId())){
            log.warn("Car comfort with id {} not found", carComfort.getId());
            throw new CarRentalException("Car comfort with id %s not found", carComfort.getId());
        }
        CarComfort comfort = wrapIntoTransaction(() -> carComfortRepository.update(carComfort));
        log.info("Car comfort has been updated successfully");
        return comfort;
    }

    @Override
    public boolean delete(UUID id) {
        if (Objects.isNull(id)) {
            log.error("Can not find car comfort. Car comfort id must be not null");
            throw new CarRentalException("Can't remove the car comfort, its id must be NOT null");
        }
        return wrapIntoTransaction(() -> {
            CarComfort carComfort = carComfortRepository.getById(id);
            carComfort.setStatus(Boolean.FALSE);
            carComfortRepository.update(carComfort);
            return carComfort.isStatus() != Boolean.TRUE;
        });
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
}