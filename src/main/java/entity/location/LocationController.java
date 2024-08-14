package entity.location;

import Service.OpenWeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.user.UserDao;
import exceptions.BadRequestToServiceException;
import exceptions.OpenWeatherServiceException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.JwtUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@WebServlet(name = "locationController", value = "/location/*")
public class LocationController extends HttpServlet {
    private UserDao userDao;
    private LocationDao locationDao;
    private OpenWeatherService openWeatherService;

    public LocationController() {
        userDao = new UserDao();
        locationDao = new LocationDao();
        openWeatherService = new OpenWeatherService();
    }

    public LocationController(UserDao userDao, LocationDao locationDao, OpenWeatherService openWeatherService) {
        this.userDao = userDao;
        this.locationDao = locationDao;
        this.openWeatherService = openWeatherService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try{
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
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Нет такого эндпоинта");
                    break;
            }
        } catch (JsonProcessingException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при парсинге JSON: " + e.getMessage());
        } catch (OpenWeatherServiceException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Проблема с внешним сервисом погоды. " + e.getMessage());
        } catch (BadRequestToServiceException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Вы отправили некорректный запрос по поиску. " + e.getMessage());
        } catch (Exception e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void search(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    public void addLocation(HttpServletRequest req, HttpServletResponse resp)
            throws BadRequestToServiceException, OpenWeatherServiceException, IOException {
        String result;
        Long userId = Long.valueOf(req.getAttribute("userId").toString());
        String city = req.getReader().readLine();

        WeatherResponseDto weatherResponseDto = openWeatherService.getWeather(city);

        Location location = Location.builder()
                .latitude(new BigDecimal(weatherResponseDto.getLat()))
                .longitude(new BigDecimal(weatherResponseDto.getLon()))
                .name(weatherResponseDto.getName())
                .userId(userId)
                .build();

        locationDao.save(location);
        result = "Локация успешно добавлена к вам в профиль";
        resp.getWriter().write(result);
    }

    public void getLocation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    public void removeLocation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
