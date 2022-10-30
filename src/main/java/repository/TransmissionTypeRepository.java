package repository;


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

    public void insert(TransmissionType transmissionType) {
        String INSERT = "INSERT INTO transmission_type(name) VALUES (?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            disableAutoCommit();

            statement.setString(1, transmissionType.getName());

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

    public List<TransmissionType> getAll() {//TODO check
        String GET_ALL = "SELECT * FROM transmission_type WHERE status";
        try (Statement statement = connection.createStatement()) {
            disableAutoCommit();
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
            rollbackTransaction();
            throw new RuntimeException(exception);
        }
        finally {
            enableAutoCommit();
        }
    }

    public TransmissionType getById(UUID id) {
        String GET_BY_ID = "SELECT * FROM transmission_type WHERE id=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            disableAutoCommit();
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                TransmissionType transmissionType =
                        (TransmissionType) Mapper.mapSingleFromResultSet(resultSet, TransmissionType.class);
                resultSet.close();
                return transmissionType;
            }

            resultSet.close();
            throw new RuntimeException(String.format("Transmission type with id %s not found", id));
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            rollbackTransaction();
            throw new RuntimeException(exception);
        }
        finally {
            enableAutoCommit();
        }
    }

    public void update(TransmissionType transmissionType) {
        /*UPDATE transmission_type SET name='?' WHERE id='?' AND status;*/
    }

    public void delete(UUID id) {
        String INACTIVATE = "UPDATE transmission_type SET status=FALSE WHERE id=? AND status;";
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