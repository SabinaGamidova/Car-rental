package services.user;

import connection.Transactionable;
import exception.CarRentalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.people.User;
import org.apache.commons.lang3.StringUtils;
import repository.UserRepository;
import services.CrudGenericInterface;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class UserService implements CrudGenericInterface<User>, Transactionable {
    private UserRepository userRepository;

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
        User updatedUser = wrapIntoTransaction(() -> userRepository.update(user));
        log.info("User has been updated successfully");
        return updatedUser;
    }

    @Override
    public boolean delete(UUID id) {
        if (Objects.isNull(id)) {
            log.error("Can not find user. User id must be not null");
            throw new CarRentalException("Can't remove the user, his id must be NOT null");
        }
        return wrapIntoTransaction(() -> {
            User user = userRepository.getById(id);
            user.setStatus(Boolean.FALSE);
            userRepository.update(user);
            return user.isStatus() != Boolean.TRUE;
        });
    }

    private void validateUser(User user) {
        if (Objects.isNull(user)) {
            log.error("User is null");
            throw new CarRentalException("User must be NOT null");
        }
        if (StringUtils.isBlank(user.getName()) ||
                StringUtils.isBlank(user.getSurname()) ||
                StringUtils.isBlank(user.getPatronymic()) ||
                StringUtils.isBlank(user.getEmail()) ||
                StringUtils.isBlank(user.getPassword())) {
            log.error("User properties have invalid format {}", user);
            throw new CarRentalException("User has invalid data");
        }
    }
}