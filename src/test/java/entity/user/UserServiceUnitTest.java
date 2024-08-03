package entity.user;

import entity.user.dto.UserRegistrationDto;
import exceptions.UserAlreadyExistException;
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
    public void shouldSuccessRegister() throws UserAlreadyExistException {
        User user = User.builder().email("email@gfl@.com").build();
        user.setId(1L);

        when(userDaoMock.save(any(User.class))).thenReturn(user);

        UserRegistrationDto dto = UserRegistrationDto.builder().email("email@gfl.com").password("123").build();

        assertDoesNotThrow(() -> userService.register(dto));
    }
}