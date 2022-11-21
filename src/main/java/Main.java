import connection.ConnectionManager;
import controller.AuthController;
import controller.Controller;
import controller.UserController;
import repository.RoleRepository;
import repository.SessionRepository;
import repository.UserRepository;
import services.auth.AuthService;
import services.session.SessionService;
import services.user.UserService;

import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Application.run();
        Scanner scanner = new Scanner(System.in);
        Connection connection = ConnectionManager.getConnection();
        UserRepository userRepository = new UserRepository(connection);
        RoleRepository roleRepository = new RoleRepository(connection);
        SessionRepository sessionRepository = new SessionRepository(connection);


        SessionService sessionService = new SessionService(sessionRepository);
        UserService userService = new UserService(userRepository,
                sessionService, roleRepository);
        AuthService authService = new AuthService(userService, sessionService);


        UserController userController = new UserController(userService,
                sessionService, scanner);
        AuthController authController = new AuthController(authService, sessionService, scanner);

        Controller controller = new Controller(authController, userController, sessionService, scanner);
        controller.programInterface();
    }
}