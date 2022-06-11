package net.contal.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

/**
 * @author Bo Li
 */
@Component
public class DbUtils {
        private SessionFactory hibernateFactory;
        @Autowired
        public DbUtils(EntityManagerFactory factory) {
            if(factory.unwrap(SessionFactory.class) == null){
                throw new NullPointerException("factory is not a hibernate factory");
            }
            this.hibernateFactory = factory.unwrap(SessionFactory.class);
        }

        public SessionFactory getHibernateFactory() {
            return hibernateFactory;
        }

        public Session openASession(){
            if(hibernateFactory != null){
                Session se = hibernateFactory.openSession();
                    se.beginTransaction();
                    return se;
            }
            return null;
        }

}
