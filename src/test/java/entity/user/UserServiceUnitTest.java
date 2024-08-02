package entity.user;

import exceptions.UserAlreadyExistException;
import exceptions.UserDaoException;
import exceptions.UserSavingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {
    @Mock
    private UserDao userDaoMock;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldSuccessRegister(){
        User user = new User.Builder().name("name").email("email@gfl@.com").build();
        user.setId(1L);

        when(userDaoMock.existsByEmail("email@gfl.com")).thenReturn(false);
        when(userDaoMock.save(any(User.class))).thenReturn(user);

        UserRegistrationRequest dto = UserRegistrationRequest.builder().name("name").email("email@gfl.com").password("123").build();

        assertDoesNotThrow(() -> userService.register(dto));
    }

    @Test
    public void shouldThrowUserAlreadyExistsException(){
        User user = new User.Builder().name("name").email("email@gfl@.com").build();
        user.setId(1L);

        when(userDaoMock.existsByEmail("email@gfl.com")).thenReturn(true);

        UserRegistrationRequest dto = UserRegistrationRequest.builder().name("name").email("email@gfl.com").password("123").build();
        assertThrows(UserAlreadyExistException.class, () -> userService.register(dto));
    }

    @Test
    public void shouldThrowPersistException(){
        User user = new User.Builder().name("name").email("email@gfl@.com").build();
        user.setId(1L);

        when(userDaoMock.existsByEmail("email@gfl.com")).thenReturn(false);
        when(userDaoMock.save(any(User.class))).thenThrow(UserDaoException.class);

        UserRegistrationRequest dto = UserRegistrationRequest.builder().name("name").email("email@gfl.com").password("123").build();
        assertThrows(UserSavingException.class, () -> userService.register(dto));
    }
}