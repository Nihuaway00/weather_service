package utils;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

public class HibernateTestUtil {
    @Getter
    private static SessionFactory sessionFactory;
    private static MariaDBContainer<?> mariadb;


    public static void startContainerAndSetupSessionFactory() {
        DockerImageName imageDB = DockerImageName.parse("mariadb:11.2");
        mariadb = new MariaDBContainer<>(imageDB)
                .withDatabaseName("weather_service_test")
                .withUsername("test")
                .withPassword("test");

        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public static void stopContainer(){
        if (mariadb != null) {
            mariadb.stop();
        }
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public static void clearDatabase(){
        try (Session session = HibernateTestUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        }
    }
}