package exceptions;

public class LocationDaoException extends RuntimeException{
    public LocationDaoException(String message) {
        super(message);
    }

    public LocationDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
