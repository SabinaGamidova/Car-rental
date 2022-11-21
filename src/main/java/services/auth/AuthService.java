package services.auth;

import exception.CarRentalException;
import lombok.AllArgsConstructor;
import models.people.User;
import models.session.Session;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import services.session.SessionService;
import services.user.UserService;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
public class AuthService {
    private final UserService userService;
    private final SessionService sessionService;

    public boolean logIn(String email, String rawPassword) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(rawPassword)) {
            throw new CarRentalException("Invalid email or password");
        }
        User user = userService.getByEmail(email);
        if (PasswordEncoder.isMatch(rawPassword, user.getPassword())) {
            sessionService.open(email);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void logOut(UUID userId) {
        if (Objects.isNull(userId)) {
            throw new CarRentalException("User id must be NOT null");
        }
        Session activeSession = sessionService.getActive();
        if (userId.equals(activeSession.getUserId())) {
            sessionService.close();
            return;
        }
        throw new CarRentalException("Can not close session due to problems with user");
    }

}