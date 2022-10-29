package repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
import models.cars.CarComfort;

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
public class CarComfortRepository {
    private final Connection connection;

    public void insert(CarComfort carComfort) {
        String INSERT = "INSERT INTO car_comfort(name, description) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            disableAutoCommit();

            statement.setString(1, carComfort.getName());
            statement.setString(2, carComfort.getDescription());

            if(!statement.execute()) {
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


    public List<CarComfort> getAll() {
        String GET_ALL = "SELECT * FROM car_comfort WHERE status";
        try (Statement statement = connection.createStatement()) {
            disableAutoCommit();
            ResultSet resultSet = statement.executeQuery(GET_ALL);

            List<CarComfort> list = new ArrayList<>();
            while (resultSet.next()) {
                CarComfort carComfort = (CarComfort) Mapper.mapSingleFromResultSet(resultSet, CarComfort.class);
                list.add(carComfort);
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

    public CarComfort getById(UUID id) {
        String GET_BY_ID = "SELECT * FROM car_comfort WHERE id=? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            disableAutoCommit();
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                CarComfort carComfort = (CarComfort)Mapper.mapSingleFromResultSet(resultSet, CarComfort.class);
                resultSet.close();
                return carComfort;
            }

            resultSet.close();
            throw new RuntimeException(String.format("Car comfort with id %s not found", id));
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            rollbackTransaction();
            throw new RuntimeException(exception);
        }
        finally {
            enableAutoCommit();
        }
    }


    public void update(CarComfort carComfort) {
        String UPDATE = "UPDATE car_comfort SET name = ?, description = ? WHERE id = ? AND status";
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            disableAutoCommit();

            statement.setString(1, carComfort.getName());
            statement.setString(2, carComfort.getDescription());
            statement.setObject(3, carComfort.getId());

                if(!statement.execute()) {
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
        String INACTIVATE = "UPDATE car_comfort SET status = FALSE WHERE id=? AND status;";
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
