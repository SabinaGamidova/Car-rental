package services.car;

import connection.Transactionable;
import connection.Worker;
import exception.CarRentalException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
        validateCarComfort(carComfort);
        return wrapIntoTransaction(()-> carComfortRepository.insert(carComfort));
    }

    @Override
    public List<CarComfort> getAll() {
        return carComfortRepository.getAll();
    }

    @Override
    public CarComfort getById(UUID id) {
        if (Objects.isNull(id)) {
            throw new CarRentalException("Car comfort id must be NOT null");
        }
        CarComfort carComfort = carComfortRepository.getById(id);
        if(Objects.isNull(carComfort)){
            throw new CarRentalException("Car comfort with such an id not found");
        }
        return carComfort;
    }

    @Override
    public CarComfort update(CarComfort carComfort) {
        validateCarComfort(carComfort);
        return wrapIntoTransaction(() -> carComfortRepository.update(carComfort));
    }

    @Override
    public void delete(UUID id) {
        if (Objects.isNull(id)) {
            throw new CarRentalException("Can't remove the car comfort, its id must be NOT null");
        }
        if(Objects.isNull(carComfortRepository.getById(id))){
            throw new CarRentalException("Car comfort by such an id must be NOT null");
        }
        wrapIntoTransaction(()-> {
            carComfortRepository.delete(id);
        });
    }

    private void validateCarComfort(CarComfort carComfort) {
        if (Objects.isNull(carComfort)) {
            throw new CarRentalException("Car comfort must be NOT null");
        }
        if (StringUtils.isBlank(carComfort.getName()) || StringUtils.isBlank(carComfort.getDescription())) {
            throw new CarRentalException("Car comfort has invalid data");
        }
    }
}