package services.order;

import models.order.Order;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public class OrderService implements OrderInterface {


    @Override
    public Order insert(Order order) {
        return null;
    }

    @Override
    public List<Order> getAll() {
        return null;
    }

    @Override
    public Order getById(UUID id) {
        return null;
    }

    @Override
    public Order update(Order order) {
        return null;
    }

    @Override
    public void delete(UUID id) {
        return;
    }

    @Override
    public List<Order> getByUserId(UUID userId) {
        return null;
    }

    @Override
    public List<Order> getBetweenDates(Date from, Date to) {
        return null;
    }

    @Override
    public List<Order> getUserOrdersBetweenDates(UUID userId, Date from, Date to) {
        return null;
    }
}