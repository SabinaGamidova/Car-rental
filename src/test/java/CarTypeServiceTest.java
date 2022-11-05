import exception.CarRentalException;
import models.cars.CarComfort;
import models.cars.CarType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CarTypeRepository;
import services.car.CarTypeService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarTypeServiceTest {
    @Mock
    private CarTypeRepository carTypeRepositoryMock;
    private CarTypeService carTypeService;

    @BeforeEach
    public void setUp() {
        carTypeService = new CarTypeService(carTypeRepositoryMock);
        assertNotNull(carTypeRepositoryMock);
        assertNotNull(carTypeService);
    }

    @Test
    public void whenInsertCorrectCarType_thenVerifyCarType() {
        CarType carType = buildCarType(UUID.randomUUID());

        when(carTypeRepositoryMock.insert(carType)).thenReturn(carType);
        CarType response = carTypeService.insert(carType);

        assertNotNull(response);
        assertEquals(carType, response);

        verify(carTypeRepositoryMock).insert(carType);
        verifyNoMoreInteractions(carTypeRepositoryMock);
    }


    @Test
    public void whenInsertNullCarType_thenVerifyException() {
        String exceptionMessage = "Car type must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carTypeService.insert(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carTypeRepositoryMock);
    }


    @Test
    public void whenInsertInvalidCarType_thenVerifyException() {
        CarType carType = new CarType();

        String exceptionMessage = "Car type has invalid data";
        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carTypeService.insert(carType));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carTypeRepositoryMock);
    }


    @Test
    public void whenGetAll_thenVerifyListOfCarTypes() {
        List<CarType> carTypes = buildCarTypeList();

        when(carTypeRepositoryMock.getAll()).thenReturn(carTypes);

        List<CarType> response = carTypeService.getAll();

        assertNotNull(response);
        assertEquals(carTypes.size(), response.size());
        assertArrayEquals(carTypes.toArray(), response.toArray());

        verify(carTypeRepositoryMock).getAll();
        verifyNoMoreInteractions(carTypeRepositoryMock);
    }


    @Test
    public void whenGetByCorrectId_thenVerifyCarType() {
        UUID id = UUID.randomUUID();
        CarType carType = buildCarType(UUID.randomUUID());

        when(carTypeRepositoryMock.getById(id)).thenReturn(carType);
        CarType response = carTypeService.getById(id);

        assertNotNull(response);
        assertEquals(carType, response);

        verify(carTypeRepositoryMock).getById(id);
        verifyNoMoreInteractions(carTypeRepositoryMock);
    }


    @Test
    public void whenGetByInvalidId_thenVerifyException() {
        String exceptionMessage = "Car type id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carTypeService.getById(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carTypeRepositoryMock);
    }


    @Test
    public void whenGetByAbsentId_thenVerifyException() {
        UUID id = UUID.randomUUID();
        String exceptionMessage = String.format("Car type with id %s not found", id);

        when(carTypeRepositoryMock.getById(id)).thenThrow(new CarRentalException(exceptionMessage));
        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carTypeService.getById(id));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(carTypeRepositoryMock).getById(id);
        verifyNoMoreInteractions(carTypeRepositoryMock);
    }


    @Test
    public void whenUpdateCorrectCarType_thenVerifyCarType() {
        CarType carType = buildCarType(UUID.randomUUID());

        when(carTypeRepositoryMock.isExistById(carType.getId())).thenReturn(Boolean.TRUE);
        when(carTypeRepositoryMock.update(carType)).thenReturn(carType);

        CarType response = carTypeService.update(carType);
        assertNotNull(response);

        assertEquals(carType, response);

        verify(carTypeRepositoryMock).isExistById(carType.getId());
        verify(carTypeRepositoryMock).update(carType);
        verifyNoMoreInteractions(carTypeRepositoryMock);
    }


    @Test
    public void whenUpdateNullCarType_thenVerifyException() {
        String exceptionMessage = "Car type must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carTypeService.update(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carTypeRepositoryMock);
    }


    @Test
    public void whenUpdateInvalidCarType_thenVerifyException() {
        CarType invalidCarType = new CarType();
        String exceptionMessage = "Car type has invalid data";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carTypeService.update(invalidCarType));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carTypeRepositoryMock);
    }


    @Test
    public void whenUpdateAbsentCarType_thenVerifyException() {
        CarType absentCarType = buildCarType(UUID.randomUUID());
        String exceptionMessage = String.format("Car type with id %s not found", absentCarType.getId());

        when(carTypeRepositoryMock.isExistById(absentCarType.getId())).thenReturn(Boolean.FALSE);

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carTypeService.update(absentCarType));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(carTypeRepositoryMock).isExistById(absentCarType.getId());
        verify(carTypeRepositoryMock, never()).update(absentCarType);
        verifyNoMoreInteractions(carTypeRepositoryMock);
    }


    @Test
    public void whenDeleteCorrectCarType_thenVerifyTrue() {
        CarType carType = buildCarType(UUID.randomUUID());

        when(carTypeRepositoryMock.getById(carType.getId())).thenReturn(carType);
        when(carTypeRepositoryMock.update(carType)).thenReturn(carType);

        boolean result = carTypeService.delete(carType.getId());

        assertTrue(result);

        verify(carTypeRepositoryMock).getById(carType.getId());
        verify(carTypeRepositoryMock).update(carType);
        verifyNoMoreInteractions(carTypeRepositoryMock);
    }


    @Test
    public void whenDeleteWithInvalidId_thenVerifyException() {
        String exceptionMessage = "Can't remove the car type, its id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carTypeService.delete(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carTypeRepositoryMock);
    }


    @Test
    public void whenDeleteWithAbsentId_thenVerifyGetByIdException() {
        UUID absentId = UUID.randomUUID();
        String exceptionMessage = String.format("Car type with id %s not found", absentId);

        when(carTypeRepositoryMock.getById(absentId)).thenThrow(new CarRentalException(exceptionMessage));

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carTypeService.delete(absentId));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(carTypeRepositoryMock).getById(absentId);
        verify(carTypeRepositoryMock, never()).update(any(CarType.class));
        verifyNoMoreInteractions(carTypeRepositoryMock);
    }


    @Test
    public void whenDelete_thenVerifyUpdateException() {
        UUID id = UUID.randomUUID();
        CarType carType = buildCarType(id);
        String exceptionMessage = "Update exception";

        when(carTypeRepositoryMock.getById(id)).thenReturn(carType);
        when(carTypeRepositoryMock.update(carType)).thenThrow(new CarRentalException(exceptionMessage));

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carTypeService.delete(id));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(carTypeRepositoryMock).getById(id);
        verify(carTypeRepositoryMock).update(carType);
        verifyNoMoreInteractions(carTypeRepositoryMock);
    }


    private CarType buildCarType(UUID id) {
        CarType carType = new CarType();
        carType.setId(id);
        carType.setName("TestCarTypeName");
        carType.setStatus(Boolean.TRUE);
        return carType;
    }


    private List<CarType> buildCarTypeList() {
        return List.of(
                buildCarType(UUID.randomUUID()),
                buildCarType(UUID.randomUUID()),
                buildCarType(UUID.randomUUID()));
    }
}