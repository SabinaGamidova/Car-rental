package repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
import models.cars.Car;
import models.cars.CarComfort;
import models.cars.CarType;

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
public class CarRepository {
    private final Connection connection;


    public void insert(Car car) {
        String INSERT = "INSERT INTO car(number, brand, model, car_type_id, car_comfort_id, engine_id, price, deposit)VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            disableAutoCommit();

            statement.setString(1, car.getNumber());
            statement.setString(2, car.getBrand());
            statement.setString(3, car.getModel());
            statement.setObject(4, car.getCarTypeId());
            statement.setObject(5, car.getComfortId());
            statement.setObject(6, car.getEngineId());
            statement.setDouble(7, car.getPrice());
            statement.setDouble(8, car.getDeposit());

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

    public List<Car> getAll() { //TODO check
        /*SELECT * FROM car WHERE status*/
        String GET_ALL = "SELECT * FROM car WHERE status";
        try (Statement statement = connection.createStatement()) {
            disableAutoCommit();
            ResultSet resultSet = statement.executeQuery(GET_ALL);

            List<Car> list = new ArrayList<>();
            while (resultSet.next()) {
                Car car = (Car) Mapper.mapSingleFromResultSet(resultSet, Car.class);
                list.add(car);
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

    public Car getById(UUID id) {
        String GET_BY_ID = "SELECT * FROM car WHERE id=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            disableAutoCommit();
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Car car = (Car) Mapper.mapSingleFromResultSet(resultSet, Car.class);
                resultSet.close();
                return car;
            }

            resultSet.close();
            throw new RuntimeException(String.format("Car with id %s not found", id));
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            rollbackTransaction();
            throw new RuntimeException(exception);
        }
        finally {
            enableAutoCommit();
        }
    }

    public void update(Car car) {
        /*UPDATE car SET number = '?' WHERE id = '?' AND status;*/
        /*UPDATE car SET brand = '?' WHERE id = '?' AND status;*/
        /*UPDATE car SET model = '?' WHERE id = '?' AND status;*/
        /*UPDATE car SET car_type_id = '?' WHERE id = '?' AND status;*/
        /*UPDATE car SET car_comfort_id = '?' WHERE id = '?' AND status;*/
        /*UPDATE car SET engine_id = '?' WHERE id = '?' AND status;*/
        /*UPDATE car SET price = ? WHERE id = '?' AND status;*/
        /*UPDATE car SET deposit = ? WHERE id = '?' AND status;*/
    }

    public void delete(UUID id) {
        /*UPDATE car SET status = FALSE WHERE id = '?' AND status;*/
    }

    public List<Car> getByCarType(CarType carType) {
        /*SELECT * FROM car WHERE car_type_id='?' AND status*/
        return null;
    }

    public List<Car> getByCarComfort(CarComfort carComfort) {
        /*SELECT * FROM car WHERE car_comfort_id='?' AND status*/
        return null;
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
