package repository;

import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
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
public class CarTypeRepository {
    private final Connection connection;

    public CarType insert(CarType carType) {
        String INSERT = "INSERT INTO car_type(name) VALUES (?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, carType.getName());
            statement.execute();
            return carType;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }


    public List<CarType> getAll() {
        String GET_ALL = "SELECT * FROM car_type WHERE status";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL);
            List<CarType> list = new ArrayList<>();
            while (resultSet.next()) {
                CarType carType = (CarType) Mapper.mapSingleFromResultSet(resultSet, CarType.class);
                list.add(carType);
            }
            resultSet.close();
            return list;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public CarType getById(UUID id) {
        String GET_BY_ID = "SELECT * FROM car_type WHERE id = ? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                CarType carType = (CarType) Mapper.mapSingleFromResultSet(resultSet, CarType.class);
                resultSet.close();
                return carType;
            }

            resultSet.close();
            throw new CarRentalException(String.format("Car type with id %s not found", id));
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public CarType update(CarType carType) {
        String UPDATE = "UPDATE car_type SET name = ? WHERE id = ? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, carType.getName());
            statement.setObject(2, carType.getId());
            statement.execute();
            return carType;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }


    public boolean isExistById(UUID id) {
        String IS_EXIST = "SELECT EXISTS (SELECT 1 FROM car_type WHERE id=? AND status);";
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
}