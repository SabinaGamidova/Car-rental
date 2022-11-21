package controller;

import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import models.cars.CarComfort;
import models.session.Session;
import services.car.CarComfortService;
import services.session.SessionService;
import services.user.UserService;

import java.util.List;
import java.util.Scanner;

import static exception.ExceptionHandler.handleException;

@RequiredArgsConstructor
public class CarComfortController {
    private final CarComfortService carComfortService;
    private final SessionService sessionService;
    private final UserService userService;
    private final Scanner scanner;

    public void programInterface() {
        handleException(() -> {
            Session session = sessionService.getActive();
            while (true) {
                if (userService.isManager(session.getUserId()) && sessionService.isUserAuthenticated()) {
                    System.out.println("\nChoose item:\n1 - Create new car comfort\n" +
                            "2 - Get all car comforts\n" +
                            "3 - Update car comfort\n" +
                            "4 - Delete car comfort\n" +
                            "5 - Return");
                    int choose = Integer.parseInt(scanner.nextLine());
                    switch (choose) {
                        case 1 -> createCarComfort();
                        case 2 -> getAllCarComforts();
                        case 3 -> updateCarComfort();
                        case 4 -> deleteCarComfort();
                        case 5 -> {
                            return;
                        }
                        default -> System.out.println("Entered incorrect data");
                    }
                } else {
                    return;
                }
            }
        });
    }

    private void createCarComfort() {
        handleException(() -> {
            System.out.println("Enter name:");
            String name = scanner.nextLine();
            System.out.println("Enter description:");
            String description = scanner.nextLine();
            CarComfort carComfort = CarComfort
                    .builder()
                    .name(name)
                    .description(description)
                    .build();
            carComfortService.insert(carComfort);
        });
    }

    private void getAllCarComforts() {
        handleException(() -> carComfortService.getAll().forEach(System.out::println));
    }


    private void updateCarComfort() {
        handleException(() -> {
            System.out.println("\nChoose car comfort you wanna update:");
            CarComfort carComfort = chooseCarComfortByPosition();
            chooseForUpdate(carComfort);
            System.out.println(carComfort);
        });
    }

    private void chooseForUpdate(CarComfort carComfort) {
        int choose;
        System.out.println("1 - Name\n2 - Description\n");
        choose = Integer.parseInt(scanner.nextLine());
        switch (choose) {
            case 1 -> {
                System.out.println("Enter new name:");
                carComfort.setName(scanner.nextLine());
            }
            case 2 -> {
                System.out.println("Enter new description:");
                carComfort.setDescription(scanner.nextLine());
            }
            default -> {
                System.out.println("\nYou entered invalid data");
                System.out.println("\nCar comfort was NOT updated\n");
                return;
            }
        }
        carComfortService.update(carComfort);
        System.out.println("\nCar comfort was updated successfully\n");
    }

    private void deleteCarComfort() {
        handleException(() -> {
            System.out.println("\nChoose car comfort you wanna delete:");
            CarComfort carComfort = chooseCarComfortByPosition();
            if (carComfortService.delete(carComfort.getId())) {
                System.out.println("\nCar comfort was deleted successfully\n");
                return;
            }
            System.out.println("\nCar comfort was NOT deleted\n");
        });
    }

    private CarComfort chooseCarComfortByPosition() {
        getAllCarComforts();
        System.out.println("\nEnter the position of necessary car comfort:");
        int position = Integer.parseInt(scanner.nextLine());
        List<CarComfort> carComforts = carComfortService.getAll();
        if (position <= 0 || position > carComforts.size() + 1) {
            throw new CarRentalException("Incorrect position entered");
        }
        return carComforts.get(position - 1);
    }
}