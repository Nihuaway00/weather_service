package entity.user;

import exceptions.UserAlreadyExistException;
import exceptions.UserDaoException;
import exceptions.UserPersistException;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void register(UserRegistrationRequest dto) throws UserAlreadyExistException, UserPersistException {
        try {
            if (userDao.findByEmail(dto.getEmail()).isPresent()) {
                throw new UserAlreadyExistException("User with this email already exists");
            }
            User user = new User.Builder()
                    .name(dto.getName()).email(dto.getEmail())
                    .build();

            userDao.save(user);
        } catch (UserDaoException e) {
            throw new UserPersistException("User cannot be persist");
        }

    }
}
