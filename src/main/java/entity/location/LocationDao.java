package entity.location;

import entity.user.User;
import entity.user.UserDao;
import exceptions.LocationDaoException;
import exceptions.UserDaoException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HibernateUtil;

import java.util.List;

public class LocationDao {
    private static Logger log = LoggerFactory.getLogger(UserDao.class);
    private SessionFactory sessionFactory;

    public LocationDao(){
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public LocationDao(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public Location findById(Long id){
        try (Session session = sessionFactory.openSession()){
            return session.get(Location.class, id);
        } catch (HibernateException e){
            throw new LocationDaoException("Ошибка при получении локации по ID: " + id, e);
        }
    }

    public void save(Location location) throws LocationDaoException{
        Transaction transaction = null;
        Session session = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(location);
            transaction.commit();
        } catch (Exception e){
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (RuntimeException re) {
                    log.error("Не удалось откатить транзакцию", re);
                }
            }
            throw new LocationDaoException("Ошибка при сохранении локации", e);
        }finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
