package entity.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import exceptions.UserAlreadyExistException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import utils.HibernateTestUtil;

import java.io.*;

@Testcontainers
class UserControllerIntegrationTest {
    static UserController userController;
    static UserService userService;
    static UserDao userDao;

    @BeforeAll
    static void setUp(){
        HibernateTestUtil.startContainerAndSetupSessionFactory();
        userDao = new UserDao(HibernateTestUtil.getSessionFactory());
        userService = new UserService(userDao);
        userController = new UserController(userService);
    }

    @BeforeEach
    void beforeEach() throws Exception {
        HibernateTestUtil.clearDatabase();
    }

    @AfterAll
    static void afterAll(){
        HibernateTestUtil.stopContainer();
        HibernateTestUtil.stopContainer();
    }

    @Test
    void shouldSuccessRegister() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter responseWriter = new StringWriter();

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        when(request.getPathInfo()).thenReturn("/register");
        String data = "{\"email\": \"email\", \"password\": \"123\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(data)));
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        userController.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void shouldThrowUserAlreadyExistException() throws Exception {
        User user = User.builder().email("email").build();
        userDao.save(user);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter responseWriter = new StringWriter();

        when(request.getPathInfo()).thenReturn("/register");
        String data = "{\"email\": \"email\", \"password\": \"123\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(data)));
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        userController.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
    }
}