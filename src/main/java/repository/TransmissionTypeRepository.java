package repository;


import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
import models.cars.TransmissionType;
import models.people.Role;

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
public class TransmissionTypeRepository {
    private final Connection connection;

    public TransmissionType insert(TransmissionType transmissionType) {
        String INSERT = "INSERT INTO transmission_type(name) VALUES (?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, transmissionType.getName());
            statement.execute();
            return transmissionType;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

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

    public TransmissionType getById(UUID id) {
        String GET_BY_ID = "SELECT * FROM transmission_type WHERE id=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                TransmissionType transmissionType =
                        (TransmissionType) Mapper.mapSingleFromResultSet(resultSet, TransmissionType.class);
                resultSet.close();
                return transmissionType;
            }
            resultSet.close();
            throw new CarRentalException(String.format("Transmission type with id %s not found", id));
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public TransmissionType update(TransmissionType transmissionType) {
        String UPDATE = "UPDATE transmission_type SET name=? WHERE id=? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, transmissionType.getName());
            statement.setObject(2, transmissionType.getId());
            statement.execute();
            return transmissionType;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public void delete(UUID id) {
        String INACTIVATE = "UPDATE transmission_type SET status=FALSE WHERE id=? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(INACTIVATE)) {
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }
}