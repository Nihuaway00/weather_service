package exceptions;

public class UserWithEmailNotExists extends Exception{
    public UserWithEmailNotExists(String message) {super(message);}
    public UserWithEmailNotExists(String message, Throwable cause) {super(message, cause);}
}
