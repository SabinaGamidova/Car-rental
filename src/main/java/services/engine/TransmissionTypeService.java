package services.engine;

import exception.CarRentalException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.cars.TransmissionType;
import org.apache.commons.lang3.StringUtils;
import repository.TransmissionTypeRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TransmissionTypeService {
    private final TransmissionTypeRepository transmissionTypeRepository;

    public List<TransmissionType> getAll() {
        return transmissionTypeRepository.getAll();
    }

    public TransmissionType getByName(String name) {
        if (StringUtils.isBlank(name)) {
            log.error("Invalid name for transmission type");
            throw new CarRentalException("Transmission type name must be valid");
        }
        return transmissionTypeRepository.getByName(name);
    }
}