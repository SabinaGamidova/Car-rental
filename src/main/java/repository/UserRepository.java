package repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
import models.people.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class UserRepository {
    private final Connection connection;

    public void insert(User user) {
        String INSERT = "INSERT INTO \"user\"(name, surname, patronymic, date_of_birth, email, password, role_id)VALUES(?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            disableAutoCommit();

            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getPatronymic());
            statement.setTimestamp(4, getTimestamp(user.getDateOfBirth()));
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPassword());
            statement.setObject(7, user.getRoleId());

            if (statement.execute()) {
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


    private Timestamp getTimestamp(java.util.Date date) {
        return date == null ? null : new java.sql.Timestamp(date.getTime());
    }


    public List<User> getAll() { //TODO check
        String GET_ALL = "SELECT * FROM \"user\" WHERE status";
        try (Statement statement = connection.createStatement()) {
            disableAutoCommit();
            ResultSet resultSet = statement.executeQuery(GET_ALL);

            List<User> list = new ArrayList<>();
            while (resultSet.next()) {
                User user = (User) Mapper.mapSingleFromResultSet(resultSet, User.class);
                list.add(user);
            }

            resultSet.close();
            return list;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            rollbackTransaction();
            throw new RuntimeException(exception);
        } finally {
            enableAutoCommit();
        }
    }

    public User getById(UUID id) {
        //SELECT * FROM "user" WHERE id='' AND status
        String GET_BY_ID = "SELECT * FROM \"user\" WHERE id=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            disableAutoCommit();
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = (User) Mapper.mapSingleFromResultSet(resultSet, User.class);
                resultSet.close();
                return user;
            }

            resultSet.close();
            throw new RuntimeException(String.format("User with id %s not found", id));
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            rollbackTransaction();
            throw new RuntimeException(exception);
        } finally {
            enableAutoCommit();
        }
    }

    public void update(User user) {
        /*UPDATE "user" SET name = '?' WHERE id = '?' AND status;*/
        /*UPDATE "user" SET surname = '?' WHERE id = '?' AND status;*/
        /*UPDATE "user" SET patronymic = '?' WHERE id = '?' AND status;*/
        /*UPDATE "user" SET date_of_birth = '?' WHERE id = '?' AND status;*/
        /*UPDATE "user" SET email = '?' WHERE id = '?' AND status;*/
        /*UPDATE "user" SET password = '?' WHERE id = '?' AND status;*/
        /*UPDATE "user" SET role_id = '?' WHERE id = '?' AND status;*/
    }

    public void delete(UUID id) {
        /*UPDATE "user" SET status=FALSE WHERE id='?' AND status;*/
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