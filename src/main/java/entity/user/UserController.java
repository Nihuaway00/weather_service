package entity.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.user.dto.UserRegistrationDto;
import exceptions.UserAlreadyExistException;
import exceptions.UserDaoException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="userController", value = "/user/*")
public class UserController extends HttpServlet {
    private UserService userService;

    public UserController() {
        this.userService = new UserService(new UserDao());
    }

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + "heeloop" + "</h1>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo().substring(1);
        switch (action) {
            case "register":
                register(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
    }

    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result;

        try{
            String body = request.getReader().readLine();

            ObjectMapper objectMapper = new ObjectMapper();
            UserRegistrationDto dto = objectMapper.readValue(body, UserRegistrationDto.class);

            userService.register(dto);

            result = "Вы зареганы";
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (UserAlreadyExistException e) {
            result = "Такая почта уже используется";
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } catch (UserDaoException e) {
            result = "Ошибка на сервере: " + e.getMessage();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException e){
            result = "Ошибка на сервере при преобразовании в json: " + e.getMessage();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(result);
    }
}
