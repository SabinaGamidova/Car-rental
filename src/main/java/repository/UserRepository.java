package repository;

import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
import models.people.User;

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
public class UserRepository {
    private final Connection connection;

    public User insert(User user) {
        String INSERT = "INSERT INTO \"user\"(name, surname, patronymic, date_of_birth, email, password, role_id)VALUES(?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getPatronymic());
            statement.setDate(4, user.getDateOfBirth());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPassword());
            statement.setObject(7, user.getRoleId());
            statement.execute();
            return user;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }


    public List<User> getAll() {
        String GET_ALL = "SELECT * FROM \"user\" WHERE status";
        try (Statement statement = connection.createStatement()) {
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
            throw new CarRentalException(exception.getMessage());
        }
    }

    public User getById(UUID id) {
        String GET_BY_ID = "SELECT * FROM \"user\" WHERE id=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = (User) Mapper.mapSingleFromResultSet(resultSet, User.class);
                resultSet.close();
                return user;
            }
            resultSet.close();
            throw new CarRentalException(String.format("User with id %s not found", id));
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public User update(User user) {
        String UPDATE = "UPDATE \"user\" SET name = ?, " +
                "surname = ?, patronymic = ?, " +
                "date_of_birth = ?, email = ?, " +
                "password = ?, role_id = ? WHERE id = ? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getPatronymic());
            statement.setDate(4, user.getDateOfBirth());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPassword());
            statement.setObject(7, user.getRoleId());
            statement.execute();
            return user;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public void delete(UUID id) {
        String INACTIVATE = "UPDATE \"user\" SET status=FALSE WHERE id=? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(INACTIVATE)) {
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }
}