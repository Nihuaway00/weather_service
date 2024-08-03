package entity.user;

import entity.user.dto.UserRegistrationDto;
import exceptions.UserAlreadyExistException;
import exceptions.UserDaoException;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void register(UserRegistrationDto dto) throws UserAlreadyExistException, UserDaoException {
        User user = new User.Builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();

        userDao.save(user);
    }
}
