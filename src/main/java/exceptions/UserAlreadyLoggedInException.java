package exceptions;

public class UserAlreadyLoggedInException extends Exception{
    public UserAlreadyLoggedInException(String message) {
        super(message);
    }
    public UserAlreadyLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }
}
