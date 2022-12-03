import config.Initializer;
import connection.ConnectionManager;
import controller.Controller;
import migration.MigrationManager;

import java.sql.Connection;

public class Application {
    public static void run() {
        MigrationManager.migrate(); //todo удалить все активные сессии перед началом application/программы
        Connection connection = ConnectionManager.getConnection();
        Controller controller = Initializer.initialize(connection);
        controller.programInterface();
    }
}