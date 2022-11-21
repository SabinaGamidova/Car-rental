import migration.MigrationManager;

public class Application {
    public static void run(){
        MigrationManager.migrate(); //todo удалить все активные сессии перед началом application/программы
    }
}