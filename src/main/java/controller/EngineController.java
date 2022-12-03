package controller;

import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import models.cars.Engine;
import models.cars.FuelType;
import models.cars.TransmissionType;
import models.session.Session;
import services.engine.EngineCharacteristics;
import services.engine.EngineService;
import services.engine.FuelTypeService;
import services.engine.TransmissionTypeService;
import services.session.SessionService;
import services.user.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static exception.ExceptionHandler.handleException;

@RequiredArgsConstructor
public class EngineController {
    private final EngineService engineService;
    private final SessionService sessionService;
    private final UserService userService;
    private final FuelTypeService fuelTypeService;
    private final TransmissionTypeService transmissionTypeService;
    private final Scanner scanner;


    public void programInterface() {
        handleException(() -> {
            if(sessionService.isUserAuthenticated()){
                Session session = sessionService.getActive();
                while (true) {
                    if (userService.isManager(session.getUserId())) {
                        System.out.println("\nChoose item:\n1 - Insert new engine\n" +
                                "2 - Get all engines\n" +
                                "3 - Update engine\n" +
                                "4 - Delete engine\n" +
                                "5 - Return");
                        int choose = Integer.parseInt(scanner.nextLine());
                        switch (choose) {
                            case 1 -> insertEngine();
                            case 2 -> getAllEngines();
                            case 3 -> updateEngine();
                            case 4 -> deleteEngine();
                            case 5 -> {
                                return;
                            }
                            default -> System.out.println("Entered incorrect data");
                        }
                    } else {
                        return;
                    }
                }
            }
        });
    }


    private FuelType chooseFuelType() {
        System.out.println("\nChoose a fuel type:\n" +
                "1 - Petrol\n2 - Diesel\n" +
                "3 - Gas\n4 - Electricity");
        int choose = Integer.parseInt(scanner.nextLine());
        return switch (choose) {
            case 1 -> fuelTypeService.getByName(EngineCharacteristics.PETROL);
            case 2 -> fuelTypeService.getByName(EngineCharacteristics.DIESEL);
            case 3 -> fuelTypeService.getByName(EngineCharacteristics.GAS);
            case 4 -> fuelTypeService.getByName(EngineCharacteristics.ELECTRICITY);
            default -> throw new CarRentalException("You entered incorrect data");
        };
    }


    private TransmissionType chooseTransmissionType() {
        System.out.println("\nChoose a transmission type:\n" +
                "1 - Automatic\n2 - Mechanic");
        int choose = Integer.parseInt(scanner.nextLine());
        return switch (choose) {
            case 1 -> transmissionTypeService.getByName(EngineCharacteristics.AUTOMATIC);
            case 2 -> transmissionTypeService.getByName(EngineCharacteristics.MECHANIC);
            default -> throw new CarRentalException("You entered incorrect data");
        };
    }


    public void insertEngine() {
        handleException(() -> {
            System.out.println("\nEnter the max speed:");
            int maxSpeed = Integer.parseInt(scanner.nextLine());

            FuelType fuelType = chooseFuelType();
            TransmissionType transmissionType = chooseTransmissionType();

            System.out.println("\nEnter the volume:");
            double volume = Double.parseDouble(scanner.nextLine());

            System.out.println("\nEnter the fuel consumption:");
            double fuelConsumption = Double.parseDouble(scanner.nextLine());

            Engine insertedEngine = engineService.insert(Engine.builder()
                    .maxSpeed(maxSpeed)
                    .fuelTypeId(fuelType.getId())
                    .transmissionTypeId(transmissionType.getId())
                    .volume(volume)
                    .fuelConsumption(fuelConsumption)
                    .build());
            System.out.println("\nEngine has been inserted successfully");
            System.out.println(insertedEngine.toString());
        });
    }


    private void getAllEngines() {
        handleException(() -> {
            AtomicInteger counter = new AtomicInteger(1);
            List<Engine> engines = engineService.getAll();
            if (engines.isEmpty()) {
                throw new CarRentalException("\nNo engines exist yet");
            }
            System.out.println();
            engines.forEach(engine -> System.out.println("#" + counter.getAndIncrement() + engine.toShortString()));
        });
    }


    private void updateEngine() {
        handleException(() -> {
            Engine engine = chooseEngineByPosition();
            System.out.println("\n1 - Max speed\n2 - Fuel type\n" +
                    "3 - Transmission type\n4 - Volume\n" +
                    "5 - Fuel consumption\n6 - Return");
            int choose = Integer.parseInt(scanner.nextLine());
            switch (choose) {
                case 1 -> {
                    System.out.println("\nEnter new max speed:");
                    engine.setMaxSpeed(Integer.parseInt(scanner.nextLine()));
                }
                case 2 -> {
                    FuelType newFuelType = chooseFuelType();
                    engine.setFuelTypeId(newFuelType.getId());
                }
                case 3 -> {
                    TransmissionType newTransmissionType = chooseTransmissionType();
                    engine.setTransmissionTypeId(newTransmissionType.getId());
                }
                case 4 -> {
                    System.out.println("\nEnter new volume:");
                    engine.setVolume(Double.parseDouble(scanner.nextLine()));
                }
                case 5 -> {
                    System.out.println("\nEnter new fuel consumption:");
                    engine.setFuelConsumption(Double.parseDouble(scanner.nextLine()));
                }
                case 6 -> {
                    return;
                }
                default -> {
                    System.out.println("\nYou entered invalid data");
                    System.out.println("\nEngine was NOT updated\n");
                    return;
                }
            }
            engineService.update(engine);
            System.out.println("\nEngine was updated successfully\n");
            System.out.println(engine.toString());
        });
    }

    private void deleteEngine() {
        handleException(() -> {
            Engine engine = chooseEngineByPosition();
            if (engineService.delete(engine.getId())) {
                System.out.println("\nEngine was deleted successfully\n");
                return;
            }
            System.out.println("\nEngine was NOT deleted\n");
        });
    }

    private Engine chooseEngineByPosition() {
        List<Engine> engines = engineService.getAll();
        if (engines.isEmpty()) {
            throw new CarRentalException("\nNo engines exist yet");
        }
        getAllEngines();
        System.out.println("\nEnter the position of necessary car type:");
        int position = Integer.parseInt(scanner.nextLine());
        if (position <= 0 || position > engines.size() + 1) {
            throw new CarRentalException("\nIncorrect position entered");
        }
        return engines.get(position - 1);
    }
}