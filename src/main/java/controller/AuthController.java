package controller;

import lombok.RequiredArgsConstructor;
import services.auth.AuthService;
import services.session.SessionService;

import java.util.Scanner;

import static exception.ExceptionHandler.handleException;

@RequiredArgsConstructor
public final class AuthController {
    private final AuthService authService;
    private final SessionService sessionService;
    private final Scanner scanner;

    public void logIn() {
        handleException(() -> {
            System.out.println("\nEnter your email:");
            String email = scanner.nextLine();
            System.out.println("\nEnter your password:");
            String password = scanner.nextLine();
            if (authService.logIn(email, password)) {
                System.out.println("\nYou logged in successfully\n");
            } else {
                System.out.println("\nEmail or password is not correct, please try again\n");
            }
        });
    }


    public void logOut() {
        handleException(() -> {
            System.out.println("\nAre you sure you wanna log out?\n1 - Yes\n2 - No");
            int choose = Integer.parseInt(scanner.nextLine());
            if (choose == 1) {
                authService.logOut(sessionService.getActive().getUserId());
                System.out.println("You logged out successfully");
            }
        });
    }

}