package repository;


import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
import models.cars.TransmissionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TransmissionTypeRepository {
    private final Connection connection;

    public List<TransmissionType> getAll() {
        String GET_ALL = "SELECT * FROM transmission_type WHERE status";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL);
            List<TransmissionType> list = new ArrayList<>();
            while (resultSet.next()) {
                TransmissionType transmissionType = (TransmissionType) Mapper.mapSingleFromResultSet(resultSet, TransmissionType.class);
                list.add(transmissionType);
            }
            resultSet.close();
            return list;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public TransmissionType getByName(String name) {
        String GET_BY_NAME = "SELECT * FROM transmission_type WHERE name=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_NAME)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                TransmissionType transmissionType =
                        (TransmissionType) Mapper.mapSingleFromResultSet(resultSet, TransmissionType.class);
                resultSet.close();
                return transmissionType;
            }
            resultSet.close();
            throw new CarRentalException("Transmission type with id %s not found", name);
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }
}