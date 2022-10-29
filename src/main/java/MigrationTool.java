import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;

import java.util.Properties;

@Slf4j
public final class MigrationTool {
    private static final String PROPERTIES_PATH = "/jdbc.properties";
    private static final String DB_URL_PROPERTY = "url";
    private static final String DB_USER_PROPERTY = "user";
    private static final String DB_PASSWORD_PROPERTY = "password";

    public static void migrate() {
        log.info("Starting migration process.");
        Properties properties = PropsLoader.loadProperties(PROPERTIES_PATH);

        String dbUrl = properties.getProperty(DB_URL_PROPERTY);
        String dbUser = properties.getProperty(DB_USER_PROPERTY);
        String dbPassword = properties.getProperty(DB_PASSWORD_PROPERTY);

        Flyway
                .configure()
                .dataSource(dbUrl, dbUser, dbPassword)
                .load()
                .migrate();
        log.info("Migration process has been successfully finished.");
    }
}
