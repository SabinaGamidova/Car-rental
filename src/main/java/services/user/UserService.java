package services.user;

import models.people.User;
import services.CrudGenericInterface;

import java.util.List;
import java.util.UUID;

public class UserService implements CrudGenericInterface<User> {

    @Override
    public void insert(User user){

    }

    @Override
    public User getById(UUID id){
        return null;
    }

    @Override
    public List<User> getAll(){
        return null;
    }

    @Override
    public void update(User user){}

    @Override
    public void delete(UUID id){}
}