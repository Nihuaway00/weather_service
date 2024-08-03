package utils;

import entity.location.Location;
import entity.session.Session;
import entity.user.User;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory = initSessionFactory();

    protected static SessionFactory initSessionFactory() {
        try{
            // Создание стандартного реестра служб
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();

            // Создание метаданных
            Metadata metadata = new MetadataSources(standardRegistry)
                    .addAnnotatedClass(entity.user.User.class)
                    .getMetadataBuilder()
                    .build();

            // Построение фабрики сессий
            return metadata.getSessionFactoryBuilder().build();
        }catch(Throwable e){
            System.out.println(e.getMessage());
            throw new ExceptionInInitializerError(e);
        }

    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
