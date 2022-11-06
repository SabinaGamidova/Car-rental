import exception.CarRentalException;
import models.cars.Car;
import models.cars.CarComfort;
import models.cars.CarType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CarRepository;
import services.car.CarService;

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
public class CarServiceTest {
    @Mock
    private CarRepository carRepositoryMock;
    private CarService carService;

    @BeforeEach
    public void setUp() {
        carService = new CarService(carRepositoryMock);
        assertNotNull(carRepositoryMock);
        assertNotNull(carService);
    }

    @Test
    public void whenInsertCorrectCarComfort_thenVerifyCarComfort() {
        Car car = buildCar(UUID.randomUUID());

        when(carRepositoryMock.insert(car)).thenReturn(car);
        Car response = carService.insert(car);

        assertNotNull(response);
        assertEquals(car, response);

        verify(carRepositoryMock).insert(car);
        verifyNoMoreInteractions(carRepositoryMock);
    }


    @Test
    public void whenInsertNullCar_thenVerifyException() {
        String exceptionMessage = "Car must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carService.insert(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carRepositoryMock);
    }


    @Test
    public void whenInsertInvalidCar_thenVerifyException() {
        Car car = new Car();

        String exceptionMessage = "Car has invalid data";
        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carService.insert(car));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carRepositoryMock);
    }


    @Test
    public void whenGetAll_thenVerifyListOfCars() {
        List<Car> cars = buildCarList();

        when(carRepositoryMock.getAll()).thenReturn(cars);

        List<Car> response = carService.getAll();

        assertNotNull(response);
        assertEquals(cars.size(), response.size());
        assertArrayEquals(cars.toArray(), response.toArray());

        verify(carRepositoryMock).getAll();
        verifyNoMoreInteractions(carRepositoryMock);
    }


    @Test
    public void whenGetByCorrectId_thenVerifyCar() {
        UUID id = UUID.randomUUID();
        Car car = buildCar(UUID.randomUUID());

        when(carRepositoryMock.getById(id)).thenReturn(car);
        Car response = carService.getById(id);

        assertNotNull(response);
        assertEquals(car, response);

        verify(carRepositoryMock).getById(id);
        verifyNoMoreInteractions(carRepositoryMock);
    }


    @Test
    public void whenGetByInvalidId_thenVerifyException() {
        String exceptionMessage = "Car id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carService.getById(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carRepositoryMock);
    }


    @Test
    public void whenGetByAbsentId_thenVerifyException() {
        UUID id = UUID.randomUUID();
        String exceptionMessage = String.format("Car with id %s not found", id);

        when(carRepositoryMock.getById(id)).thenThrow(new CarRentalException(exceptionMessage));
        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carService.getById(id));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(carRepositoryMock).getById(id);
        verifyNoMoreInteractions(carRepositoryMock);
    }


    @Test
    public void whenUpdateCorrectCar_thenVerifyCarComfort() {
        Car car = buildCar(UUID.randomUUID());

        when(carRepositoryMock.isExistById(car.getId())).thenReturn(Boolean.TRUE);
        when(carRepositoryMock.update(car)).thenReturn(car);

        Car response = carService.update(car);
        assertNotNull(response);

        assertEquals(car, response);

        verify(carRepositoryMock).isExistById(car.getId());
        verify(carRepositoryMock).update(car);
        verifyNoMoreInteractions(carRepositoryMock);
    }


    @Test
    public void whenUpdateNullCar_thenVerifyException() {
        String exceptionMessage = "Car must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carService.update(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carRepositoryMock);
    }


    @Test
    public void whenUpdateInvalidCar_thenVerifyException() {
        Car invalidCar = new Car();
        String exceptionMessage = "Car has invalid data";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carService.update(invalidCar));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carRepositoryMock);
    }


    @Test
    public void whenUpdateAbsentCar_thenVerifyException() {
        Car absentCar = buildCar(UUID.randomUUID());
        String exceptionMessage = String.format("Car with id %s not found", absentCar.getId());

        when(carRepositoryMock.isExistById(absentCar.getId())).thenReturn(Boolean.FALSE);

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carService.update(absentCar));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(carRepositoryMock).isExistById(absentCar.getId());
        verify(carRepositoryMock, never()).update(absentCar);
        verifyNoMoreInteractions(carRepositoryMock);
    }


    @Test
    public void whenDeleteCorrectCar_thenVerifyTrue() {
        Car car = buildCar(UUID.randomUUID());

        when(carRepositoryMock.getById(car.getId())).thenReturn(car);
        when(carRepositoryMock.update(car)).thenReturn(car);

        boolean result = carService.delete(car.getId());

        assertTrue(result);

        verify(carRepositoryMock).getById(car.getId());
        verify(carRepositoryMock).update(car);
        verifyNoMoreInteractions(carRepositoryMock);
    }


    @Test
    public void whenDeleteWithInvalidId_thenVerifyException() {
        String exceptionMessage = "Can't remove the car, its id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carService.delete(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carRepositoryMock);
    }


    @Test
    public void whenDeleteWithAbsentId_thenVerifyGetByIdException() {
        UUID absentId = UUID.randomUUID();
        String exceptionMessage = String.format("Car with id %s not found", absentId);

        when(carRepositoryMock.getById(absentId)).thenThrow(new CarRentalException(exceptionMessage));

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carService.delete(absentId));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(carRepositoryMock).getById(absentId);
        verify(carRepositoryMock, never()).update(any(Car.class));
        verifyNoMoreInteractions(carRepositoryMock);
    }


    @Test
    public void whenDelete_thenVerifyUpdateException() {
        UUID id = UUID.randomUUID();
        Car car = buildCar(id);
        String exceptionMessage = "Update exception";

        when(carRepositoryMock.getById(id)).thenReturn(car);
        when(carRepositoryMock.update(car)).thenThrow(new CarRentalException(exceptionMessage));

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carService.delete(id));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(carRepositoryMock).getById(id);
        verify(carRepositoryMock).update(car);
        verifyNoMoreInteractions(carRepositoryMock);
    }


    @Test
    public void whenGetAllByCarTypeId_thenVerifyListOfCars() {
        UUID id = UUID.randomUUID();
        List<Car> cars = buildCarList();

        when(carRepositoryMock.getByCarType(id)).thenReturn(cars);

        List<Car> response = carService.getByCarType(id);

        assertNotNull(response);
        assertEquals(cars.size(), response.size());
        assertArrayEquals(cars.toArray(), response.toArray());

        verify(carRepositoryMock).getByCarType(id);
        verifyNoMoreInteractions(carRepositoryMock);
    }


    @Test
    public void whenGetAllWithNullCarTypeId_thenVerifyException() {
        String exceptionMessage = "Car type id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carService.getByCarType(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carRepositoryMock);
    }



    @Test
    public void whenGetAllByCarComfortId_thenVerifyListOfCars() {
        UUID id = UUID.randomUUID();
        List<Car> cars = buildCarList();

        when(carRepositoryMock.getByCarComfort(id)).thenReturn(cars);

        List<Car> response = carService.getByCarComfort(id);

        assertNotNull(response);
        assertEquals(cars.size(), response.size());
        assertArrayEquals(cars.toArray(), response.toArray());

        verify(carRepositoryMock).getByCarComfort(id);
        verifyNoMoreInteractions(carRepositoryMock);
    }


    @Test
    public void whenGetAllWithNullCarComfortId_thenVerifyException() {
        String exceptionMessage = "Car comfort id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> carService.getByCarComfort(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(carRepositoryMock);
    }


    private Car buildCar(UUID id) {
        Car car = new Car();
        car.setId(id);
        car.setNumber("carNumber");
        car.setBrand("carBrand");
        car.setModel("carModel");
        car.setCarTypeId(UUID.randomUUID());
        car.setComfortId(UUID.randomUUID());
        car.setEngineId(UUID.randomUUID());
        car.setPrice(1D);
        car.setDeposit(1D);
        car.setStatus(Boolean.TRUE);
        return car;
    }


    private CarType buildCarType(UUID id) {
        CarType carType = new CarType();
        carType.setId(id);
        carType.setName("TestCarTypeName");
        carType.setStatus(Boolean.TRUE);
        return carType;
    }


    private CarComfort buildCarComfort(UUID id) {
        CarComfort carComfort = new CarComfort();
        carComfort.setId(id);
        carComfort.setName("TestCarComfortName");
        carComfort.setDescription("TestCarComfortDescription");
        carComfort.setStatus(Boolean.TRUE);
        return carComfort;
    }


    private List<Car> buildCarList() {
        return List.of(
                buildCar(UUID.randomUUID()),
                buildCar(UUID.randomUUID()),
                buildCar(UUID.randomUUID()));
    }
}