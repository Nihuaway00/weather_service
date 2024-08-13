package exceptions;

public class BadRequestToServiceException extends RuntimeException{
    public BadRequestToServiceException(String message) {
        super(message);
    }
    public BadRequestToServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
