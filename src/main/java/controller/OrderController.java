package controller;

import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import models.cars.Car;
import models.order.Order;
import models.people.User;
import models.session.Session;
import services.car.CarService;
import services.order.OrderService;
import services.session.SessionService;
import services.user.UserService;
import util.DateTimeUtil;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static exception.ExceptionHandler.handleException;

@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CarService carService;
    private final UserService userService;
    private final SessionService sessionService;
    private final Scanner scanner;


    public void programInterface() {
        handleException(() -> {
            Session session = sessionService.getActive();
            if (userService.isManager(session.getUserId())) {
                managerInterface();
            } else {
                userInterface();
            }
        });
    }


    private void managerInterface() {
        while (true) {
            if (sessionService.isUserAuthenticated()) {
                System.out.println("\nChoose item:\n1 - Form order\n" +
                        "2 - Get all orders\n3 - Update order\n" +
                        "4 - Delete order\n5 - Get your orders\n" +
                        "6 - Get orders between dates\n" +
                        "7 - Get your orders between dates\n8 - Return");
                int choose = Integer.parseInt(scanner.nextLine());
                switch (choose) {
                    case 1 -> formOrder();
                    case 2 -> getAll();
                    case 3 -> chooseAndUpdateForUser();
                    case 4 -> deleteOrder();
                    case 5 -> getOrdersByUserId();
                    case 6 -> getOrdersBetweenDates();
                    case 7 -> getUserOrdersBetweenDates();
                    case 8 -> {
                        return;
                    }
                    default -> System.out.println("\nEntered incorrect data");
                }
            } else {
                return;
            }
        }
    }


    private void userInterface() {
        while (true) {
            if (sessionService.isUserAuthenticated()) {
                System.out.println("\nChoose item:\n1 - Form order\n" +
                        "2 - Update order\n" +
                        "3 - Delete order\n4 - Get your orders\n" +
                        "5 - Get your orders between dates\n6 - Return");
                int choose = Integer.parseInt(scanner.nextLine());
                switch (choose) {
                    case 1 -> formOrder();
                    case 2 -> chooseAndUpdateForUser();
                    case 3 -> deleteOrder();
                    case 4 -> getOrdersByUserId();
                    case 5 -> getUserOrdersBetweenDates();
                    case 6 -> {
                        return;
                    }
                    default -> System.out.println("\nEntered incorrect data");
                }
            } else {
                return;
            }
        }
    }


    private void formOrder() {
        handleException(() -> {
            System.out.println("\nEnter order date FROM in format " + DateTimeUtil.DATE_PATTERN + ":");
            Date dateFrom = DateTimeUtil.parseFromString(scanner.nextLine());
            System.out.println("\nEnter order date TO in format " + DateTimeUtil.DATE_PATTERN + ":");
            Date dateTo = DateTimeUtil.parseFromString(scanner.nextLine());
            Car car = chooseCarByPosition(dateFrom, dateTo);
            UUID userId = sessionService.getActive().getUserId();

            Order order = orderService.formOrder(Order.builder()
                    .clientId(userId)
                    .carId(car.getId())
                    .from(dateFrom)
                    .to(dateTo)
                    .build());
            System.out.println("\nOrder has been formed successfully");
            System.out.println(order.toString());
        });
    }


    private void chooseAndUpdateForUser() {
        int choose;

        Session curSession = sessionService.getActive();
        User user = userService.getById(curSession.getUserId());
        List<Order> orders = orderService.getOrdersByUserId(user.getId());
        System.out.println();
        Order order = chooseOrderFromOrdersList(orders);

        System.out.println("\n1 - Date from\n2 - Date to\n3 - Return\n");
        choose = Integer.parseInt(scanner.nextLine());
        switch (choose) {
            case 1 -> {
                System.out.println("\nEnter new date FROM in format " + DateTimeUtil.DATE_PATTERN + ":");
                Date dateFrom = DateTimeUtil.parseFromString(scanner.nextLine());
                order.setFrom(dateFrom);
            }
            case 2 -> {
                System.out.println("\nEnter new date TO in format " + DateTimeUtil.DATE_PATTERN + ":");
                Date dateTo = DateTimeUtil.parseFromString(scanner.nextLine());
                order.setTo(dateTo);
            }
            case 3 -> {
                return;
            }
            default -> {
                System.out.println("\nYou entered invalid data\n");
                System.out.println("\nOrder was NOT updated");
                return;
            }
        }
        orderService.update(order);
        System.out.println("\nOrder updated successfully");
    }


    private void getAll() {
        handleException(() -> {
            AtomicInteger counter = new AtomicInteger(1);
            System.out.println();
            List<Order> orders = orderService.getAll();
            if (orders.isEmpty()) {
                throw new CarRentalException("\nNo orders exist yet");
            }
            orders.forEach(order ->
                    System.out.println("#" + counter.getAndIncrement() +
                            getCarInfoById(order.getCarId()) +
                            getUserInfoById(order.getClientId()) + order.toShortString()));
        });
    }


    private void getOrdersByUserId() {
        handleException(() -> {
            AtomicInteger counter = new AtomicInteger(1);
            Session session = sessionService.getActive();
            System.out.println();
            orderService.getOrdersByUserId(session.getUserId()).forEach(order ->
                    System.out.println("#" + counter.getAndIncrement() +
                            getCarInfoById(order.getCarId()) + order.toShortString()));
        });
    }


    private void getOrdersBetweenDates() {
        handleException(() -> {
            System.out.println("\nEnter date FROM in format " + DateTimeUtil.DATE_PATTERN + ":");
            Date dateFrom = DateTimeUtil.parseFromString(scanner.nextLine());
            System.out.println("\nEnter date TO in format " + DateTimeUtil.DATE_PATTERN + ":");
            Date dateTo = DateTimeUtil.parseFromString(scanner.nextLine());

            List<Order> orders = orderService.getBetweenDates(dateFrom, dateTo);
            if(orders.isEmpty()){
                throw new CarRentalException("\nNo orders in this range");
            }

            AtomicInteger counter = new AtomicInteger(1);
            System.out.println();
            orders.forEach(order ->
                    System.out.println("#" + counter.getAndIncrement() +
                            getCarInfoById(order.getCarId()) + order.toShortString()));
        });
    }


    private void getUserOrdersBetweenDates() {
        handleException(() -> {
            Session session = sessionService.getActive();
            System.out.println("\nEnter date FROM in format " + DateTimeUtil.DATE_PATTERN + ":");
            Date dateFrom = DateTimeUtil.parseFromString(scanner.nextLine());
            System.out.println("\nEnter date TO in format " + DateTimeUtil.DATE_PATTERN + ":");
            Date dateTo = DateTimeUtil.parseFromString(scanner.nextLine());

            List<Order> orders = orderService.getUserOrdersBetweenDates(session.getUserId(), dateFrom, dateTo);
            if(orders.isEmpty()){
                throw new CarRentalException("\nNo orders in this range");
            }

            AtomicInteger counter = new AtomicInteger(1);
            System.out.println();
            orders.forEach(order ->
                    System.out.println("#" + counter.getAndIncrement() +
                            getCarInfoById(order.getCarId()) + order.toShortString()));
        });
    }


    private void deleteOrder() {
        handleException(() -> {
            System.out.println("\nChoose order you wanna delete:\n");
            Order order = chooseOrderFromOrdersList(orderService.getAll());
            System.out.println("\nAre you sure you wanna delete this order?\n1 - Yes\n2 - No");
            int choose = Integer.parseInt(scanner.nextLine());
            if (choose == 1) {
                if (orderService.delete(order.getId())) {
                    System.out.println("\nOrder was deleted successfully\n");
                    return;
                }
                System.out.println("\nOrder was NOT deleted\n");
            }
        });
    }


    private String getCarInfoById(UUID id) {
        Car car = carService.getById(id);
        return String.format(" | Car: number: %-5s brand: %-5s model: %-5s ",
                car.getNumber(), car.getBrand(), car.getModel());
    }


    private Order chooseOrderFromOrdersList(List<Order> orders) {
        if (orders.isEmpty()) {
            throw new CarRentalException("\nNo orders exist, firstly create order");
        }
        AtomicInteger counter = new AtomicInteger(1);
        orders.forEach(order -> System.out.println("#" + counter.getAndIncrement() + order.toShortString()));
        System.out.println("\nEnter the position of necessary order:");
        int position = Integer.parseInt(scanner.nextLine());
        if (position <= 0 || position > orders.size() + 1) {
            throw new CarRentalException("\nIncorrect position entered");
        }
        return orders.get(position - 1);
    }


    private String getUserInfoById(UUID id) {
        User user = userService.getById(id);
        return String.format(" | User: %-10s  %-10s  %-10s email: %-5s\n",
                user.getName(), user.getSurname(), user.getPatronymic(), user.getEmail());
    }


    private Car chooseCarByPosition(Date from, Date to) {
        List<Car> cars = carService.getAvailableCars(from, to);
        if (cars.isEmpty()) {
            throw new CarRentalException("\nNo cars exist, firstly create car");
        }
        AtomicInteger counter = new AtomicInteger(1);
        cars.forEach(carComfort -> System.out.println("#" + counter.getAndIncrement() + carComfort.toShortString()));
        System.out.println("\nEnter the position of necessary car:");
        int position = Integer.parseInt(scanner.nextLine());
        if (position <= 0 || position > cars.size() + 1) {
            throw new CarRentalException("\nIncorrect position entered");
        }
        return cars.get(position - 1);
    }
}