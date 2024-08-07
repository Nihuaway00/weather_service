package entity.location;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "locationController", value = "/location/*")
public class LocationController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getPathInfo().substring(1);
        switch (action) {
            case "search":
                search(req, resp);
                break;
            case "add":
                addLocation(req, resp);
                break;
            case "get":
                getLocation(req, resp);
                break;
            case "remove":
                removeLocation(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
    }

    public void search(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    public void addLocation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    public void getLocation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    public void removeLocation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
