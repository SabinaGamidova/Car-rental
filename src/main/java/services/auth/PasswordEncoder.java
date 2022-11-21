package services.auth;

import org.mindrot.jbcrypt.BCrypt;

import static org.apache.commons.lang3.StringUtils.isBlank;

public final class PasswordEncoder {
    private static final String SALT = BCrypt.gensalt();

    public static String encode(String password) {
        return BCrypt.hashpw(password, SALT);
    }

    public static boolean isMatch(String rawPassword, String hashedPassword) {
        if ((isBlank(rawPassword) && !isBlank(hashedPassword)) ||
                (!isBlank(rawPassword) && isBlank(hashedPassword))) {
            return Boolean.FALSE;
        }
        return  BCrypt.checkpw(rawPassword, hashedPassword);
    }
}