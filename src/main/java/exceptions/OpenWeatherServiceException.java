package exceptions;

public class OpenWeatherServiceException extends RuntimeException {
    public OpenWeatherServiceException(String message) {
        super(message);
    }

    public OpenWeatherServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
