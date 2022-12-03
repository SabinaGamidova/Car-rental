package controller;

import lombok.RequiredArgsConstructor;
import models.people.User;
import models.session.Session;
import services.session.SessionService;
import services.user.UserService;

import java.util.Scanner;

import static exception.ExceptionHandler.handleException;

@RequiredArgsConstructor
public class Controller {
    private final AuthController authController;
    private final UserController userController;
    private final EngineController engineController;
    private final CarComfortController carComfortController;
    private final CarTypeController carTypeController;
    private final CarController carController;
    private final OrderController orderController;
    private final SessionService sessionService;
    private final UserService userService;
    private final Scanner scanner;

    public void programInterface() {
        while (true) {
            if (sessionService.isUserAuthenticated()) {
                Session session = sessionService.getActive();
                if (userService.isManager(session.getUserId())) {
                    authenticatedManagerInterface();
                } else {
                    authenticatedUserInterface();
                }
            } else {
                notAuthenticatedUserInterface();
            }
        }
    }

    private void authenticatedManagerInterface() {
        handleException(() -> {
            System.out.println("\nChoose the item:\n" +
                    "1 - Account actions\n2 - Order operations\n" +
                    "3 - Car operations\n4 - Car type operations\n" +
                    "5 - Car comfort operations\n6 - Engine operations\n" +
                    "0 - Log out");
            int choose = Integer.parseInt(scanner.nextLine());
            switch (choose) {
                case 1 -> userController.programInterface();
                case 2 -> orderController.programInterface();
                case 3 -> carController.programInterface();
                case 4 -> carTypeController.programInterface();
                case 5 -> carComfortController.programInterface();
                case 6 -> engineController.programInterface();
                case 0 -> authController.logOut();
                default -> System.out.println("Entered incorrect data");
            }
        });
    }


    private void authenticatedUserInterface() {
        handleException(() -> {
            System.out.println("\nChoose the item:\n" +
                    "1 - Account actions\n2 - Car operations\n3 - Order operations\n0 - Log out");
            int choose = Integer.parseInt(scanner.nextLine());
            switch (choose) {
                case 1 -> userController.programInterface();
                case 2 -> carController.programInterface();
                case 3 -> orderController.programInterface();
                case 0 -> authController.logOut();
                default -> System.out.println("Entered incorrect data");
            }
        });
    }

    private void notAuthenticatedUserInterface() {
        handleException(() -> {
            System.out.println("\nChoose the item:\n1 - Log in\n2 - Register");
            int choose = Integer.parseInt(scanner.nextLine());
            switch (choose) {
                case 1 -> authController.logIn();
                case 2 -> userController.register();
                default -> System.out.println("Entered incorrect data");
            }
        });
    }
}