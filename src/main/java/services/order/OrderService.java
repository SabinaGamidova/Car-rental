package services.order;

import connection.Transactionable;
import exception.CarRentalException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.cars.CarType;
import models.order.Order;
import org.apache.commons.lang3.StringUtils;
import repository.CarTypeRepository;
import repository.OrderRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class OrderService implements OrderInterface, Transactionable {
    OrderRepository orderRepository;

    @Override
    public Order insert(Order order) {
        log.info("Trying to insert new order");
        validateOrder(order);
        Order o = wrapIntoTransaction(() -> orderRepository.insert(order));
        log.info("Order has been inserted successfully");
        return o;
    }

    @Override
    public List<Order> getAll() {
        log.info("Trying to get all orders");
        return orderRepository.getAll();
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
    public boolean delete(UUID id) {
        return true;
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

    private void validateOrder(Order order) {
        if (Objects.isNull(order)) {
            log.error("Order is null {}", order);
            throw new CarRentalException("Order must be NOT null");
        }
        if (Objects.isNull(order.getClientId())) {
            log.error("Client id in order is null {}", order.getClientId());
            throw new CarRentalException("Client id must be NOT null");
        }
        if (Objects.isNull(order.getCarId())) {
            log.error("Car id in order is null {}", order.getCarId());
            throw new CarRentalException("Car id must be NOT null");
        }
        if (order.getTotalPrice() <= 0 || order.getTotalPrice() >= Integer.MAX_VALUE) {
            log.error("Order properties have invalid format {}", order);
            throw new CarRentalException("Order has invalid data");
        }
    }
}