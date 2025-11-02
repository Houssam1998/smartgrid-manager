package org.smartgrid.smartgridmanager.dao;


import org.smartgrid.smartgridmanager.model.Reading;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ReadingDao {

    private static final String HINT_BYPASS_CACHE = "jakarta.persistence.cache.retrieveMode";
    public void save(Reading r) {
        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(r);
        em.getTransaction().commit();
        em.close();
    }
    public List<Object[]> getStatsByType() {
        EntityManager em = JpaUtil.getEntityManager();
        List<Object[]> results = em.createQuery(
                "SELECT r.readingType, AVG(r.value), MIN(r.value), MAX(r.value) FROM Reading r GROUP BY r.readingType",
                Object[].class).getResultList();
        em.close();
        return results;
    }
    public List<Object[]> getCountByDevice() {
        EntityManager em = JpaUtil.getEntityManager();
        List<Object[]> results = em.createQuery(
                "SELECT r.device.name, COUNT(r) FROM Reading r GROUP BY r.device.name", Object[].class).getResultList();
        em.close();
        return results;
    }

    public List<Reading> getRecentReadings(int limit) {
        EntityManager em = JpaUtil.getEntityManager();
        List<Reading> results = em.createQuery(
                "SELECT r FROM Reading r ORDER BY r.timestamp DESC",
                Reading.class
        ).setMaxResults(limit).setHint(HINT_BYPASS_CACHE, "BYPASS").getResultList();
        em.close();
        return results;
    }

    public List<Reading> findByDevice(Long deviceId) {
        EntityManager em = JpaUtil.getEntityManager();
        List<Reading> list = em.createQuery("SELECT r FROM Reading r WHERE r.device.id = :id", Reading.class)
                .setParameter("id", deviceId)
                //.setMaxResults(50)
                .setHint(HINT_BYPASS_CACHE, "BYPASS")
                .getResultList();
        em.close();
        return list;
    }

    public List<Reading> findByType(String type) {
        EntityManager em = JpaUtil.getEntityManager();
        List<Reading> list = em.createQuery("SELECT r FROM Reading r WHERE r.readingType = :t", Reading.class)
                .setParameter("t", type)
                //.setMaxResults(50)
                .setHint(HINT_BYPASS_CACHE, "BYPASS")
                .getResultList();
        em.close();
        return list;
    }

    public List<Reading> findByDeviceAndType(Long deviceId, String type) {
        EntityManager em = JpaUtil.getEntityManager();
        List<Reading> list = em.createQuery(
                        "SELECT r FROM Reading r WHERE r.device.id = :id AND r.readingType = :t", Reading.class)
                .setParameter("id", deviceId)
                .setParameter("t", type)
                //.setMaxResults(50)
                .setHint(HINT_BYPASS_CACHE, "BYPASS")
                .getResultList();
        em.close();
        return list;
    }

    public List<Reading> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        List<Reading> list = em.createQuery("SELECT r FROM Reading r", Reading.class)
                .setHint(HINT_BYPASS_CACHE, "BYPASS").getResultList();
        em.close();
        return list;
    }



}
