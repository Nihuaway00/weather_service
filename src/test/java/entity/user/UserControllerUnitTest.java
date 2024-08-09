package entity.user;

import exceptions.UserAlreadyExistException;
import exceptions.UserSavingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.JwtUtil;

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
    private UserDao userDaoMock;

    @InjectMocks
    private UserController userController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;


    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void shouldSuccessRegister() throws ServletException, IOException, UserAlreadyExistException, UserSavingException {
        when(request.getPathInfo()).thenReturn("/register");
        String data = "{\"email\": \"email\", \"password\": \"123\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(data)));
        when(userDaoMock.save(any(User.class)))
                .thenReturn(User.builder().id(1L).email("email").password("123").build());
        when(request.getSession()).thenReturn(session);

        userController.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void shouldThrowUserAlreadyExist() throws Exception {
        String data = "{\"email\": \"email\", \"password\": \"123\"}";

        when(request.getPathInfo()).thenReturn("/register");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(data)));
        doThrow(new UserAlreadyExistException("Пользователь с такой почтой уже существует")).when(userDaoMock).save(any(User.class));

        userController.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @Test
    void shouldSuccessLogin() throws ServletException, IOException, UserAlreadyExistException, UserSavingException {
        when(request.getPathInfo()).thenReturn("/login");
        String data = "{\"email\": \"email\", \"password\": \"123\"}";

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(data)));
        when(userDaoMock.findByEmail(anyString()))
                .thenReturn(User.builder().id(1L).email("email").password("123").build());
        when(request.getSession()).thenReturn(session);

        userController.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void shouldThrowInvalidPasswordException() throws ServletException, IOException, UserAlreadyExistException, UserSavingException {
        when(request.getPathInfo()).thenReturn("/login");
        String data = "{\"email\": \"email\", \"password\": \"\"}";

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(data)));
        when(userDaoMock.findByEmail(anyString()))
                .thenReturn(User.builder().id(1L).email("email").password("truePass").build());

        userController.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}