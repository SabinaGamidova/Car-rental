package config;

import controller.AuthController;
import controller.CarComfortController;
import controller.CarController;
import controller.CarTypeController;
import controller.Controller;
import controller.EngineController;
import controller.OrderController;
import controller.UserController;
import repository.CarComfortRepository;
import repository.CarRepository;
import repository.CarTypeRepository;
import repository.EngineRepository;
import repository.FuelTypeRepository;
import repository.OrderRepository;
import repository.RoleRepository;
import repository.SessionRepository;
import repository.TransmissionTypeRepository;
import repository.UserRepository;
import services.auth.AuthService;
import services.car.CarComfortService;
import services.car.CarService;
import services.car.CarTypeService;
import services.engine.EngineService;
import services.engine.FuelTypeService;
import services.engine.TransmissionTypeService;
import services.order.OrderService;
import services.session.SessionService;
import services.user.UserService;

import java.sql.Connection;
import java.util.Scanner;

public class Initializer {
    public static Controller initialize(Connection connection) {
        UserRepository userRepository = new UserRepository(connection);
        RoleRepository roleRepository = new RoleRepository(connection);
        SessionRepository sessionRepository = new SessionRepository(connection);
        CarTypeRepository carTypeRepository = new CarTypeRepository(connection);
        CarComfortRepository carComfortRepository = new CarComfortRepository(connection);
        CarRepository carRepository = new CarRepository(connection);
        EngineRepository engineRepository = new EngineRepository(connection);
        FuelTypeRepository fuelTypeRepository = new FuelTypeRepository(connection);
        OrderRepository orderRepository = new OrderRepository(connection);
        TransmissionTypeRepository transmissionTypeRepository
                = new TransmissionTypeRepository(connection);


        SessionService sessionService = new SessionService(sessionRepository);
        UserService userService = new UserService(userRepository,
                sessionService, roleRepository);
        AuthService authService = new AuthService(userService, sessionService);
        CarTypeService carTypeService = new CarTypeService(carTypeRepository);
        CarComfortService carComfortService = new CarComfortService(carComfortRepository);
        CarService carService = new CarService(carRepository);
        EngineService engineService = new EngineService(engineRepository);
        FuelTypeService fuelTypeService = new FuelTypeService(fuelTypeRepository);
        TransmissionTypeService transmissionTypeService =
                new TransmissionTypeService(transmissionTypeRepository);
        OrderService orderService = new OrderService(orderRepository, carRepository);

        Scanner scanner = new Scanner(System.in);
        UserController userController = new UserController(userService,
                sessionService, scanner);
        AuthController authController = new AuthController(authService, sessionService, scanner);
        EngineController engineController = new EngineController(engineService, sessionService, userService, fuelTypeService, transmissionTypeService, scanner);
        CarTypeController carTypeController = new CarTypeController(carTypeService, sessionService, userService, scanner);
        CarComfortController carComfortController = new CarComfortController(carComfortService, sessionService, userService, scanner);
        CarController carController = new CarController(carService, userService, sessionService, carComfortService, carTypeService, engineService, scanner);
        OrderController orderController = new OrderController(orderService, carService, userService, sessionService, scanner);
        return new Controller(authController, userController, engineController, carComfortController, carTypeController, carController, sessionService, orderController, scanner);
    }
}