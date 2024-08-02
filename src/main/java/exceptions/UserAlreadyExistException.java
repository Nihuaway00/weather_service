package exceptions;

public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException() {
        super("Пользователь уже сущетсвует");
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }
    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
