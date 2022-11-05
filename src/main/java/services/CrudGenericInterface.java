package services;

import java.util.List;
import java.util.UUID;

public interface CrudGenericInterface<T> {
    T insert(T t);

    List<T> getAll();

    T getById(UUID id);

    T update(T t);

    boolean delete(UUID id);
}