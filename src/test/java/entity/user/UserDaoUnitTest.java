package entity.user;

import exceptions.UserDaoException;
import exceptions.UserWithEmailNotExists;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;
import utils.HibernateTestUtil;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoUnitTest {
    UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        HibernateTestUtil.startContainerAndSetupSessionFactory();
        HibernateTestUtil.clearDatabase();
    }

    @AfterAll
    static void afterAll() {
        HibernateTestUtil.stopContainer();
    }

    @BeforeEach
    void setUp(){
        userDao = new UserDao(HibernateTestUtil.getSessionFactory());
        HibernateTestUtil.clearDatabase();
    }

    @Test
    void shouldReturnUserById() {
        User user1 = new User.Builder().name("test1").email("test1@email.com").build();

        User user = userDao.save(user1);

        User resultUser = userDao.findById(user.getId());
        assertEquals(resultUser.getName(), user1.getName());
    }

    @Test
    void shouldReturnNull() {
        assertNull(userDao.findById(1L));
    }

    @Test
    void shouldReturnUserByEmail() {
        User user1 = new User.Builder().name("test1").email("test1@email.com").build();
        userDao.save(user1);

        User resultUser = userDao.findByEmail("test1@email.com");
        assertEquals(resultUser.getName(), user1.getName());
    }

    @Test
    void shouldReturnTrueIfUserWithEmailExist() {
        assertFalse(userDao.existsByEmail("test1@email.com"));

        User user1 = new User.Builder().name("test1").email("test1@email.com").build();
        userDao.save(user1);

        assertTrue(userDao.existsByEmail("test1@email.com"));
    }

    @Test
    void shouldThrowExceptionUserWithEmailDoesNotExist() {
        assertThrows(UserDaoException.class, () -> userDao.findByEmail("test1@email.com"));
    }

}