package services;

import java.util.List;
import java.util.UUID;

public interface CrudGenericInterface<T> {
    void insert(T t);

    List<T> getAll();

    T getById(UUID id);

    void update(T t);

    void delete(UUID id);
}