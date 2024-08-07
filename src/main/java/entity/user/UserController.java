package entity.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.user.dto.UserRegistrationDto;
import exceptions.UserAlreadyExistException;
import exceptions.UserDaoException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.JwtUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@WebServlet(name = "userController", value = "/user/*")
public class UserController extends HttpServlet {
    private final UserDao userDao;

    public UserController() {
        this.userDao = new UserDao();
    }

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo().substring(1);
        switch (action) {
            case "register":
                register(request, response);
                break;
            case "login":
                login(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
    }

    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result;

        try {
            String requestBody = request.getReader().readLine();

            ObjectMapper objectMapper = new ObjectMapper();
            UserRegistrationDto dto = objectMapper.readValue(requestBody, UserRegistrationDto.class);

            User user = User.builder()
                    .email(dto.getEmail())
                    .password(dto.getPassword())
                    .build();
            User registeredUser = userDao.save(user);

            Map<String, ?> claims = Map.of("id", registeredUser.getId(), "email", dto.getEmail());
            String jwt = JwtUtil.generateToken(claims, Date.from(Instant.ofEpochSecond(10L)));

            Cookie jwtCookie = new Cookie("jwt", jwt);
            jwtCookie.setHttpOnly(true);
            response.addCookie(jwtCookie);

            result = "Вы зареганы";
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (UserAlreadyExistException e) {
            result = "Такая почта уже используется";
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } catch (UserDaoException e) {
            result = "Ошибка при сохранении пользователя: " + e.getMessage();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException e) {
            result = "Ошибка на сервере при преобразовании в json: " + e.getMessage();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(result);
    }

    public void login(HttpServletRequest request, HttpServletResponse response) {

    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {

    }
}
