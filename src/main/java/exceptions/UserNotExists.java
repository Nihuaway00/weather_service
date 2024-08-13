package exceptions;

public class UserNotExists extends Exception{
    public UserNotExists(String message) {super(message);}
    public UserNotExists(String message, Throwable cause) {super(message, cause);}
}
