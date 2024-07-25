package entity.user;

import exceptions.UserDaoException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HibernateUtil;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDao.class);

    public User findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return session.get(User.class, id);
    }


    public void save(User user) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e){
            if (transaction != null) {
                transaction.rollback();
                throw new UserDaoException("Error with saving user", e);
            }
        }
    }

    public void update(User user) {

    }

    public void delete(User user) {

    }

    public List<User> findAll() {
        return List.of();
    }

    public Optional<User> findByEmail(String email) throws UserDaoException{
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            Query<User> query = session.createQuery("FROM User U WHERE U.email = :email", User.class);
            query.setParameter("email", email);
            List<User> result = query.list();
            return Optional.of(result.getFirst());
        }catch (NoSuchElementException e){
            log.info("Пользователь с такой почтой не найден: " + email, e);
            throw new UserDaoException("Пользователь с такой почтой не найден: " + email, e);
        }

    }
}
