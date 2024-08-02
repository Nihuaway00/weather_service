package entity.user;

import exceptions.UserAlreadyExistException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;


    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void shouldSuccessRegister() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/register");
        String data = "{\"name\": \"name\", \"email\": \"email\", \"password\": \"123\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(data)));

        userController.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void shouldThrowUserAlreadyExist() throws Exception {
        String data = "{\"name\": \"name\", \"email\": \"email\", \"password\": \"123\"}";

        when(request.getPathInfo()).thenReturn("/register");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(data)));
        doThrow(new UserAlreadyExistException("Пользователь с такой почтой уже существует")).when(userService).register(any(UserRegistrationRequest.class));

        userController.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
    }
}