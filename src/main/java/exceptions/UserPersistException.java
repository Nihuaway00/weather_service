package exceptions;

public class UserPersistException extends Exception{
    public UserPersistException(String message) {
        super(message);
    }
    public UserPersistException(String message, Throwable cause) {
        super(message, cause);
    }
}
