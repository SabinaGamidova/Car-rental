package repository;

import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
import models.cars.Car;
import models.cars.CarComfort;
import models.cars.CarType;
import util.DateTimeUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CarRepository {
    private final Connection connection;


    public Car insert(Car car) {
        String INSERT = "INSERT INTO car(number, brand, model, car_type_id, car_comfort_id, engine_id, price, deposit)VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, car.getNumber());
            statement.setString(2, car.getBrand());
            statement.setString(3, car.getModel());
            statement.setObject(4, car.getCarTypeId());
            statement.setObject(5, car.getComfortId());
            statement.setObject(6, car.getEngineId());
            statement.setDouble(7, car.getPrice());
            statement.setDouble(8, car.getDeposit());
            statement.execute();
            return car;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public List<Car> getAll() {
        String GET_ALL = "SELECT * FROM car WHERE status";
        try (Statement statement = connection.createStatement()) {
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
            throw new CarRentalException(exception.getMessage());
        }
    }

    public Car getById(UUID id) {
        String GET_BY_ID = "SELECT * FROM car WHERE id=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Car car = (Car) Mapper.mapSingleFromResultSet(resultSet, Car.class);
                resultSet.close();
                return car;
            }

            resultSet.close();
            throw new CarRentalException(String.format("Car with id %s not found", id));
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public Car update(Car car) {
        String UPDATE = "UPDATE car SET number = ?, brand = ?, model = ?, car_type_id = ?, car_comfort_id = ?, engine_id = ?, price = ?, deposit = ?, status=? WHERE id = ? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, car.getNumber());
            statement.setString(2, car.getBrand());
            statement.setString(3, car.getModel());
            statement.setObject(4, car.getCarTypeId());
            statement.setObject(5, car.getComfortId());
            statement.setObject(6, car.getEngineId());
            statement.setDouble(7, car.getPrice());
            statement.setDouble(8, car.getDeposit());
            statement.setBoolean(9, car.isStatus());
            statement.setObject(10, car.getId());
            statement.execute();
            return car;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }


    public List<Car> getByCarType(UUID carTypeId) {
        String GET_BY_CAR_TYPE = "SELECT * FROM car WHERE car_type_id=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_CAR_TYPE)) {
            statement.setObject(1, carTypeId);
            ResultSet resultSet = statement.executeQuery();

            List<Car> list = new ArrayList<>();
            while (resultSet.next()) {
                Car car = (Car) Mapper.mapSingleFromResultSet(resultSet, Car.class);
                list.add(car);
            }

            resultSet.close();
            return list;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public List<Car> getByCarComfort(UUID carComfortId) {
        String GET_BY_CAR_COMFORT = "SELECT * FROM car WHERE car_comfort_id=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_CAR_COMFORT)) {
            statement.setObject(1, carComfortId);
            ResultSet resultSet = statement.executeQuery();

            List<Car> list = new ArrayList<>();
            while (resultSet.next()) {
                Car car = (Car) Mapper.mapSingleFromResultSet(resultSet, Car.class);
                list.add(car);
            }

            resultSet.close();
            return list;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }


    public boolean isExistById(UUID id) {
        String IS_EXIST = "SELECT EXISTS (SELECT 1 FROM car WHERE id=? AND status);";
        try (PreparedStatement statement = connection.prepareStatement(IS_EXIST)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            boolean isExist = Boolean.FALSE;
            if (resultSet.next()) {
                isExist = resultSet.getBoolean(1);
            }

            resultSet.close();
            return isExist;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public List<Car> getAvailable(Date from, Date to) {
        String GET_AVAILABLE = "SELECT DISTINCT car.* FROM car " +
                "LEFT JOIN \"order\" ON \"order\".car_id = car.id " +
                "WHERE (\"order\".\"to\" <= ?) OR (\"order\".\"from\" >= ?) " +
                "AND car.status " +
                "AND \"order\".status OR \"order\".status IS NULL " +
                "AND \"order\".\"to\" >= NOW() OR \"order\".\"to\" IS NULL;";
        try (PreparedStatement statement = connection.prepareStatement(GET_AVAILABLE)) {
            statement.setDate(1, DateTimeUtil.toSqlDate(from));
            statement.setDate(2, DateTimeUtil.toSqlDate(to));
            ResultSet resultSet = statement.executeQuery();

            List<Car> list = new ArrayList<>();
            while (resultSet.next()) {
                Car car = (Car) Mapper.mapSingleFromResultSet(resultSet, Car.class);
                list.add(car);
            }

            resultSet.close();
            return list;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }
}