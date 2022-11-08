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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class OrderRepository {
    private final Connection connection;

    public Order insert(Order order) {
        String INSERT = "INSERT INTO \"order\"(client_id, car_id, \"from\", \"to\", total_price) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {

            statement.setObject(1, order.getClientId());
            statement.setObject(2, order.getCarId());
            statement.setDate(3, new java.sql.Date(order.getFrom().getTime()));
            statement.setDate(4, new java.sql.Date(order.getTo().getTime()));
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
        String UPDATE = "UPDATE \"order\" SET client_id = ?, car_id = ?, \"from\" = ?, \"to\" = ?, total_price = ? WHERE id = ? AND status;";
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {

            statement.setObject(1, order.getClientId());
            statement.setObject(2, order.getCarId());
            statement.setDate(3, new java.sql.Date(order.getFrom().getTime()));
            statement.setDate(4, new java.sql.Date(order.getTo().getTime()));
            statement.setDouble(5, order.getTotalPrice());
            statement.setObject(6, order.getId());
            statement.execute();

            return order;
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

    public List<Order> getOrdersBetweenDates(Date from, Date to) {
        String GET_ORDER_BETWEEN_DATES = "SELECT * FROM \"order\" WHERE (\"from\" >= ? AND \"to\" <= ?) AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_ORDER_BETWEEN_DATES)) {

            statement.setDate(1, new java.sql.Date(from.getTime()));
            statement.setDate(2, new java.sql.Date(to.getTime()));

            ResultSet resultSet = statement.executeQuery(GET_ORDER_BETWEEN_DATES);

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
        String GET_USER_ORDERS_BETWEEN_DATES = "SELECT * FROM \"order\" WHERE client_id = ? AND (\"from\" >= ? AND \"to\" <= ?) AND status";
        try (PreparedStatement statement = connection.prepareStatement(GET_USER_ORDERS_BETWEEN_DATES)) {

            statement.setObject(1, userId);
            statement.setDate(2, new java.sql.Date(from.getTime()));
            statement.setDate(3, new java.sql.Date(to.getTime()));

            ResultSet resultSet = statement.executeQuery(GET_USER_ORDERS_BETWEEN_DATES);

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

    public boolean isExistById(UUID id) {
        String IS_EXIST = "SELECT EXISTS (SELECT 1 FROM \"order\" WHERE id=? AND status);";
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


    public boolean isUserExistById(UUID userId) {
        String IS_USER_EXIST = "SELECT EXISTS (SELECT 1 FROM \"order\" WHERE client_id=? AND status)";
        try (PreparedStatement statement = connection.prepareStatement(IS_USER_EXIST)) {
            statement.setObject(1, userId);

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


    public boolean isCarExistById(UUID carId) {
        String IS_CAR_EXIST = "SELECT EXISTS (SELECT 1 FROM \"order\" WHERE car_id=? AND status)";
        try (PreparedStatement statement = connection.prepareStatement(IS_CAR_EXIST)) {
            statement.setObject(1, carId);

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