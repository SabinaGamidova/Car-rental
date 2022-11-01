package repository;

import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
import models.cars.CarType;
import models.cars.Engine;
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
public class RoleRepository {
    private final Connection connection;

    public Role insert(Role role) {
        String INSERT = "INSERT INTO role(name, description) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, role.getName());
            statement.setString(2, role.getDescription());
            statement.execute();
            return role;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public List<Role> getAll() {
        String GET_ALL = "SELECT * FROM role WHERE status";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL);
            List<Role> list = new ArrayList<>();
            while (resultSet.next()) {
                Role role = (Role) Mapper.mapSingleFromResultSet(resultSet, Role.class);
                list.add(role);
            }
            resultSet.close();
            return list;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public Role getById(UUID id) {
        String GET_BY_ID = "SELECT * FROM role WHERE id=? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Role role = (Role) Mapper.mapSingleFromResultSet(resultSet, Role.class);
                resultSet.close();
                return role;
            }
            resultSet.close();
            throw new CarRentalException(String.format("Car type with id %s not found", id));
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public Role update(Role role) {
        String UPDATE = "UPDATE role SET name = ?, description = ? WHERE id = ? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, role.getName());
            statement.setString(2, role.getDescription());
            statement.setObject(3, role.getId());
            statement.execute();
            return role;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public void delete(UUID id) {
        String INACTIVATE = "UPDATE role SET status = FALSE WHERE id = ? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(INACTIVATE)) {
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }
}