package Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.location.WeatherResponseDto;
import exceptions.BadRequestToServiceException;
import exceptions.OpenWeatherServiceException;
import org.apache.http.client.utils.URIBuilder;
import utils.HttpClientUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class OpenWeatherService {
    private String API_KEY;
    private String getWeatherUrl;

    public OpenWeatherService() {
        this.API_KEY = System.getenv("OPENWEATHER_API_KEY");
        this.getWeatherUrl = "https://api.openweathermap.org/data/2.5/weather";
    }

    private boolean isOnlyLatin(String text) {
        Pattern pattern = Pattern.compile("[A-Za-z]+");
        return text.matches(pattern.pattern());
    }

    public WeatherResponseDto getWeather(String city) throws BadRequestToServiceException, OpenWeatherServiceException {
        try {
            if (city == null || city.isEmpty())
                throw new BadRequestToServiceException("Запрос для поиска не должен быть пустым");
            if (!isOnlyLatin(city))
                throw new BadRequestToServiceException("Запрос должен быть на английском");

            URI uri = new URIBuilder(getWeatherUrl)
                    .addParameter("q", URLEncoder.encode(city, StandardCharsets.UTF_8))
                    .addParameter("appid", API_KEY)
                    .build();

            String response = HttpClientUtil.GET(uri);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            return WeatherResponseDto.builder()
                    .name(jsonNode.get("name").asText())
                    .lon(jsonNode.get("lon").asDouble())
                    .lat(jsonNode.get("lat").asDouble())
                    .build();
        } catch (JsonProcessingException e){
            throw new OpenWeatherServiceException("Проблема с JSON ответом от OpenWeather: " + e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new BadRequestToServiceException("Проблема с синтаксисом запроса: " + e.getMessage(), e);
        } catch (InterruptedException | IOException e){
            throw new OpenWeatherServiceException("Проблема с отправкой запроса: " + e.getMessage(), e);
        }

    }

    public WeatherResponseDto getWeatherByCoords(Double lat, Double lon) throws BadRequestToServiceException, OpenWeatherServiceException {
        try{
            if (lat == null || lon == null)
                throw new BadRequestToServiceException("Координаты для поиска не должены быть пустыми");

            URI uri = new URIBuilder(getWeatherUrl)
                    .addParameter("lat", lat.toString())
                    .addParameter("lon", lon.toString())
                    .addParameter("appid", API_KEY)
                    .build();

            String response = HttpClientUtil.GET(uri);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            return WeatherResponseDto.builder()
                    .name(jsonNode.get("name").asText())
                    .lon(jsonNode.get("lon").asDouble())
                    .lat(jsonNode.get("lat").asDouble())
                    .build();
        } catch (JsonProcessingException e){
            throw new OpenWeatherServiceException("Проблема с JSON ответом от OpenWeather: " + e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new BadRequestToServiceException("Проблема с синтаксисом запроса: " + e.getMessage(), e);
        } catch (InterruptedException | IOException e){
            throw new OpenWeatherServiceException("Проблема с отправкой запроса: " + e.getMessage(), e);
        }
    }

}
