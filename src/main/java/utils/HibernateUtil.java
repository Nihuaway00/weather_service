package utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = initSessionFactory();

    protected static SessionFactory initSessionFactory() {
        try{
            return new Configuration()
                    .configure()
                    .buildSessionFactory();
        }catch(Throwable e){
            System.out.println(e.getMessage());
            throw new ExceptionInInitializerError(e);
        }

    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
