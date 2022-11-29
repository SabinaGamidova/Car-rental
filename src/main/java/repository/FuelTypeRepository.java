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

    public List<FuelType> getAll() {
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

    public FuelType getByName(String name) {
        String GET_BY_NAME = "SELECT * FROM fuel_type WHERE name=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_NAME)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                FuelType fuelType = (FuelType) Mapper.mapSingleFromResultSet(resultSet, FuelType.class);
                resultSet.close();
                return fuelType;
            }
            resultSet.close();
            throw new CarRentalException("Fuel type with name %s not found", name);
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }
}