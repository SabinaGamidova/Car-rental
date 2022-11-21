package controller;

import lombok.RequiredArgsConstructor;
import models.people.User;
import models.session.Session;
import services.session.SessionService;
import services.user.UserService;
import util.DateTimeUtil;

import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

import static exception.ExceptionHandler.handleException;

@RequiredArgsConstructor
public class UserController {
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
                    case 5 -> getAll();
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
    }

    private void userInterface() {
        while (true) {
            if (sessionService.isUserAuthenticated()) {
                System.out.println("\nChoose item:\n1 - Get your profile\n" +
                        "2 - Update your account\n" +
                        "3 - Delete your account\n4 - Return");
                int choose = Integer.parseInt(scanner.nextLine());
                switch (choose) {
                    case 1 -> getProfile();
                    case 2 -> update();
                    case 3 -> delete();
                    case 4 -> {
                        return;
                    }
                    default -> System.out.println("Entered incorrect data");
                }
            } else {
                return;
            }
        }
    }

    public void register() {
        handleException(() -> {
            System.out.println("Enter your name:");
            String name = scanner.nextLine();
            System.out.println("Enter your surname:");
            String surname = scanner.nextLine();
            System.out.println("Enter your patronymic:");
            String patronymic = scanner.nextLine();
            System.out.println("Enter date of your birth " + DateTimeUtil.DATE_PATTERN + ":");
            Date dateOfBirth = DateTimeUtil.parseFromString(scanner.nextLine());
            System.out.println("Enter email:");
            String email = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();
            User registeredUser = userService.register(User.builder()
                    .name(name)
                    .surname(surname)
                    .patronymic(patronymic)
                    .dateOfBirth(dateOfBirth)
                    .email(email)
                    .password(password)
                    .build());
            System.out.println("You have been registered successfully");
            System.out.println(registeredUser.toString());
        });
    }


    private void registerManager() {
        System.out.println("Enter email of a new manager:");
        String email = scanner.nextLine();
        handleException(() -> {
            User registeredManager = userService.registerManager(email);
            System.out.println("New manager has been registered successfully");
            System.out.println(registeredManager.toString());
        });
    }

    private void getAll() {
        handleException(() -> userService.getAll().forEach(System.out::println));
    }


    private void getByEmail() {
        handleException(() -> {
            System.out.println("Enter the email of necessary user:");
            String email = scanner.nextLine();
            User user = userService.getByEmail(email);
            System.out.println(user.toString());
        });
    }

    private void getProfile() {
        handleException(() -> {
            Session currentSession = sessionService.getActive();
            User user = userService.getById(currentSession.getUserId());
            System.out.println(user.toString());
        });
    }


    private void update() {
        handleException(() -> {
            getProfile();
            chooseAndUpdate();
            System.out.println("Your account updated successfully");
            getProfile();
        });
    }


    private void chooseAndUpdate() {
        int choose;
        Session curSession = sessionService.getActive();
        User user = userService.getById(curSession.getUserId());
        System.out.println("1 - Name\n2 - Surname\n" +
                "3 - Patronymic\n4 - Date of birth\n" +
                "5 - Email\n6 - Password");
        choose = Integer.parseInt(scanner.nextLine());
        switch (choose) {
            case 1 -> {
                System.out.println("Enter new name:");
                user.setName(scanner.nextLine());
            }
            case 2 -> {
                System.out.println("Enter new surname:");
                user.setSurname(scanner.nextLine());
            }
            case 3 -> {
                System.out.println("Enter new patronymic:");
                user.setPatronymic(scanner.nextLine());
            }
            case 4 -> {
                System.out.println("Enter new date of birth " + DateTimeUtil.DATE_PATTERN + ":");
                Date dateOfBirth = DateTimeUtil.parseFromString(scanner.nextLine());
                user.setDateOfBirth(dateOfBirth);
            }
            case 5 -> {
                System.out.println("Enter new email:");
                user.setEmail(scanner.nextLine());
            }
            case 6 -> {
                System.out.println("Enter old password:");
                String oldPassword = scanner.nextLine();
                System.out.println("Enter new password:");
                String newPassword = scanner.nextLine();
                userService.updatePassword(user.getId(), oldPassword, newPassword);
                return;
            }
            default -> {
                System.out.println("You entered invalid data");
                return;
            }
        }
        userService.update(user);
    }


    private void delete() {
        handleException(() -> {
            System.out.println("Are you sure you wanna delete your account?\n1 - Yes\n2 - No");
            int choose = Integer.parseInt(scanner.nextLine());
            if (choose == 1) {
                Session curSession = sessionService.getActive();
                userService.delete(curSession.getUserId());
                System.out.println("Your account deleted successfully");
            }
        });
    }

    private void deleteById() {
        handleException(() -> {
            System.out.println("Enter the user id:");
            UUID id = UUID.fromString(scanner.nextLine());
            userService.delete(id);
            System.out.println("User account with such an id deleted successfully");
        });
    }
}