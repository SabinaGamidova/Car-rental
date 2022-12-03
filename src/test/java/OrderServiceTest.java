import exception.CarRentalException;
import models.cars.Car;
import models.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CarRepository;
import repository.OrderRepository;
import services.order.OrderService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepositoryMock;
    @Mock
    private CarRepository carRepositoryMock;
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService(orderRepositoryMock, carRepositoryMock);
        assertNotNull(orderRepositoryMock);
        assertNotNull(carRepositoryMock);
        assertNotNull(orderService);
    }


    @Test
    public void whenFormCorrectOrder_thenVerifyOrderIsFormed() {
        Order order = buildOrder(UUID.randomUUID());
        Car car = buildCar(order.getCarId());

        when(orderRepositoryMock.insert(order)).thenReturn(order);
        when(carRepositoryMock.getById(car.getId())).thenReturn(car);

        Order response = orderService.formOrder(order);

        assertNotNull(response);
        assertEquals(order, response);

        verify(orderRepositoryMock).insert(order);
        verify(carRepositoryMock).getById(car.getId());
        verifyNoMoreInteractions(orderRepositoryMock);
        verifyNoMoreInteractions(carRepositoryMock);
    }


    @Test
    public void whenFormNullOrder_thenVerifyException() {
        String exceptionMessage = "Order must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.formOrder(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenFormInvalidPriceOrder_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());
        Car car = buildCar(order.getCarId());
        car.setPrice(-10);
        car.setDeposit(-100);

        String exceptionMessage = "Your order has invalid data";

        when(carRepositoryMock.getById(car.getId())).thenReturn(car);
        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.formOrder(order));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(carRepositoryMock).getById(car.getId());
        verifyNoMoreInteractions(carRepositoryMock);
        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenInsertCorrectOrder_thenVerifyOrder() {
        Order order = buildOrder(UUID.randomUUID());

        when(orderRepositoryMock.insert(order)).thenReturn(order);
        Order response = orderService.insert(order);

        assertNotNull(response);
        assertEquals(order, response);

        verify(orderRepositoryMock).insert(order);
        verifyNoMoreInteractions(orderRepositoryMock);
    }


    @Test
    public void whenInsertNullOrder_thenVerifyException() {
        String exceptionMessage = "Order must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.insert(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenInsertOrderWithNullUserId_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());
        order.setClientId(null);
        String exceptionMessage = "Order must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.insert(order));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenInsertOrderWithNullCarId_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());
        order.setCarId(null);
        String exceptionMessage = "Order must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.insert(order));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenInsertOrderWithToSmallerThanToday_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -2);
        order.setTo(calendar.getTime());

        String exceptionMessage = "Date must be greater or equal than today.";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.insert(order));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenInsertOrderWithFromSmallerThanToday_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -2);
        order.setFrom(calendar.getTime());

        String exceptionMessage = "Date must be greater or equal than today.";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.insert(order));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenInsertOrderWithFromGreaterThanToDate_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 4);
        order.setFrom(calendar.getTime());

        String exceptionMessage = "Date to must be greater than from.";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.insert(order));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetAll_thenVerifyListOfOrders() {
        List<Order> orders = buildOrderList();

        when(orderRepositoryMock.getAll()).thenReturn(orders);

        List<Order> response = orderService.getAll();

        assertNotNull(response);
        assertEquals(orders.size(), response.size());
        assertArrayEquals(orders.toArray(), response.toArray());

        verify(orderRepositoryMock).getAll();
        verifyNoMoreInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetByCorrectId_thenVerifyOrder() {
        UUID id = UUID.randomUUID();
        Order order = buildOrder(UUID.randomUUID());

        when(orderRepositoryMock.getById(id)).thenReturn(order);
        Order response = orderService.getById(id);

        assertNotNull(response);
        assertEquals(order, response);

        verify(orderRepositoryMock).getById(id);
        verifyNoMoreInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetByInvalidId_thenVerifyException() {
        String exceptionMessage = "Order id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.getById(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetByAbsentId_thenVerifyException() {
        UUID absentId = UUID.randomUUID();
        String exceptionMessage = String.format("Order with id %s not found", absentId);

        when(orderRepositoryMock.getById(absentId)).thenThrow(new CarRentalException(exceptionMessage));
        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.getById(absentId));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(orderRepositoryMock).getById(absentId);
        verifyNoMoreInteractions(orderRepositoryMock);
    }


    @Test
    public void whenUpdateCorrectOrder_thenVerifyOrder() {
        Order order = buildOrder(UUID.randomUUID());
        Car car = buildCar(order.getCarId());
        when(orderRepositoryMock.isExistById(order.getId())).thenReturn(Boolean.TRUE);
        when(orderRepositoryMock.getById(order.getId())).thenReturn(order);
        when(carRepositoryMock.getById(order.getCarId())).thenReturn(car);
        when(orderRepositoryMock.update(order)).thenReturn(order);

        Order response = orderService.update(order);
        assertNotNull(response);

        assertEquals(order, response);

        verify(orderRepositoryMock).isExistById(order.getId());
        verify(orderRepositoryMock).getById(order.getId());
        verify(carRepositoryMock).getById(order.getCarId());
        verify(orderRepositoryMock).update(order);
        verifyNoMoreInteractions(orderRepositoryMock);
    }


    @Test
    public void whenUpdateWithNullOrder_thenVerifyException() {
        String exceptionMessage = String.format("Order must be NOT null");

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.update(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenUpdateWithNullUserId_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());
        order.setClientId(null);

        String exceptionMessage = String.format("Order must be NOT null");

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.update(order));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenUpdateWithNullCarId_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());
        order.setCarId(null);

        String exceptionMessage = String.format("Order must be NOT null");

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.update(order));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenUpdateWithToSmallerThanToday_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -2);
        order.setTo(calendar.getTime());

        String exceptionMessage = "Date must be greater or equal than today.";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.update(order));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenUpdateWithFromSmallerThanToday_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -2);
        order.setFrom(calendar.getTime());

        String exceptionMessage = "Date must be greater or equal than today.";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.update(order));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenUpdateWithFromGreaterThanTo_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 4);
        order.setFrom(calendar.getTime());

        String exceptionMessage = "Date to must be greater than from.";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.update(order));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenUpdateOrderWithAbsentId_thenVerifyException() {
        Order absentOrder = buildOrder(UUID.randomUUID());
        String exceptionMessage = String.format("Order with id %s not found", absentOrder.getId());

        when(orderRepositoryMock.isExistById(absentOrder.getId())).thenReturn(Boolean.FALSE);

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.update(absentOrder));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(orderRepositoryMock).isExistById(absentOrder.getId());
        verify(orderRepositoryMock, never()).getById(absentOrder.getId());
        verify(orderRepositoryMock, never()).update(absentOrder);
        verifyNoMoreInteractions(orderRepositoryMock);
    }


    @Test
    public void whenDeleteCorrectOrder_thenVerifyTrue() {
        Order order = buildOrder(UUID.randomUUID());

        when(orderRepositoryMock.getById(order.getId())).thenReturn(order);
        when(orderRepositoryMock.update(order)).thenReturn(order);

        boolean result = orderService.delete(order.getId());

        assertTrue(result);

        verify(orderRepositoryMock).getById(order.getId());
        verify(orderRepositoryMock).update(order);
        verifyNoMoreInteractions(orderRepositoryMock);
    }


    @Test
    public void whenDeleteWithInvalidId_thenVerifyException() {
        String exceptionMessage = "Can't remove the order, its id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.delete(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(orderRepositoryMock, never()).getById(any(UUID.class));
        verify(orderRepositoryMock, never()).update(any(Order.class));
        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenDeleteWithAbsentId_thenVerifyGetByIdException() {
        UUID absentId = UUID.randomUUID();
        String exceptionMessage = String.format("Order with id %s not found", absentId);

        when(orderRepositoryMock.getById(absentId)).thenThrow(new CarRentalException(exceptionMessage));

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.delete(absentId));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(orderRepositoryMock).getById(absentId);
        verify(orderRepositoryMock, never()).update(any(Order.class));
        verifyNoMoreInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetAllByUserId_thenVerifyListOfOrders() {
        UUID id = UUID.randomUUID();
        List<Order> orders = buildOrderList();

        when(orderRepositoryMock.getByUserId(id)).thenReturn(orders);

        List<Order> response = orderService.getOrdersByUserId(id);

        assertNotNull(response);
        assertEquals(orders.size(), response.size());
        assertArrayEquals(orders.toArray(), response.toArray());

        verify(orderRepositoryMock).getByUserId(id);
        verifyNoMoreInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetAllWithNullUserId_thenVerifyException() {
        String exceptionMessage = "User id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.getOrdersByUserId(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetAllBetweenDates_thenVerifyListOfOrders() {
        Order order = buildOrder(UUID.randomUUID());
        List<Order> orders = buildOrderList();

        when(orderRepositoryMock.getOrdersBetweenDates(order.getFrom(), order.getTo())).thenReturn(orders);

        List<Order> response = orderService.getBetweenDates(order.getFrom(), order.getTo());

        assertNotNull(response);
        assertEquals(orders.size(), response.size());
        assertArrayEquals(orders.toArray(), response.toArray());

        verify(orderRepositoryMock).getOrdersBetweenDates(order.getFrom(), order.getTo());
        verifyNoMoreInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetOrdersBetweenDatesWithToSmallerThanToday_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -2);
        order.setTo(calendar.getTime());

        String exceptionMessage = "Date must be greater or equal than today.";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.getBetweenDates(order.getFrom(), order.getTo()));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetOrdersBetweenDatesWithFromSmallerThanToday_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -2);
        order.setFrom(calendar.getTime());

        String exceptionMessage = "Date must be greater or equal than today.";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.getBetweenDates(order.getFrom(), order.getTo()));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetOrdersBetweenDatesWithFromGreaterThanTo_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 4);
        order.setFrom(calendar.getTime());

        String exceptionMessage = "Date to must be greater than from.";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.getBetweenDates(order.getFrom(), order.getTo()));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetUserOrdersBetweenDates_thenVerifyListOfOrders() {
        Order order = buildOrder(UUID.randomUUID());
        List<Order> orders = buildOrderList();

        when(orderRepositoryMock.getUserOrdersBetweenDates(order.getClientId(), order.getFrom(), order.getTo())).thenReturn(orders);

        List<Order> response = orderService.getUserOrdersBetweenDates(order.getClientId(), order.getFrom(), order.getTo());

        assertNotNull(response);
        assertEquals(orders.size(), response.size());
        assertArrayEquals(orders.toArray(), response.toArray());

        verify(orderRepositoryMock).getUserOrdersBetweenDates(order.getClientId(), order.getFrom(), order.getTo());
        verifyNoMoreInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetUserOrdersBetweenDatesWithNullUserId_thenVerifyException() {
        String exceptionMessage = "User id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.getUserOrdersBetweenDates(null, new Date(), new Date()));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetUserOrdersBetweenDatesWithToSmallerThanToday_thenVerifyException() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -2);
        Date to = calendar.getTime();

        String exceptionMessage = "Date must be greater or equal than today.";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.getUserOrdersBetweenDates(UUID.randomUUID(), new Date(), to));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetUserOrdersBetweenDatesWithFromSmallerThanToday_thenVerifyException() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -2);
        Date from = calendar.getTime();

        String exceptionMessage = "Date must be greater or equal than today.";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.getUserOrdersBetweenDates(UUID.randomUUID(), from, new Date()));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    @Test
    public void whenGetUserOrdersBetweenDatesWithFromGreaterThanTo_thenVerifyException() {
        Order order = buildOrder(UUID.randomUUID());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 4);
        order.setFrom(calendar.getTime());

        String exceptionMessage = "Date to must be greater than from.";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> orderService.getUserOrdersBetweenDates(order.getClientId(), order.getFrom(), order.getTo()));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(orderRepositoryMock);
    }


    private Order buildOrder(UUID id) {
        Order order = new Order();
        order.setId(id);
        order.setClientId(UUID.randomUUID());
        order.setCarId(UUID.randomUUID());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 1);
        order.setFrom(calendar.getTime());
        calendar.add(Calendar.MONTH, 2);
        order.setTo(calendar.getTime());
        order.setTotalPrice(100D);
        order.setStatus(Boolean.TRUE);
        return order;
    }


    private Car buildCar(UUID id) {
        Car car = new Car();
        car.setId(id);
        car.setNumber("carNumber");
        car.setBrand("carBrand");
        car.setModel("carModel");
        car.setCarTypeId(UUID.randomUUID());
        car.setComfortId(UUID.randomUUID());
        car.setEngineId(UUID.randomUUID());
        car.setPrice(100D);
        car.setDeposit(100D);
        car.setStatus(Boolean.TRUE);
        return car;
    }


    private List<Order> buildOrderList() {
        return List.of(
                buildOrder(UUID.randomUUID()),
                buildOrder(UUID.randomUUID()),
                buildOrder(UUID.randomUUID()));
    }
}