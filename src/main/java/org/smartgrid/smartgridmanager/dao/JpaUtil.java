package org.smartgrid.smartgridmanager.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("smartgridPU");
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() { emf.close(); }
}
