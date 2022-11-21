package services.order;

import models.order.Order;
import services.CrudGenericInterface;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface OrderInterface extends CrudGenericInterface<Order>{
    Order formOrder(Order order);

    List<Order> getByUserId(UUID userId);

    List<Order> getBetweenDates(Date from, Date to);

    List<Order> getUserOrdersBetweenDates(UUID userId, Date from, Date to);
}
