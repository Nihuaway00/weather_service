package entity.user;

import exceptions.UserDaoException;
import exceptions.UserWithEmailNotExists;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HibernateUtil;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UserDao {
    private static Logger log = LoggerFactory.getLogger(UserDao.class);
    SessionFactory sessionFactory;

    public UserDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User findById(Long id) {
        try(Session session = sessionFactory.openSession()){
            return session.get(User.class, id);
        }catch (Exception e){
            throw new UserDaoException("Error with getting user", e);
        }
    }


    public User save(User user) throws UserDaoException {
        Transaction transaction = null;
        Session session = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return user;
        } catch (Exception e){
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (RuntimeException re) {
                    log.error("Could not roll back transaction", re);
                }
            }
            throw new UserDaoException("Error with saving user", e);
        }finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public User findByEmail(String email) throws UserDaoException{
        try(Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("FROM User U WHERE U.email = :email", User.class);
            query.setParameter("email", email);
            List<User> result = query.list();
            return result.stream().findFirst().orElseThrow(() -> new UserWithEmailNotExists("Пользователь с такой почтой не найден: " + email));
        } catch (Exception e) {
            log.error("Ошибка при поиске пользователя с email: " + email, e);
            throw new UserDaoException("Ошибка при поиске пользователя с email: " + email, e);
        }

    }

    public boolean existsByEmail(String email) throws UserDaoException{
        try(Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("FROM User U WHERE U.email = :email", User.class);
            query.setParameter("email", email);
            List<User> result = query.list();
            return result.stream().findFirst().isPresent();
        }catch (Exception e) {
            log.error("Ошибка при поиске пользователя с email: " + email, e);
            throw new UserDaoException("Ошибка при поиске пользователя с email: " + email, e);
        }
    }
}
