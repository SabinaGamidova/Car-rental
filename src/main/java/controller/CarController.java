package controller;

import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import models.cars.Car;
import models.cars.CarComfort;
import models.cars.CarType;
import models.cars.Engine;
import models.session.Session;
import services.car.CarComfortService;
import services.car.CarService;
import services.car.CarTypeService;
import services.engine.EngineService;
import services.session.SessionService;
import services.user.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static exception.ExceptionHandler.handleException;

@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    private final UserService userService;
    private final SessionService sessionService;
    private final CarComfortService carComfortService;
    private final CarTypeService carTypeService;
    private final EngineService engineService;
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
        if(sessionService.isUserAuthenticated()){
            Session session = sessionService.getActive();
            while (true) {
                if (userService.isManager(session.getUserId())) {
                    System.out.println("\n\nChoose item:\n1 - Insert new car\n" +
                            "2 - Get all cars\n" +
                            "3 - Get car by id\n" +
                            "4 - Get all cars by car type\n" +
                            "5 - Get all cars by car comfort\n" +
                            "6 - Update car\n" +
                            "7 - Delete car\n" +
                            "8 - Return");
                    int choose = Integer.parseInt(scanner.nextLine());
                    switch (choose) {
                        case 1 -> insertCar();
                        case 2 -> getAllCars();
                        case 3 -> getCarById();
                        case 4 -> getCarsByCarType();
                        case 5 -> getCarsByCarComfort();
                        case 6 -> chooseAndUpdate();
                        case 7 -> deleteCar();
                        case 8 -> {
                            return;
                        }
                        default -> System.out.println("\n\nEntered incorrect data");
                    }
                } else {
                    return;
                }
            }
        }
    }


    private void userInterface() {
        if(sessionService.isUserAuthenticated()){
            Session session = sessionService.getActive();
            while (true) {
                    System.out.println("\n\nChoose item:\n1 - Get all cars\n" +
                            "2 - Get all cars by car type\n" +
                            "3 - Get all cars by car comfort\n" +
                            "4 - Return");
                    int choose = Integer.parseInt(scanner.nextLine());
                    switch (choose) {
                        case 1 -> getAllCars();
                        case 2 -> getCarsByCarType();
                        case 3 -> getCarsByCarComfort();
                        case 4 -> {
                            return;
                        }
                        default -> System.out.println("\n\nEntered incorrect data");
                    }
            }
        }
    }


    private void insertCar() {
        handleException(() -> {
            System.out.println("\nEnter the number:");
            String number = scanner.nextLine();
            System.out.println("\nEnter your brand:");
            String brand = scanner.nextLine();
            System.out.println("\nEnter your model:");
            String model = scanner.nextLine();

            CarType carType = chooseCarTypeByPosition();
            CarComfort carComfort = chooseCarComfortByPosition();
            Engine engine = chooseEngineByPosition();

            System.out.println("\nEnter your price:");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.println("\nEnter your deposit:");
            double deposit = Double.parseDouble(scanner.nextLine());

            Car insertedCar = carService.insert(Car.builder()
                    .number(number)
                    .brand(brand)
                    .model(model)
                    .carTypeId(carType.getId())
                    .comfortId(carComfort.getId())
                    .engineId(engine.getId())
                    .price(price)
                    .deposit(deposit)
                    .build());
            System.out.println("\nCar has been inserted successfully");
            System.out.println(insertedCar.toString());
        });
    }


    private void getAllCars() {
        System.out.println();
        handleException(() -> {
            AtomicInteger counter = new AtomicInteger(1);
            System.out.println();
            List<Car> cars = carService.getAll();
            if (cars.isEmpty()) {
                throw new CarRentalException("\nNo cars exist yet");
            }
            cars.forEach(car -> System.out.println("#" + counter.getAndIncrement() + car.toShortString()));
        });
    }


    private void getCarById() {
        handleException(() -> {
            System.out.println("\nEnter the id of necessary car:");
            UUID id = UUID.fromString(scanner.nextLine());
            Car car = carService.getById(id);
            System.out.println(car.toString());
        });
    }


    private void getCarsByCarType() {
        handleException(() -> {
            CarType carType = chooseCarTypeByPosition();
            List<Car> cars = carService.getByCarType(carType.getId());
            if (cars.isEmpty()) {
                throw new CarRentalException("\nNo cars in this car type category exist yet");
            }
            AtomicInteger counter = new AtomicInteger(1);
            System.out.println();
            cars.forEach(car -> System.out.println("#" + counter.getAndIncrement() + car.toShortString()));
        });
    }


    private void getCarsByCarComfort() {
        handleException(() -> {
            CarComfort carComfort = chooseCarComfortByPosition();
            List<Car> cars = carService.getByCarComfort(carComfort.getId());
            if (cars.isEmpty()) {
                throw new CarRentalException("\nNo cars in this car comfort category exist yet");
            }
            AtomicInteger counter = new AtomicInteger(1);
            System.out.println();
            cars.forEach(car -> System.out.println("#" + counter.getAndIncrement() + car.toShortString()));
        });
    }


    private void chooseAndUpdate() {
        int choose;
        Car car = chooseCarByPosition();
        System.out.println("1 - Number\n2 - Brand\n" +
                "3 - Model\n4 - Car type\n5 - Car comfort\n" +
                "6 - Engine\n7 - Price\n" +
                "8 - Deposit\n9 - Return");
        choose = Integer.parseInt(scanner.nextLine());
        switch (choose) {
            case 1 -> {
                System.out.println("\nEnter new number:");
                car.setNumber(scanner.nextLine());
            }
            case 2 -> {
                System.out.println("\nEnter new brand:");
                car.setBrand(scanner.nextLine());
            }
            case 3 -> {
                System.out.println("\nEnter new model:");
                car.setModel(scanner.nextLine());
            }
            case 4 -> {
                CarType carType = chooseCarTypeByPosition();
                car.setCarTypeId(carType.getId());
            }
            case 5 -> {
                CarComfort carComfort = chooseCarComfortByPosition();
                car.setComfortId(carComfort.getId());
            }
            case 6 -> {
                Engine engine = chooseEngineByPosition();
                car.setEngineId(engine.getId());
            }
            case 7 -> {
                System.out.println("\nEnter new price:");
                car.setPrice(scanner.nextDouble());
            }
            case 8 -> {
                System.out.println("\nEnter new deposit:");
                car.setDeposit(scanner.nextDouble());
            }
            case 9 -> {
                return;
            }
            default -> {
                System.out.println("\nYou entered invalid data\n\nCar was NOT updated");
            }
        }
        carService.update(car);
        System.out.println("\nCar updated successfully\n");
    }


    private void deleteCar() {
        handleException(() -> {
            Car car = chooseCarByPosition();
            if (carService.delete(car.getId())) {
                System.out.println("\nCar was deleted successfully\n");
                return;
            }
            System.out.println("\nCar was NOT deleted\n");
        });
    }


    private Car chooseCarByPosition() {
        List<Car> car = carService.getAll();
        if (car.isEmpty()) {
            throw new CarRentalException("\nNo cars exist yet");
        }
        getAllCars();
        System.out.println("\nEnter the position of necessary car:");
        int position = Integer.parseInt(scanner.nextLine());
        if (position <= 0 || position > car.size() + 1) {
            throw new CarRentalException("\nIncorrect position entered");
        }
        return car.get(position - 1);
    }


    private CarType chooseCarTypeByPosition() {
        System.out.println();
        List<CarType> carTypes = carTypeService.getAll();
        if (carTypes.isEmpty()) {
            throw new CarRentalException("\nNo car types exist, firstly create car type");
        }
        AtomicInteger counter = new AtomicInteger(1);
        System.out.println();
        carTypes.forEach(carType -> System.out.println("#" + counter.getAndIncrement() + carType.toShortString()));
        System.out.println("\n\nEnter the position of necessary car type:");
        int position = Integer.parseInt(scanner.nextLine());
        if (position <= 0 || position > carTypes.size() + 1) {
            throw new CarRentalException("\nIncorrect position entered");
        }
        return carTypes.get(position - 1);
    }


    private CarComfort chooseCarComfortByPosition() {
        System.out.println();
        List<CarComfort> carComforts = carComfortService.getAll();
        if (carComforts.isEmpty()) {
            throw new CarRentalException("\nNo car comforts exist, firstly create car comfort");
        }

        AtomicInteger counter = new AtomicInteger(1);
        System.out.println();
        carComforts.forEach(carComfort -> System.out.println("#" + counter.getAndIncrement() + carComfort.toShortString()));
        System.out.println("\n\nEnter the position of necessary car comfort:");
        int position = Integer.parseInt(scanner.nextLine());
        if (position <= 0 || position > carComforts.size() + 1) {
            throw new CarRentalException("\nIncorrect position entered");
        }
        return carComforts.get(position - 1);
    }


    private Engine chooseEngineByPosition() {
        System.out.println();
        List<Engine> engines = engineService.getAll();
        if (engines.isEmpty()) {
            throw new CarRentalException("\nNo engines exist, firstly create engine");
        }
        AtomicInteger counter = new AtomicInteger(1);
        System.out.println();
        engines.forEach(carComfort -> System.out.println("#" + counter.getAndIncrement() + carComfort.toShortString()));
        System.out.println("\n\nEnter the position of necessary engine:");
        int position = Integer.parseInt(scanner.nextLine());
        if (position <= 0 || position > engines.size() + 1) {
            throw new CarRentalException("\nIncorrect position entered");
        }
        return engines.get(position - 1);
    }
}