package mapper;

import exception.CarRentalException;
import lombok.NoArgsConstructor;
import models.Mappable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@NoArgsConstructor
public class Mapper {
    public static Object mapSingleFromResultSet(ResultSet resultSet, Class<?> cls) {
        Object[] fieldValues = getFieldsValuesFromResultSet(resultSet, cls);
        Constructor<?> constructor = getConstructor(cls);
        return createInstance(constructor, fieldValues);
    }


    private static Object[] getFieldsValuesFromResultSet(ResultSet resultSet, Class<?> cls) {
        return Arrays.stream(cls.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Mappable.class))
                .map(field -> getSingleFieldValue(resultSet, field))
                .toArray(Object[]::new);
    }


    private static Object getSingleFieldValue(ResultSet resultSet, Field field) {
        field.setAccessible(Boolean.TRUE);
        Mappable m = field.getAnnotation(Mappable.class);
        try {
            return resultSet.getObject(m.columnNumber(), m.dataType());
        } catch (SQLException e) {
            throw new CarRentalException(e.getMessage());
        }
    }


    private static Constructor<?> getConstructor(Class<?> cls) {
        try {
            Class<?>[] fieldsTypes = getClassFieldsTypes(cls);
            return cls.getConstructor(fieldsTypes);
        } catch (NoSuchMethodException e) {
            throw new CarRentalException(e.getMessage());
        }
    }


    private static Class<?>[] getClassFieldsTypes(Class<?> cls) {
        return Arrays
                .stream(cls.getDeclaredFields())
                .map(Field::getType)
                .toArray(Class[]::new);
    }


    private static Object createInstance(Constructor<?> constructor, Object... objects) {
        try {
            return constructor.newInstance(objects);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Exception in createInstance method.");
            throw new CarRentalException(e.getMessage());
        }
    }
}