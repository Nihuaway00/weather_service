package entity.user;

import exceptions.UserAlreadyExistException;
import exceptions.UserDaoException;
import exceptions.UserSavingException;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void register(UserRegistrationRequest dto) throws UserAlreadyExistException, UserSavingException {
        try {
            if (userDao.existsByEmail(dto.getEmail())) {
                throw new UserAlreadyExistException("Пользователь с такой почтой уже существует");
            }
            User user = User.builder()
                    .name(dto.getName()).email(dto.getEmail())
                    .build();

            userDao.save(user);
        } catch (UserDaoException e) {
            throw new UserSavingException("Пользователь не может быть сохранен");
        }
    }
}
