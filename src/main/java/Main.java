import entity.user.UserDao;
import entity.user.dto.UserRegistrationDto;
import entity.user.UserService;
import exceptions.UserAlreadyExistException;
import exceptions.UserSavingException;

public class Main {
    public static void main(String[] args) throws UserAlreadyExistException, UserSavingException {
        UserDao userDao = new UserDao();

        UserService userService = new UserService(userDao);
        userService.register(UserRegistrationDto.builder().email("fdsfs2").name("fldk").password("fmk3fi3").build());
    }
}
