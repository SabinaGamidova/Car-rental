package controller;

import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import models.cars.CarType;
import models.session.Session;
import services.car.CarTypeService;
import services.session.SessionService;
import services.user.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static exception.ExceptionHandler.handleException;

@RequiredArgsConstructor
public class CarTypeController {
    private final CarTypeService carTypeService;
    private final SessionService sessionService;
    private final UserService userService;
    private final Scanner scanner;

    public void programInterface() {
        handleException(() -> {
            Session session = sessionService.getActive();
            while (true) {
                if (userService.isManager(session.getUserId())) {
                    System.out.println("\nChoose item:\n1 - Create new car type\n" +
                            "2 - Get all car types\n" +
                            "3 - Update car type\n" +
                            "4 - Delete car type\n" +
                            "5 - Return");
                    int choose = Integer.parseInt(scanner.nextLine());
                    switch (choose) {
                        case 1 -> createCarType();
                        case 2 -> getAllCarTypes();
                        case 3 -> updateCarType();
                        case 4 -> deleteCarType();
                        case 5 -> {
                            return;
                        }
                        default -> System.out.println("\nEntered incorrect data");
                    }
                } else {
                    return;
                }
            }
        });
    }

    private void createCarType() {
        handleException(() -> {
            System.out.println("\nEnter name:");
            String name = scanner.nextLine();
            CarType carType = CarType
                    .builder()
                    .name(name)
                    .build();
            carTypeService.insert(carType);
        });
    }

    private void getAllCarTypes() {
        handleException(() -> {
            AtomicInteger counter = new AtomicInteger(1);
            List<CarType> carTypes = carTypeService.getAll();
            if (carTypes.isEmpty()) {
                throw new CarRentalException("\nNo car types exist yet");
            }
            System.out.println();
            carTypes.forEach(carType -> System.out.println("#" + counter.getAndIncrement() + carType.toShortString()));
        });
    }


    private void updateCarType() {
        handleException(() -> {
            CarType carType = chooseCarTypeByPosition();
            System.out.println("\nEnter new name:");
            carType.setName(scanner.nextLine());
            carTypeService.update(carType);
            System.out.println("\nCar type was updated successfully\n");
            System.out.println(carType);
        });
    }


    private void deleteCarType() {
        handleException(() -> {

            //System.out.println("\nChoose car type you wanna delete:");
            CarType carType = chooseCarTypeByPosition();
            if (carTypeService.delete(carType.getId())) {
                System.out.println("\nCar type was deleted successfully\n");
                return;
            }
            System.out.println("\nCar type was NOT deleted\n");
        });
    }

    private CarType chooseCarTypeByPosition() {
            List<CarType> carTypes = carTypeService.getAll();
            if (carTypes.isEmpty()) {
                throw new CarRentalException("\nNo car types exist yet");
            }
            getAllCarTypes();
            System.out.println("\nEnter the position of necessary car type:");
            int position = Integer.parseInt(scanner.nextLine());
            if (position <= 0 || position > carTypes.size() + 1) {
                throw new CarRentalException("\nIncorrect position entered");
            }
            return carTypes.get(position - 1);
    }
}