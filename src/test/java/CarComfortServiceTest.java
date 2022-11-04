import exception.CarRentalException;
import models.cars.CarComfort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.CarComfortRepository;
import services.car.CarComfortService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


public class CarComfortServiceTest {
    private CarComfortRepository carComfortRepositoryMock;
    private CarComfortService carComfortService;

    @BeforeEach
    public void setUp() {
        carComfortRepositoryMock = mock(CarComfortRepository.class);
        assertNotNull(carComfortRepositoryMock);

        carComfortService = new CarComfortService(carComfortRepositoryMock);
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
        String exceptionMessage = "Car comfort with such an id not found";

        when(carComfortRepositoryMock.getById(id)).thenReturn(null);
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

        when(carComfortRepositoryMock.update(carComfort)).thenReturn(carComfort);

        CarComfort response = carComfortService.update(carComfort);
        assertNotNull(response);

        assertEquals(carComfort.getId(), response.getId());
        assertEquals(carComfort.getName(), response.getName());
        assertEquals(carComfort.getDescription(), response.getDescription());

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
        CarComfort carComfort = new CarComfort();

        String exceptionMessage = "Car comfort has invalid data";
        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carComfortService.update(carComfort));
        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carComfortRepositoryMock);
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
    public void whenDeleteCorrectCarComfort_thenVerifyCarComfort() {
        CarComfort carComfort = buildCarComfort(UUID.randomUUID());
        carComfortRepositoryMock.delete(carComfort.getId());
        assertNotNull(carComfort);
        verify(carComfortRepositoryMock).delete(carComfort.getId());
        verifyNoMoreInteractions(carComfortRepositoryMock);
    }


    @Test
    public void whenDeleteWithAbsentId_thenVerifyException() {
        UUID id = UUID.randomUUID();
        String exceptionMessage = "Car comfort by such an id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carComfortService.delete(id));
        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());
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