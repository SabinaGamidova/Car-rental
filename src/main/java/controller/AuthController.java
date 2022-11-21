package controller;

import lombok.AllArgsConstructor;
import services.auth.AuthService;
import services.session.SessionService;

import java.util.Scanner;

@AllArgsConstructor
public final class AuthController {
    private AuthService authService;
    private SessionService sessionService;
    private Scanner scanner;

    public void logIn() {
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        if (authService.logIn(email, password)) {
            System.out.println("You logged in successfully");
        } else {
            System.out.println("Email or password is not correct, please try again");
        }

    }


    public void logOut() {
        System.out.println("Are you sure you wanna log out?\n1 - Yes\n2 - No");
        int choose = Integer.parseInt(scanner.nextLine());
        if (choose == 1) {
            authService.logOut(sessionService.getActive().getUserId());
        }
    }
}