package connection;

import lombok.extern.slf4j.Slf4j;
import properties.PropsLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class ConnectionManager {
    private static final String PROPERTIES_PATH = "/jdbc.properties";
    private static final String DB_URL_PROPERTY = "url";
    private static Connection connection;

    public static Connection getConnection() {
        if(isInvalidConnection()) {
            connection = createConnection();
        }
        return connection;
    }

    private static boolean isInvalidConnection() {
        try {
            return Objects.isNull(connection) || connection.isClosed();
        } catch (SQLException exception) {
            log.error("Problem with connection", exception);
            throw new RuntimeException(exception);
        }
    }

    private static Connection createConnection() {
        Properties properties = PropsLoader.loadProperties(PROPERTIES_PATH);
        String url = properties.getProperty(DB_URL_PROPERTY);
        try {
            log.info("Trying to get connection to {}", url);
            Connection connection = DriverManager.getConnection(url, properties);
            log.info("Connection has been successfully got");
            return connection;
        } catch (SQLException exception) {
            log.error("Can not get connection", exception);
            throw new RuntimeException(exception);
        }
    }

    public static void closeConnection(Connection connection) {
        if(Objects.isNull(connection)) {
            throw new RuntimeException("Can not close connection. Connection is null");
        }
        try {
            log.info("Trying to close connection");
            connection.close();
            log.info("Connection has been successfully closed");
        } catch (SQLException exception) {
            log.error("Can not close connection", exception);
            throw new RuntimeException(exception);
        }
    }
}
