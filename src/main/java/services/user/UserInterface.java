package services.user;

import models.people.User;
import services.CrudGenericInterface;

import java.util.UUID;

public interface UserInterface extends CrudGenericInterface<User> {
    User registerManager(String email);

    boolean isManager(UUID userId);

    User register(User user);

    User getByEmail(String email);
}