package utils;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory = initSessionFactory();

    protected static SessionFactory initSessionFactory() {
        try{
            Configuration configuration = new Configuration().configure();
            configuration.setProperty("hibernate.connection.url", System.getenv("DATABASE_URL"));
            configuration.setProperty("hibernate.connection.username", System.getenv("DATABASE_USER"));
            configuration.setProperty("hibernate.connection.password", System.getenv("DATABASE_PASSWORD"));
            return configuration.buildSessionFactory();
        }catch(Throwable e){
            System.out.println(e.getMessage());
            throw new ExceptionInInitializerError(e);
        }

    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
