package repository;

import models.order.Order;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OrderRepository {
    public boolean create(Order order) {
        //INSERT INTO "order"(client_id, car_id, from, to, total_price) VALUES ('?', '?', ?, ?, ?);
        return true;
    }

    public Order getById(UUID id) {
        //SELECT * FROM "order" WHERE id = '?' AND status
        return null;
    }

    public List<Order> readAll() {
        //SELECT * FROM "order" WHERE status
        return null;
    }

    public Order update(Order order) {
        return null;
    }

    public void delete(UUID id) {
        //UPDATE "order" SET status = FALSE WHERE id = '?' AND status;
    }

    public List<Order> getByUserId(UUID userId) {
        //SELECT * FROM "order" WHERE client_id = '?' AND status
        return null;
    }

    public List<Order> getBetweenDates(Date from, Date to) {
        //SELECT * FROM "order" WHERE from BETWEEN ? AND ? AND status
        //SELECT * FROM "order" WHERE to BETWEEN ? AND ? AND status
        return null;
    }

    public List<Order> getUserOrdersBetweenDates(UUID userId, Date from, Date to) {

        return null;
    }
}
