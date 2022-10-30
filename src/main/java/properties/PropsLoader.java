package properties;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

@Slf4j
public class PropsLoader {

    public static Properties loadProperties(String path) {
        log.info("Trying to load properties from {}", path);
        try (InputStream is = PropsLoader.class.getResourceAsStream(path)) {
            Properties properties = new Properties();
            properties.load(is);
            log.info("Properties have been loaded successfully");
            return properties;
        } catch (IOException exception) {
            log.info("Can not load properties", exception);
            throw new UncheckedIOException(exception);
        }
    }
}
