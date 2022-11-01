package repository;

import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
import models.order.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class OrderRepository {
    private final Connection connection;

    public Order insert(Order order) {
        String INSERT = "INSERT INTO \"order\"(client_id, car_id, from, to, total_price) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setObject(1, order.getClientId());
            statement.setObject(2, order.getCarId());
            statement.setTimestamp(3, getTimestamp(order.getFrom()));
            statement.setTimestamp(4, getTimestamp(order.getTo()));
            statement.setDouble(5, order.getTotalPrice());
            statement.execute();
            return order;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }


    public List<Order> getAll() {
        String GET_ALL = "SELECT * FROM \"order\" WHERE status";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL);
            List<Order> list = new ArrayList<>();
            while (resultSet.next()) {
                Order order = (Order) Mapper.mapSingleFromResultSet(resultSet, Order.class);
                list.add(order);
            }
            resultSet.close();
            return list;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public Order getById(UUID id) {
        String GET_BY_ID = "SELECT * FROM \"order\" WHERE id = ? AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Order order = (Order) Mapper.mapSingleFromResultSet(resultSet, Order.class);
                resultSet.close();
                return order;
            }
            resultSet.close();
            throw new CarRentalException(String.format("Order with id %s not found", id));
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public Order update(Order order) {
        return null;
    }

    public void delete(UUID id) {
        String INACTIVATE = "UPDATE \"order\" SET status = FALSE WHERE id = ? AND status";
        try (PreparedStatement statement = connection.prepareStatement(INACTIVATE)) {
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public List<Order> getByUserId(UUID userId) {
        String GET_BY_USER_ID = "SELECT * FROM \"order\" WHERE client_id = ? AND status";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_BY_USER_ID);
            List<Order> list = new ArrayList<>();
            while (resultSet.next()) {
                Order order = (Order) Mapper.mapSingleFromResultSet(resultSet, Order.class);
                list.add(order);
            }
            resultSet.close();
            return list;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public List<Order> getOrdersBetweenDates(Date from, Date to) { //Right condition???
        String GET_BY_USER_ID = "SELECT * FROM \"order\" " +
                "WHERE (from BETWEEN ? AND ? AND status) AND " +
                "(to BETWEEN ? AND ? AND status)";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_BY_USER_ID);
            List<Order> list = new ArrayList<>();
            while (resultSet.next()) {
                Order order = (Order) Mapper.mapSingleFromResultSet(resultSet, Order.class);
                list.add(order);
            }
            resultSet.close();
            return list;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public List<Order> getUserOrdersBetweenDates(UUID userId, Date from, Date to) {
        return null;
    }

    private Timestamp getTimestamp(java.util.Date date) {
        return date == null ? null : new java.sql.Timestamp(date.getTime());
    }
}
