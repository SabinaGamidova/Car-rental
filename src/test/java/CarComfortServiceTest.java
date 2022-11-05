import exception.CarRentalException;
import models.cars.CarComfort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CarComfortRepository;
import services.car.CarComfortService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarComfortServiceTest {
    @Mock
    private CarComfortRepository carComfortRepositoryMock;
    private CarComfortService carComfortService;

    @BeforeEach
    public void setUp() {
        carComfortService = new CarComfortService(carComfortRepositoryMock);
        assertNotNull(carComfortRepositoryMock);
        assertNotNull(carComfortService);
    }


    @Test
    public void whenInsertCorrectCarComfort_thenVerifyCarComfort() {
        CarComfort carComfort = buildCarComfort(UUID.randomUUID());

        when(carComfortRepositoryMock.insert(carComfort)).thenReturn(carComfort);
        CarComfort response = carComfortService.insert(carComfort);

        assertNotNull(response);
        assertEquals(carComfort, response);

        verify(carComfortRepositoryMock).insert(carComfort);
        verifyNoMoreInteractions(carComfortRepositoryMock);
    }


    @Test
    public void whenInsertNullCarComfort_thenVerifyException() {
        String exceptionMessage = "Car comfort must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carComfortService.insert(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carComfortRepositoryMock);
    }


    @Test
    public void whenInsertInvalidCarComfort_thenVerifyException() {
        CarComfort carComfort = new CarComfort();

        String exceptionMessage = "Car comfort has invalid data";
        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carComfortService.insert(carComfort));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carComfortRepositoryMock);
    }


    @Test
    public void whenGetAll_thenVerifyListOfCarComforts() {
        List<CarComfort> carComforts = buildCarComfortList();

        when(carComfortRepositoryMock.getAll()).thenReturn(carComforts);

        List<CarComfort> response = carComfortService.getAll();

        assertNotNull(response);
        assertEquals(carComforts.size(), response.size());
        assertArrayEquals(carComforts.toArray(), response.toArray());

        verify(carComfortRepositoryMock).getAll();
        verifyNoMoreInteractions(carComfortRepositoryMock);
    }


    @Test
    public void whenGetByCorrectId_thenVerifyCarComfort() {
        UUID id = UUID.randomUUID();
        CarComfort carComfort = buildCarComfort(UUID.randomUUID());

        when(carComfortRepositoryMock.getById(id)).thenReturn(carComfort);
        CarComfort response = carComfortService.getById(id);

        assertNotNull(response);
        assertEquals(carComfort, response);

        verify(carComfortRepositoryMock).getById(id);
        verifyNoMoreInteractions(carComfortRepositoryMock);
    }


    @Test
    public void whenGetByInvalidId_thenVerifyException() {
        String exceptionMessage = "Car comfort id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carComfortService.getById(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carComfortRepositoryMock);
    }


    @Test
    public void whenGetByAbsentId_thenVerifyException() {
        UUID id = UUID.randomUUID();
        String exceptionMessage = String.format("Car comfort with id %s not found", id);

        when(carComfortRepositoryMock.getById(id)).thenThrow(new CarRentalException(exceptionMessage));
        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carComfortService.getById(id));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(carComfortRepositoryMock).getById(id);
        verifyNoMoreInteractions(carComfortRepositoryMock);
    }


    @Test
    public void whenUpdateCorrectCarComfort_thenVerifyCarComfort() {
        CarComfort carComfort = buildCarComfort(UUID.randomUUID());

        when(carComfortRepositoryMock.isExistById(carComfort.getId())).thenReturn(Boolean.TRUE);
        when(carComfortRepositoryMock.update(carComfort)).thenReturn(carComfort);

        CarComfort response = carComfortService.update(carComfort);
        assertNotNull(response);

        assertEquals(carComfort, response);

        verify(carComfortRepositoryMock).isExistById(carComfort.getId());
        verify(carComfortRepositoryMock).update(carComfort);
        verifyNoMoreInteractions(carComfortRepositoryMock);
    }


    @Test
    public void whenUpdateNullCarComfort_thenVerifyException() {
        String exceptionMessage = "Car comfort must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carComfortService.update(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carComfortRepositoryMock);
    }


    @Test
    public void whenUpdateInvalidCarComfort_thenVerifyException() {
        CarComfort invalidCarComfort = new CarComfort();
        String exceptionMessage = "Car comfort has invalid data";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carComfortService.update(invalidCarComfort));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carComfortRepositoryMock);
    }


    @Test
    public void whenUpdateAbsentCarComfort_thenVerifyException() {
        CarComfort absentCarComfort = buildCarComfort(UUID.randomUUID());
        String exceptionMessage = String.format("Car comfort with id %s not found", absentCarComfort.getId());

        when(carComfortRepositoryMock.isExistById(absentCarComfort.getId())).thenReturn(Boolean.FALSE);

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carComfortService.update(absentCarComfort));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(carComfortRepositoryMock).isExistById(absentCarComfort.getId());
        verify(carComfortRepositoryMock, never()).update(absentCarComfort);
        verifyNoMoreInteractions(carComfortRepositoryMock);
    }


    @Test
    public void whenDeleteCorrectCarComfort_thenVerifyTrue() {
        CarComfort carComfort = buildCarComfort(UUID.randomUUID());

        when(carComfortRepositoryMock.getById(carComfort.getId())).thenReturn(carComfort);
        when(carComfortRepositoryMock.update(carComfort)).thenReturn(carComfort);

        boolean result = carComfortService.delete(carComfort.getId());

        assertTrue(result);

        verify(carComfortRepositoryMock).getById(carComfort.getId());
        verify(carComfortRepositoryMock).update(carComfort);
        verifyNoMoreInteractions(carComfortRepositoryMock);
    }

    @Test
    public void whenDeleteWithInvalidId_thenVerifyException() {
        String exceptionMessage = "Can't remove the car comfort, its id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carComfortService.delete(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carComfortRepositoryMock);
    }


    @Test
    public void whenDeleteWithAbsentId_thenVerifyGetByIdException() {
        UUID absentId = UUID.randomUUID();
        String exceptionMessage = String.format("Car comfort with id %s not found", absentId);

        when(carComfortRepositoryMock.getById(absentId)).thenThrow(new CarRentalException(exceptionMessage));

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carComfortService.delete(absentId));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(carComfortRepositoryMock).getById(absentId);
        verify(carComfortRepositoryMock, never()).update(any(CarComfort.class));
        verifyNoMoreInteractions(carComfortRepositoryMock);
    }


    @Test
    public void whenDelete_thenVerifyUpdateException() {
        UUID id = UUID.randomUUID();
        CarComfort carComfort = buildCarComfort(id);
        String exceptionMessage = "Update exception";

        when(carComfortRepositoryMock.getById(id)).thenReturn(carComfort);
        when(carComfortRepositoryMock.update(carComfort)).thenThrow(new CarRentalException(exceptionMessage));

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carComfortService.delete(id));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(carComfortRepositoryMock).getById(id);
        verify(carComfortRepositoryMock).update(carComfort);
        verifyNoMoreInteractions(carComfortRepositoryMock);
    }


    private CarComfort buildCarComfort(UUID id) {
        CarComfort carComfort = new CarComfort();
        carComfort.setId(id);
        carComfort.setName("TestCarComfortName");
        carComfort.setDescription("TestCarComfortDescription");
        carComfort.setStatus(Boolean.TRUE);
        return carComfort;
    }


    private List<CarComfort> buildCarComfortList() {
        return List.of(
                buildCarComfort(UUID.randomUUID()),
                buildCarComfort(UUID.randomUUID()),
                buildCarComfort(UUID.randomUUID()));
    }
}