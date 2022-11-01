package repository;

import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
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

    public FuelType insert(FuelType fuelType) {
        String INSERT = "INSERT INTO fuel_type(description) VALUES (?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, fuelType.getDescription());
            statement.execute();
            return fuelType;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }


    public List<FuelType> getAll() {//TODO check
        String GET_ALL = "SELECT * FROM fuel_type WHERE status";
        try (Statement statement = connection.createStatement()) {
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
            throw new CarRentalException(exception.getMessage());
        }
    }

    public FuelType getById(UUID id) {
        String GET_BY_ID = "SELECT * FROM fuel_type WHERE id=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                FuelType fuelType = (FuelType) Mapper.mapSingleFromResultSet(resultSet, FuelType.class);
                resultSet.close();
                return fuelType;
            }
            resultSet.close();
            throw new CarRentalException(String.format("Fuel type with id %s not found", id));
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public FuelType update(FuelType fuelType) {
        String UPDATE = "UPDATE fuel_type SET description = ? WHERE id = ? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, fuelType.getDescription());
            statement.setObject(2, fuelType.getId());
            statement.execute();
            return fuelType;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public void delete(UUID id) {
        String INACTIVATE = "UPDATE fuel_type SET status = FALSE WHERE id = ? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(INACTIVATE)) {
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }
}