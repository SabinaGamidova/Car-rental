import exception.CarRentalException;
import models.people.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.UserRepository;
import services.user.UserService;

import java.util.Date;
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
public class UserServiceTest {
    @Mock
    private UserRepository userRepositoryMock;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepositoryMock);
        assertNotNull(userRepositoryMock);
        assertNotNull(userService);
    }

    @Test
    public void whenInsertCorrectUser_thenVerifyUser() {
        User user = buildUser(UUID.randomUUID());

        when(userRepositoryMock.insert(user)).thenReturn(user);
        User response = userService.insert(user);

        assertNotNull(response);
        assertEquals(user, response);

        verify(userRepositoryMock).insert(user);
        verifyNoMoreInteractions(userRepositoryMock);
    }


    @Test
    public void whenInsertNullUser_thenVerifyException() {
        String exceptionMessage = "User must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> userService.insert(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(userRepositoryMock);
    }


    @Test
    public void whenInsertInvalidUser_thenVerifyException() {
        User user = new User();

        String exceptionMessage = "User has invalid data";
        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> userService.insert(user));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(userRepositoryMock);
    }


    @Test
    public void whenGetAll_thenVerifyListOfUsers() {
        List<User> users = buildUserList();

        when(userRepositoryMock.getAll()).thenReturn(users);

        List<User> response = userService.getAll();

        assertNotNull(response);
        assertEquals(users.size(), response.size());
        assertArrayEquals(users.toArray(), response.toArray());

        verify(userRepositoryMock).getAll();
        verifyNoMoreInteractions(userRepositoryMock);
    }


    @Test
    public void whenGetByCorrectId_thenVerifyUser() {
        UUID id = UUID.randomUUID();
        User user = buildUser(UUID.randomUUID());

        when(userRepositoryMock.getById(id)).thenReturn(user);
        User response = userService.getById(id);

        assertNotNull(response);
        assertEquals(user, response);

        verify(userRepositoryMock).getById(id);
        verifyNoMoreInteractions(userRepositoryMock);
    }


    @Test
    public void whenGetByInvalidId_thenVerifyException() {
        String exceptionMessage = "Car comfort id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> userService.getById(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(userRepositoryMock);
    }


    @Test
    public void whenGetByAbsentId_thenVerifyException() {
        UUID id = UUID.randomUUID();
        String exceptionMessage = String.format("User with id %s not found", id);

        when(userRepositoryMock.getById(id)).thenThrow(new CarRentalException(exceptionMessage));
        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> userService.getById(id));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(userRepositoryMock).getById(id);
        verifyNoMoreInteractions(userRepositoryMock);
    }


    @Test
    public void whenUpdateCorrectUser_thenVerifyUser() {
        User user = buildUser(UUID.randomUUID());

        when(userRepositoryMock.isExistById(user.getId())).thenReturn(Boolean.TRUE);
        when(userRepositoryMock.update(user)).thenReturn(user);

        User response = userService.update(user);
        assertNotNull(response);

        assertEquals(user, response);

        verify(userRepositoryMock).isExistById(user.getId());
        verify(userRepositoryMock).update(user);
        verifyNoMoreInteractions(userRepositoryMock);
    }


    @Test
    public void whenUpdateNullUser_thenVerifyException() {
        String exceptionMessage = "User must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> userService.update(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(userRepositoryMock);
    }


    @Test
    public void whenUpdateInvalidUser_thenVerifyException() {
        User invalidUser = new User();
        String exceptionMessage = "User has invalid data";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> userService.update(invalidUser));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(userRepositoryMock);
    }


    @Test
    public void whenUpdateAbsentUser_thenVerifyException() {
        User absentUser = buildUser(UUID.randomUUID());
        String exceptionMessage = String.format("User with id %s not found", absentUser.getId());

        when(userRepositoryMock.isExistById(absentUser.getId())).thenReturn(Boolean.FALSE);

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> userService.update(absentUser));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(userRepositoryMock).isExistById(absentUser.getId());
        verify(userRepositoryMock, never()).update(absentUser);
        verifyNoMoreInteractions(userRepositoryMock);
    }


    @Test
    public void whenDeleteCorrectUser_thenVerifyTrue() {
        User user = buildUser(UUID.randomUUID());

        when(userRepositoryMock.getById(user.getId())).thenReturn(user);
        when(userRepositoryMock.update(user)).thenReturn(user);

        boolean result = userService.delete(user.getId());

        assertTrue(result);

        verify(userRepositoryMock).getById(user.getId());
        verify(userRepositoryMock).update(user);
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void whenDeleteWithInvalidId_thenVerifyException() {
        String exceptionMessage = "Can't remove the user, its id must be NOT null";

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> userService.delete(null));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verifyNoInteractions(userRepositoryMock);
    }


    @Test
    public void whenDeleteWithAbsentId_thenVerifyGetByIdException() {
        UUID absentId = UUID.randomUUID();
        String exceptionMessage = String.format("User with id %s not found", absentId);

        when(userRepositoryMock.getById(absentId)).thenThrow(new CarRentalException(exceptionMessage));

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> userService.delete(absentId));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(userRepositoryMock).getById(absentId);
        verify(userRepositoryMock, never()).update(any(User.class));
        verifyNoMoreInteractions(userRepositoryMock);
    }


    @Test
    public void whenDelete_thenVerifyUpdateException() {
        UUID id = UUID.randomUUID();
        User user = buildUser(id);
        String exceptionMessage = "Update exception";

        when(userRepositoryMock.getById(id)).thenReturn(user);
        when(userRepositoryMock.update(user)).thenThrow(new CarRentalException(exceptionMessage));

        CarRentalException exception = assertThrows(
                CarRentalException.class, () -> userService.delete(id));

        assertNotNull(exception);
        assertEquals(exceptionMessage, exception.getMessage());

        verify(userRepositoryMock).getById(id);
        verify(userRepositoryMock).update(user);
        verifyNoMoreInteractions(userRepositoryMock);
    }


    private User buildUser(UUID id) {
        User user = new User();
        user.setId(id);
        user.setName("TestUserName");
        user.setSurname("TestUserSurname");
        user.setPatronymic("TestUserPatronymic");
        user.setDateOfBirth(new Date());
        user.setRoleId(UUID.randomUUID());
        user.setEmail("TestUserEmail");
        user.setPassword("TestUserPassword");
        user.setStatus(Boolean.TRUE);
        return user;
    }


    private List<User> buildUserList() {
        return List.of(
                buildUser(UUID.randomUUID()),
                buildUser(UUID.randomUUID()),
                buildUser(UUID.randomUUID()));
    }
}