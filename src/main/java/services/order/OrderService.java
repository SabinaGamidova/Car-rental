package services.order;

import connection.Transactionable;
import exception.CarRentalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.cars.Car;
import models.order.Order;
import repository.CarRepository;
import repository.OrderRepository;
import util.DateTimeUtil;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class OrderService implements OrderInterface, Transactionable {
    private final OrderRepository orderRepository;
    private final CarRepository carRepository;

    @Override
    public Order formOrder(Order order) {
        log.info("Trying to form new order");
        validateOrder(order);
        double totalPrice = calculateOrderTotalPrice(order.getCarId(), order.getFrom(), order.getTo());
        order.setTotalPrice(totalPrice);
        Order formedOrder = this.insert(order);
        log.info("Order has been formed successfully");
        return formedOrder;
    }

    @Override
    public Order insert(Order order) {
        log.info("Trying to insert new order");
        validateOrder(order);
        return wrapIntoTransaction(() -> {
            Order insertedOrder = orderRepository.insert(order);
            log.info("Order has been inserted successfully");
            return insertedOrder;
        });
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.getAll();
    }

    @Override
    public Order getById(UUID id) {
        if (Objects.isNull(id)) {
            log.error("Can not find order. Order id must be not null");
            throw new CarRentalException("Order id must be NOT null");
        }
        return orderRepository.getById(id);
    }

    @Override
    public Order update(Order order) {
        log.info("Trying to update order {}", order);
        validateOrder(order);
        if (!orderRepository.isExistById(order.getId())) {
            log.warn("Order with id {} not found", order.getId());
            throw new CarRentalException("Order with id %s not found", order.getId());
        }
        return wrapIntoTransaction(() -> {
            Order orderToUpdate = orderRepository.getById(order.getId());
            orderToUpdate.setFrom(order.getFrom());
            orderToUpdate.setTo(order.getTo());
            double totalPrice = calculateOrderTotalPrice(order.getCarId(), order.getFrom(), order.getTo());
            orderToUpdate.setTotalPrice(totalPrice);
            orderRepository.update(orderToUpdate);
            log.info("Order has been updated successfully");
            return orderToUpdate;
        });
    }


    private double calculateOrderTotalPrice(UUID carId, Date from, Date to) {
        long daysAmount = Duration
                .between(from.toInstant(), to.toInstant())
                .toDays();
        Car car = carRepository.getById(carId);
        double totalPrice = daysAmount * car.getPrice() + car.getDeposit();
        if (totalPrice <= 0 || totalPrice > OrderMaxPrice.MAX_PRICE.getValue()) {
            log.error("Order properties have invalid format {}", totalPrice);
            throw new CarRentalException("Your order has invalid data");
        }
        return totalPrice;
    }


    @Override
    public boolean delete(UUID id) {
        if (Objects.isNull(id)) {
            log.error("Can not find order. Order id must be not null");
            throw new CarRentalException("Can't remove the order, its id must be NOT null");
        }
        return wrapIntoTransaction(() -> {
            Order order = orderRepository.getById(id);
            order.setStatus(Boolean.FALSE);
            order = orderRepository.update(order);
            return order.isStatus() != Boolean.TRUE;
        });
    }

    @Override
    public List<Order> getOrdersByUserId(UUID userId) {
        if (Objects.isNull(userId)) {
            log.error("Can not find orders. User id must be not null");
            throw new CarRentalException("User id must be NOT null");
        }
        return orderRepository.getByUserId(userId);
    }

    @Override
    public List<Order> getBetweenDates(Date from, Date to) {
        DateTimeUtil.validateWithToday(from);
        DateTimeUtil.validateWithToday(to);
        DateTimeUtil.validateDates(from, to);
        return orderRepository.getOrdersBetweenDates(from, to);
    }

    @Override
    public List<Order> getUserOrdersBetweenDates(UUID userId, Date from, Date to) {
        if (Objects.isNull(userId)) {
            log.error("Can not find orders. User id must be not null");
            throw new CarRentalException("User id must be NOT null");
        }
        DateTimeUtil.validateWithToday(from);
        DateTimeUtil.validateWithToday(to);
        DateTimeUtil.validateDates(from, to);
        return orderRepository.getUserOrdersBetweenDates(userId, from, to);
    }


    private void validateOrder(Order order) {
        if (Objects.isNull(order) || Objects.isNull(order.getClientId()) || Objects.isNull(order.getCarId())) {
            log.error("Order is null {}", order);
            throw new CarRentalException("Order must be NOT null");
        }
        DateTimeUtil.validateWithToday(order.getFrom());
        DateTimeUtil.validateWithToday(order.getTo());
        DateTimeUtil.validateDates(order.getFrom(), order.getTo());
    }
}