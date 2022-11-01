package repository;

import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
import models.cars.Engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class EngineRepository {
    private final Connection connection;

    public Engine insert(Engine engine) {
        String INSERT = "INSERT INTO engine(max_speed, fuel_type_id, transmission_type_id, volume, fuel_consumption)VALUES(?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setInt(1, engine.getMaxSpeed());
            statement.setObject(2, engine.getFuelTypeId());
            statement.setObject(3, engine.getTransmissionTypeId());
            statement.setDouble(4, engine.getVolume());
            statement.setDouble(5, engine.getFuelConsumption());
            statement.execute();
            return engine;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public List<Engine> getAll() {
        String GET_ALL = "SELECT * FROM engine WHERE status";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL);
            List<Engine> list = new ArrayList<>();
            while (resultSet.next()) {
                Engine engine = (Engine) Mapper.mapSingleFromResultSet(resultSet, Engine.class);
                list.add(engine);
            }
            resultSet.close();
            return list;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public Engine getById(UUID id) {
        String GET_BY_ID = "SELECT * FROM engine WHERE id=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Engine engine = (Engine) Mapper.mapSingleFromResultSet(resultSet, Engine.class);
                resultSet.close();
                return engine;
            }

            resultSet.close();
            throw new CarRentalException(String.format("Engine with id %s not found", id));
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public Engine update(Engine engine) {
        String UPDATE = "UPDATE engine SET max_speed = ?, " +
                "fuel_type_id = ?, transmission_type_id = ?, " +
                "volume = ?, fuel_consumption = ? WHERE id = ? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setInt(1, engine.getMaxSpeed());
            statement.setObject(2, engine.getFuelTypeId());
            statement.setObject(3, engine.getTransmissionTypeId());
            statement.setDouble(4, engine.getVolume());
            statement.setDouble(5, engine.getFuelConsumption());
            statement.execute();
            return engine;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public void delete(UUID id) {
        String INACTIVATE = "UPDATE engine SET status = FALSE WHERE id = ? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(INACTIVATE)) {
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }
}