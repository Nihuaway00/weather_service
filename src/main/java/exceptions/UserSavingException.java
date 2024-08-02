package exceptions;

public class UserSavingException extends Exception{
    public UserSavingException(String message) {
        super(message);
    }
    public UserSavingException(String message, Throwable cause) {
        super(message, cause);
    }
}
