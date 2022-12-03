package repository;

import exception.CarRentalException;
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

    public CarComfort insert(CarComfort carComfort) {
        String INSERT = "INSERT INTO car_comfort(name, description) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {

            statement.setString(1, carComfort.getName());
            statement.setString(2, carComfort.getDescription());

            statement.execute();
            return carComfort;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }


    public List<CarComfort> getAll() {
        String GET_ALL = "SELECT * FROM car_comfort WHERE status";
        try (Statement statement = connection.createStatement()) {
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
            throw new CarRentalException(exception.getMessage());
        }
    }

    public CarComfort getById(UUID id) {
        String GET_BY_ID = "SELECT * FROM car_comfort WHERE id=? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                CarComfort carComfort = (CarComfort) Mapper.mapSingleFromResultSet(resultSet, CarComfort.class);
                resultSet.close();
                return carComfort;
            }

            resultSet.close();
            throw new CarRentalException("Car comfort with id %s not found", id);
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }


    public CarComfort update(CarComfort carComfort) {
        String UPDATE = "UPDATE car_comfort SET name = ?, description = ?, status=? WHERE id = ? AND status";
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {

            statement.setString(1, carComfort.getName());
            statement.setString(2, carComfort.getDescription());
            statement.setBoolean(3, carComfort.isStatus());
            statement.setObject(4, carComfort.getId());

            statement.execute();
            return carComfort;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }


    public boolean isExistById(UUID id) {
        String IS_EXIST = "SELECT EXISTS (SELECT 1 FROM car_comfort WHERE id=? AND status);";
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
