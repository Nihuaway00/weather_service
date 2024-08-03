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
        User user = new User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();

        userDao.save(user);
    }
}
