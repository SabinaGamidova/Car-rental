package services.user;

import models.people.User;
import services.CrudGenericInterface;

import java.util.List;
import java.util.UUID;

public class UserService implements CrudGenericInterface<User> {

    @Override
    public User insert(User user){
        return null;
    }

    @Override
    public User getById(UUID id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User update(User user){return null;}

    @Override
    public boolean delete(UUID id){return true;}
}