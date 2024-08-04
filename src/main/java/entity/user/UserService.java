package entity.user;

import exceptions.UserAlreadyExistException;
import exceptions.UserDaoException;
import exceptions.UserSavingException;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User register(UserRegistrationRequest dto) throws UserAlreadyExistException, UserSavingException {
        User user = User.builder()
                .email(dto.getEmail())
                .build();

        return userDao.save(user);
    }
}
