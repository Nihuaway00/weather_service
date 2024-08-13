package exceptions;

public class LocationControllerException extends RuntimeException{
    public LocationControllerException(String message) {
        super(message);
    }
    public LocationControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}
