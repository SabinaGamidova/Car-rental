package repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
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
public class FuelTypeRepository {
    private final Connection connection;

    public void insert(FuelType fuelType) {
        String INSERT = "INSERT INTO fuel_type(description) VALUES (?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            disableAutoCommit();

            statement.setString(1, fuelType.getDescription());
            statement.addBatch();

            int[] result = statement.executeBatch();
            for(int singleResult : result) {
                if(singleResult != 1) {
                    rollbackTransaction();
                    break;
                }
            }
            statement.clearBatch();

        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            rollbackTransaction();
            throw new RuntimeException(exception);
        } finally {
            enableAutoCommit();
        }
    }


    public List<FuelType> getAll() {//TODO check
        String GET_ALL = "SELECT * FROM fuel_type WHERE status";
        try (Statement statement = connection.createStatement()) {
            disableAutoCommit();
            ResultSet resultSet = statement.executeQuery(GET_ALL);

            List<FuelType> list = new ArrayList<>();
            while (resultSet.next()) {
                FuelType fuelType = (FuelType) Mapper.mapSingleFromResultSet(resultSet, FuelType.class);
                list.add(fuelType);
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

    public FuelType getById(UUID id) {
        String GET_BY_ID = "SELECT * FROM fuel_type WHERE id=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            disableAutoCommit();
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                FuelType fuelType = (FuelType) Mapper.mapSingleFromResultSet(resultSet, FuelType.class);
                resultSet.close();
                return fuelType;
            }

            resultSet.close();
            throw new RuntimeException(String.format("Fuel type with id %s not found", id));
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            rollbackTransaction();
            throw new RuntimeException(exception);
        }
        finally {
            enableAutoCommit();
        }
    }

    public void update(FuelType fuelType) {
        /*UPDATE fuel_type SET description = '?' WHERE id = '?' AND status;*/
    }

    public void delete(UUID id) {
        /*UPDATE fuel_type SET status = FALSE WHERE id = '?' AND status;*/
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