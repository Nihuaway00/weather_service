package entity.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.InvalidPasswordException;
import exceptions.UserAlreadyExistException;
import exceptions.UserAlreadyLoggedInException;
import exceptions.UserDaoException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.JwtUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Arrays;
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
            User user = objectMapper.readValue(requestBody, User.class);
            User registeredUser = userDao.save(user);

            Map<String, ?> claims = Map.of("id", registeredUser.getId(), "email", user.getEmail());
            String jwt = JwtUtil.generateToken(claims, Date.from(Instant.ofEpochSecond(10L)));

            HttpSession session = request.getSession();
            session.setAttribute("token", jwt);
            session.setAttribute("userId", registeredUser.getId());

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

    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result;
        HttpSession session = request.getSession();

        try{
            String requestBody = request.getReader().readLine();

            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(requestBody, User.class);

            User userFromDB = userDao.findByEmail(user.getEmail());

            if(!userFromDB.getPassword().equals(user.getPassword())){
                throw new InvalidPasswordException("Неверный пароль");
            }

            Map<String, ?> claims = Map.of("id", userFromDB.getId(), "email", user.getEmail());
            String jwt = JwtUtil.generateToken(claims, Date.from(Instant.ofEpochSecond(10L)));

            session.setAttribute("token", jwt);
            session.setAttribute("userId", userFromDB.getId());

            result = "Вы успешно авторизированы";
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (InvalidPasswordException e){
            result = e.getMessage();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(result);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    // Установить время жизни куки в 0, чтобы удалить его
                    cookie.setMaxAge(0);
                    cookie.setPath("/"); // Необходимо установить путь, иначе куки не будут удалены
                    response.addCookie(cookie);
                }
            }
            result = "Вы вышли";
        } else{
            result = "Вы и не входили...";
        }


        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(result);
    }
}
