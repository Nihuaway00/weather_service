package entity.user;

import exceptions.UserAlreadyExistException;
import exceptions.UserDaoException;
import exceptions.UserNotExists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import utils.HibernateTestUtil;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoUnitTest {
    static UserDao userDao;

    @BeforeAll
    static void setUp() {
        HibernateTestUtil.startContainerAndSetupSessionFactory();
        userDao = new UserDao(HibernateTestUtil.getSessionFactory());
    }

    @AfterAll
    static void afterAll() {
        HibernateTestUtil.stopContainer();
    }

    @BeforeEach
    void beforeEach(){
        HibernateTestUtil.clearDatabase();
    }

    @Test
    void shouldReturnUserById() throws UserAlreadyExistException, UserNotExists {
        User user1 = User.builder().password("test1").email("test1@email.com").build();

        User user = userDao.save(user1);

        User resultUser = userDao.findById(user.getId());
        assertEquals(resultUser.getEmail(), user1.getEmail());
    }

    @Test
    void shouldReturnNull() throws UserNotExists {
        assertNull(userDao.findById(1L));
    }

    @Test
    void shouldReturnUserByEmail() throws UserAlreadyExistException {
        User user1 = User.builder().password("test1").email("test1@email.com").build();
        userDao.save(user1);

        User resultUser = userDao.findByEmail("test1@email.com");
        assertEquals(resultUser.getEmail(), user1.getEmail());
    }

    @Test
    void shouldReturnTrueIfUserWithEmailExist() throws UserAlreadyExistException {
        assertFalse(userDao.existsByEmail("test1@email.com"));

        User user1 = User.builder().password("test1").email("test1@email.com").build();
        userDao.save(user1);

        assertTrue(userDao.existsByEmail("test1@email.com"));
    }

    @Test
    void shouldThrowExceptionUserWithEmailDoesNotExist() {
        assertThrows(UserDaoException.class, () -> userDao.findByEmail("test1@email.com"));
    }

}