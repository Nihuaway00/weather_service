package entity.location;

import Service.OpenWeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.user.User;
import entity.user.UserDao;
import entity.user.UserPayloadDto;
import exceptions.BadRequestToServiceException;
import exceptions.LocationControllerException;
import exceptions.OpenWeatherServiceException;
import exceptions.UserNotExists;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.http.client.utils.URIBuilder;
import org.hibernate.Session;
import utils.HibernateUtil;
import utils.HttpClientUtil;
import utils.JwtUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
        } catch (ExpiredJwtException e){
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Токен истек. Вам нужна повторная авторизация: " + e.getMessage());
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
            throws BadRequestToServiceException, OpenWeatherServiceException, ExpiredJwtException, IOException {
        String result;
        HttpSession session = req.getSession();
        String token = session.getAttribute("token").toString();
        Jwt<?, ?> jwt = JwtUtil.parseToken(token);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> payload = objectMapper.readValue(JwtUtil.parsePayloadToJson(jwt.getPayload().toString()), Map.class);

        Long userId = Long.valueOf(payload.get("id").toString());

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
