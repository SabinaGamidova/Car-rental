package services.car;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.cars.CarComfort;
import repository.CarComfortRepository;
import repository.TransactionManager;
import services.CrudGenericInterface;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CarComfortService implements CrudGenericInterface<CarComfort>, TransactionManager {
    private CarComfortRepository carComfortRepository;

    @Override
    public void insert(CarComfort carComfort) {
        /*wrapIntoTransaction(() -> {
            carComfortRepository.insert(null);
            carComfortRepository.getAll();
            carComfortRepository.delete(null);
        });*/
    }

    @Override
    public List<CarComfort> getAll() {
        return null;
    }

    @Override
    public CarComfort getById(UUID id) {
        return null;
    }

    @Override
    public void update(CarComfort carComfort) {
        return;
    }

    @Override
    public void delete(UUID id) {}
}