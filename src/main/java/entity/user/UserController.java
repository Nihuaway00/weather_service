package entity.user;

import exceptions.UserAlreadyExistException;
import exceptions.UserPersistException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user")
public class UserController extends HttpServlet {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
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
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String email = request.getParameter("email");

            userService.register(UserRegistrationRequest.builder()
                    .email(email).name(name).password(password)
                    .build());

            result = "Вы зареганы";
        } catch (UserAlreadyExistException e) {
            result = "Такая почта уже используется";
        } catch (UserPersistException e) {
            result = "Ошибка на сервере";
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Registration Successful</h1>");
        out.println("</body></html>");
    }
}
