package controller;

import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import models.cars.Car;
import models.cars.CarType;
import models.people.User;
import models.session.Session;
import services.car.CarService;
import services.session.SessionService;
import services.user.UserService;
import util.DateTimeUtil;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import static exception.ExceptionHandler.handleException;


@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    private final UserService userService;
    private final SessionService sessionService;
    private final Scanner scanner;

    /*public void programInterface() {
        handleException(() -> {
            Session session = sessionService.getActive();
            if (carService.isManager(session.getUserId())) {
                managerInterface();
            } else {
                userInterface();
            }
        });
    }

    private void managerInterface() {
        while (true) {
            if (sessionService.isUserAuthenticated()) {
                System.out.println("Choose item:\n1 - Get your profile\n" +
                        "2 - Update your account\n3 - Delete your account\n" +
                        "4 - Register manager\n5 - Get all users\n" +
                        "6 - Get user by email\n7 - Delete user by id\n8 - Return");
                int choose = Integer.parseInt(scanner.nextLine());
                switch (choose) {
                    case 1 -> getProfile();
                    case 2 -> update();
                    case 3 -> delete();
                    case 4 -> registerManager();
                    case 5 -> getAllCars();
                    case 6 -> getByEmail();
                    case 7 -> deleteById();
                    case 8 -> {
                        return;
                    }
                    default -> System.out.println("Entered incorrect data");
                }
            } else {
                return;
            }
        }
    }*/


    public void register() {
        handleException(() -> {
            System.out.println("Enter your number:");
            String number = scanner.nextLine();
            System.out.println("Enter your brand:");
            String brand = scanner.nextLine();
            System.out.println("Enter your model:");
            String model = scanner.nextLine();

            System.out.println("Enter your price:");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.println("Enter your deposit:");
            double deposit = Double.parseDouble(scanner.nextLine());

            Car registeredCar = carService.insert(Car.builder()
                    .number(number)
                    .brand(brand)
                    .model(model)
                    .price(price)
                    .deposit(deposit)
                    .build());
            System.out.println("You have been registered successfully");
            System.out.println(registeredCar.toString());
        });
    }

    private void getAllCars() {
        handleException(() -> carService.getAll().forEach(System.out::println));
    }


    private void getById() {
        handleException(() -> {
            System.out.println("Enter the id of necessary car:");
            UUID id = UUID.fromString(scanner.nextLine());
            Car car = carService.getById(id);
            System.out.println(car.toString());
        });
    }


    private void update() {
        handleException(() -> {
            chooseAndUpdate();
            System.out.println("Your account updated successfully");
            chooseAndUpdate();
        });
    }


    private void chooseAndUpdate() {
        int choose;
        Car car = chooseCarTypeByPosition();
        System.out.println("1 - Number\n2 - Brand\n" +
                "3 - Model\n4 - Price\n" +
                "5 - Deposit\n6 - Return");
        choose = Integer.parseInt(scanner.nextLine());
        switch (choose) {
            case 1 -> {
                System.out.println("Enter new number:");
                car.setNumber(scanner.nextLine());
            }
            case 2 -> {
                System.out.println("Enter new brand:");
                car.setBrand(scanner.nextLine());
            }
            case 3 -> {
                System.out.println("Enter new model:");
                car.setModel(scanner.nextLine());
            }
            case 4 -> {
                System.out.println("Enter new price:");
                car.setPrice(scanner.nextDouble());
            }
            case 5 -> {
                System.out.println("Enter new deposit:");
                car.setDeposit(scanner.nextDouble());
            }
            case 6 -> {
                return;
            }
            default -> {
                System.out.println("You entered invalid data\n\nCar was NOT updated");
                return;
            }
        }
        carService.update(car);
        System.out.println("\nCar updated successfully\n");
    }


    private void delete() {
        handleException(() -> {
            System.out.println("Are you sure you wanna delete your account?\n1 - Yes\n2 - No");
            int choose = Integer.parseInt(scanner.nextLine());
            if (choose == 1) {
                Session curSession = sessionService.getActive();
                carService.delete(curSession.getUserId());
                System.out.println("Your account deleted successfully");
            }
        });
    }

    private void deleteById() {
        handleException(() -> {
            System.out.println("Enter the user id:");
            UUID id = UUID.fromString(scanner.nextLine());
            carService.delete(id);
            System.out.println("User account with such an id deleted successfully");
        });
    }


    private Car chooseCarTypeByPosition() {
        getAllCars();
        System.out.println("\nEnter the position of necessary car:");
        int position = Integer.parseInt(scanner.nextLine());
        List<Car> cars = carService.getAll();
        if (position <= 0 || position > cars.size() + 1) {
            throw new CarRentalException("Incorrect position entered");
        }
        return cars.get(position - 1);
    }
}