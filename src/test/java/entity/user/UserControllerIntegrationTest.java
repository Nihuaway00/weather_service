package entity.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import exceptions.UserAlreadyExistException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import utils.HibernateTestUtil;

import java.io.*;

@Testcontainers
class UserControllerIntegrationTest {
    private UserController userController;
    private UserService userService;
    private UserDao userDao;


    @BeforeEach
    public void setUp() throws Exception {
        HibernateTestUtil.startContainerAndSetupSessionFactory();
        HibernateTestUtil.clearDatabase();

        userDao = new UserDao(HibernateTestUtil.getSessionFactory());
        userService = new UserService(userDao);
        userController = new UserController(userService);
    }

    @AfterAll
    public static void afterAll(){
        HibernateTestUtil.clearDatabase();
    }

    @Test
    void shouldSuccessRegister() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter responseWriter = new StringWriter();

        when(request.getPathInfo()).thenReturn("/register");
        String data = "{\"name\": \"name\", \"email\": \"email\", \"password\": \"123\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(data)));
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        userController.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void shouldThrowUserAlreadyExistException() throws Exception {
        User user = new User.builder().email("email").password("123").build();
        userDao.save(user);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter responseWriter = new StringWriter();

        when(request.getPathInfo()).thenReturn("/register");
        String data = "{\"name\": \"name\", \"email\": \"email\", \"password\": \"123\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(data)));
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        userController.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
    }
}