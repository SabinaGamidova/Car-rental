package services.engine;

import connection.Transactionable;
import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.cars.Engine;
import repository.EngineRepository;
import services.CrudGenericInterface;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class EngineService implements CrudGenericInterface<Engine>, Transactionable {
    private final EngineRepository engineRepository;

    @Override
    public Engine insert(Engine engine) {
        log.info("Trying to insert new engine");
        validateEngine(engine);
        Engine engineToInsert = wrapIntoTransaction(() -> engineRepository.insert(engine));
        log.info("Engine has been inserted successfully");
        return engineToInsert;
    }

    @Override
    public List<Engine> getAll() {
        return engineRepository.getAll();
    }

    @Override
    public Engine getById(UUID id) {
        if (Objects.isNull(id)) {
            log.error("Can not find engine. Its id must be not null");
            throw new CarRentalException("Engine id must be NOT null");
        }
        return engineRepository.getById(id);
    }


    @Override
    public Engine update(Engine engine) {
        validateEngine(engine);
        if (!engineRepository.isExistById(engine.getId())) {
            log.warn("Engine with id {} not found", engine.getId());
            throw new CarRentalException("Engine with id %s not found", engine.getId());
        }
        Engine engineToUpdate = wrapIntoTransaction(() -> engineRepository.update(engine));
        log.info("Engine has been updated successfully");
        return engineToUpdate;
    }


    @Override
    public boolean delete(UUID id) {
        if (Objects.isNull(id)) {
            log.error("Can not find engine. Its id must be not null");
            throw new CarRentalException("Can't remove the engine, its id must be NOT null");
        }
        return wrapIntoTransaction(() -> {
            Engine engine = engineRepository.getById(id);
            engine.setStatus(Boolean.FALSE);
            engineRepository.update(engine);
            return engine.isStatus() != Boolean.TRUE;
        });
    }


    private void validateEngine(Engine engine) {
        if (Objects.isNull(engine) || Objects.isNull(engine.getFuelTypeId()) || Objects.isNull(engine.getTransmissionTypeId())) {
            log.error("Engine is null {} or incorrect id for fuel/transmission type", engine);
            throw new CarRentalException("Engine must be NOT null");
        }
        if (engine.getMaxSpeed() <= 0 || engine.getMaxSpeed() > EngineCharacteristics.MAX_SPEED) {
            log.error("Invalid max speed - not within the acceptable range [1, " + EngineCharacteristics.MAX_SPEED + "]");
            throw new CarRentalException("Max speed must be less than " + EngineCharacteristics.MAX_SPEED + " km/h");
        }
        if(engine.getVolume() <= 0 || engine.getVolume() > EngineCharacteristics.MAX_VOLUME){
            log.error("Invalid volume - not within the acceptable range [1, " + EngineCharacteristics.MAX_VOLUME + "]");
            throw new CarRentalException("Volume must be less than " + EngineCharacteristics.MAX_VOLUME);
        }
        if(engine.getFuelConsumption() <= 0 || engine.getFuelConsumption() > EngineCharacteristics.MAX_FUEL_CONSUMPTION){
            log.error("Invalid fuel consumption - not within the acceptable range [1, " + EngineCharacteristics.MAX_FUEL_CONSUMPTION + "]");
            throw new CarRentalException("Fuel consumption must be less than " + EngineCharacteristics.MAX_FUEL_CONSUMPTION);
        }
    }
}