package github.sql4j.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagers {

    private static final EntityManager ENTITY_MANAGER = doGetEntityManager();

    private EntityManagers() {
    }

    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER;
    }

    private static EntityManager doGetEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("org.hibernate.jpa");
        return factory.createEntityManager();
    }


}
