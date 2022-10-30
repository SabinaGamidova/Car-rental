package repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
import models.cars.CarType;
import models.cars.Engine;
import models.cars.FuelType;

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

    public void insert(Engine engine) {
        String INSERT = "INSERT INTO engine(max_speed, fuel_type_id, transmission_type_id, volume, fuel_consumption)VALUES(?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            disableAutoCommit();

            statement.setInt(1, engine.getMaxSpeed());
            statement.setObject(2, engine.getFuelTypeId());
            statement.setObject(3, engine.getTransmissionTypeId());
            statement.setDouble(4, engine.getVolume());
            statement.setDouble(5, engine.getFuelConsumption());

            if(statement.execute()) {
                rollbackTransaction();
            }

        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            rollbackTransaction();
            throw new RuntimeException(exception);
        } finally {
            enableAutoCommit();
        }
    }

    public List<Engine> getAll() {//TODO check
        String GET_ALL = "SELECT * FROM engine WHERE status";
        try (Statement statement = connection.createStatement()) {
            disableAutoCommit();
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
            rollbackTransaction();
            throw new RuntimeException(exception);
        }
        finally {
            enableAutoCommit();
        }
    }

    public Engine getById(UUID id) {
        String GET_BY_ID = "SELECT * FROM engine WHERE id=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            disableAutoCommit();
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Engine engine = (Engine) Mapper.mapSingleFromResultSet(resultSet, Engine.class);
                resultSet.close();
                return engine;
            }

            resultSet.close();
            throw new RuntimeException(String.format("Engine with id %s not found", id));
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            rollbackTransaction();
            throw new RuntimeException(exception);
        }
        finally {
            enableAutoCommit();
        }
    }

    public void update(Engine engine) {
        String UPDATE = "UPDATE engine SET max_speed = ?, " +
                "fuel_type_id = ?, transmission_type_id = ?, " +
                "volume = ?, fuel_consumption = ? WHERE id = ? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            disableAutoCommit();

            statement.setInt(1, engine.getMaxSpeed());
            statement.setObject(2, engine.getFuelTypeId());
            statement.setObject(3, engine.getTransmissionTypeId());
            statement.setDouble(4, engine.getVolume());
            statement.setDouble(5, engine.getFuelConsumption());


            if(statement.execute()) {
                rollbackTransaction();
            }

        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            rollbackTransaction();
            throw new RuntimeException(exception);
        }
        finally {
            enableAutoCommit();
        }
    }

    public void delete(UUID id) {
        String INACTIVATE = "UPDATE engine SET status = FALSE WHERE id = ? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(INACTIVATE)) {
            disableAutoCommit();
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            rollbackTransaction();
            throw new RuntimeException(exception);
        }
        finally {
            enableAutoCommit();
        }
    }


    private void disableAutoCommit() {
        try {
            connection.setAutoCommit(Boolean.FALSE);
        } catch (SQLException exception) {
            log.error("Can not disable autocommit", exception);
            throw new RuntimeException(exception);
        }
    }

    private void enableAutoCommit() {
        try {
            connection.setAutoCommit(Boolean.TRUE);
        } catch (SQLException exception) {
            log.error("Can not enable autocommit", exception);
            throw new RuntimeException(exception);
        }
    }

    private void rollbackTransaction() {
        try {
            connection.rollback();
        } catch (SQLException exception) {
            log.error("Can not rollback transaction", exception);
            throw new RuntimeException(exception);
        }
    }
}