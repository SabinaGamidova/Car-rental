package services.engine;

import exception.CarRentalException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.cars.Engine;
import models.cars.FuelType;
import org.apache.commons.lang3.StringUtils;
import repository.FuelTypeRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class FuelTypeService {
    private final FuelTypeRepository fuelTypeRepository;

    public List<FuelType> getAll() {
        return fuelTypeRepository.getAll();
    }

    public FuelType getByName(String name) {
        if (StringUtils.isBlank(name)) {
            log.error("Invalid name for fuel type");
            throw new CarRentalException("Fuel type must be not blank");
        }
        return fuelTypeRepository.getByName(name);
    }
}