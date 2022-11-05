package services.car;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.cars.CarComfort;
import repository.CarComfortRepository;
import services.CrudGenericInterface;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CarComfortService implements CrudGenericInterface<CarComfort> {
    private CarComfortRepository carComfortRepository;

    @Override
    public CarComfort insert(CarComfort carComfort) {
        return null;
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
    public CarComfort update(CarComfort carComfort) {
        return null;
    }

    @Override
    public void delete(UUID id) {}
}