package services.user;

import connection.Transactionable;
import exception.CarRentalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.people.Role;
import models.people.User;
import models.session.Session;
import org.apache.commons.lang3.StringUtils;
import repository.RoleRepository;
import repository.UserRepository;
import services.auth.PasswordEncoder;
import services.session.SessionService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import static services.user.PasswordSize.MAX_SIZE;
import static services.user.PasswordSize.MIN_SIZE;


@Slf4j
@AllArgsConstructor
public class UserService implements UserInterface, Transactionable {
    private static final String EMAIL_PATTERN = "^(.+)@(.+)$";
    private static final String PASSWORD_PATTERN = ".*([a-zA-Z].*[0-9]|[0-9].*[a-zA-Z]).*";
    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final RoleRepository roleRepository;


    @Override
    public boolean isManager(UUID userId) {
        if (Objects.isNull(userId)) {
            return Boolean.FALSE;
        }
        return userRepository.isManager(userId);
    }

    @Override
    public User register(User user) {
        log.info("Trying to register new user");
        validateUser(user);
        if (userRepository.isExistByEmail(user.getEmail())) {
            throw new CarRentalException("User with such an email exists");
        }
        Role userRole = roleRepository.getUserRole();
        user.setRoleId(userRole.getId());
        validatePassword(user.getPassword());
        String encodedPassword = PasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User registeredUser = insert(user);
        sessionService.open(user.getEmail());
        log.info("User has been registered successfully");
        return registeredUser;
    }


    @Override
    public User registerManager(String email) {
        log.info("Trying to register new manager");
        if (StringUtils.isBlank(email)) {
            throw new CarRentalException("Invalid email");
        }
        User newManager = userRepository.getByEmail(email);
        Role managerRole = roleRepository.getManagerRole();
        newManager.setRoleId(managerRole.getId());
        this.update(newManager);
        log.info("New manager has been registered successfully");
        return newManager;
    }

    @Override
    public User insert(User user) {
        log.info("Trying to insert new user");
        validateUser(user);
        User insertedUser = wrapIntoTransaction(() -> userRepository.insert(user));
        log.info("User has been inserted successfully");
        return insertedUser;
    }

    @Override
    public List<User> getAll() {
        log.info("Trying to get all users");
        return userRepository.getAll();
    }

    @Override
    public User getByEmail(String email) {
        log.info("Trying to get user by email");
        if (Objects.isNull(email)) {
            log.error("Can not find user. User email must be not null");
            throw new CarRentalException("User email must be NOT null");
        }
        return userRepository.getByEmail(email);
    }

    @Override
    public User getById(UUID id) {
        log.info("Trying to get user by id");
        if (Objects.isNull(id)) {
            log.error("Can not find user. User id must be not null");
            throw new CarRentalException("User id must be NOT null");
        }
        return userRepository.getById(id);
    }

    @Override
    public User update(User user) {
        log.info("Trying to update user {}", user);
        validateUser(user);
        if (!userRepository.isExistById(user.getId())) {
            log.warn("User with id {} not found", user.getId());
            throw new CarRentalException("User with id %s not found", user.getId());
        }
        validateNewUserEmail(user);
        User updatedUser = wrapIntoTransaction(() -> userRepository.update(user));
        log.info("User has been updated successfully");
        return updatedUser;
    }


    private void validateNewUserEmail(User user) {
        if (userRepository.isExistByEmail(user.getEmail())) {
            User userByEmail = userRepository.getByEmail(user.getEmail());
            if (!userByEmail.getId().equals(user.getId())) {
                throw new CarRentalException("User with such an email already exists, try another one");
            }
        }
    }


    public boolean updatePassword(UUID userId, String oldPassword, String newPassword) {
        User user = userRepository.getById(userId);
        if (!PasswordEncoder.isMatch(oldPassword, user.getPassword())) {
            throw new CarRentalException("You entered incorrect old password (doesn't match)");
        }
        if (oldPassword.equals(newPassword)) {
            throw new CarRentalException("New password must be different from old one");
        }
        user.setPassword(PasswordEncoder.encode(newPassword));
        return userRepository.updatePassword(user);
    }

    @Override
    public boolean delete(UUID id) {
        if (Objects.isNull(id)) {
            log.error("Can not find user. User id must be not null");
            throw new CarRentalException("Can't remove the user, his id must be NOT null");
        }
        return wrapIntoTransaction(() -> {
            User user = userRepository.getById(id);
            checkIsExistAndCloseSession(id);
            user.setStatus(Boolean.FALSE);
            userRepository.update(user);
            return user.isStatus() != Boolean.TRUE;
        });
    }


    private void checkIsExistAndCloseSession(UUID id) {
        Session session = sessionService.getActive();
        if (session.getUserId().equals(id)) {
            sessionService.close();
        }
    }

    private void validateUser(User user) {
        if (Objects.isNull(user)) {
            log.error("User is null");
            throw new CarRentalException("User must be NOT null");
        }
        validateFields(user);
        validateEmail(user.getEmail());
    }

    private void validateFields(User user) {
        if (StringUtils.isBlank(user.getName()) ||
                StringUtils.isBlank(user.getSurname()) ||
                StringUtils.isBlank(user.getPatronymic()) ||
                StringUtils.isBlank(user.getEmail()) ||
                StringUtils.isBlank(user.getPassword())) {
            log.error("User properties have invalid format {}", user);
            throw new CarRentalException("User has invalid data");
        }
    }

    private void validateEmail(String email) {
        if (!Pattern.compile(EMAIL_PATTERN).matcher(email).matches()) {
            log.warn("User email has invalid format {}", email);
            throw new CarRentalException("User email has invalid format");
        }
    }

    private void validatePassword(String password) {
        boolean patternMatch = Pattern
                .compile(PASSWORD_PATTERN)
                .matcher(password)
                .matches();
        if(password.length() < MIN_SIZE.getValue() ||
                password.length() > MAX_SIZE.getValue() || !patternMatch){
            log.warn("User password has invalid format {}", password);
            throw new CarRentalException("User password has invalid format (size must be %d-%d and contains at least one symbol and number)", MIN_SIZE.getValue(), MAX_SIZE.getValue());
        }
    }
}