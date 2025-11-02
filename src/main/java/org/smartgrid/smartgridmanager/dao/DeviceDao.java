package org.smartgrid.smartgridmanager.dao;


import org.smartgrid.smartgridmanager.model.Device;
import jakarta.persistence.EntityManager;
import java.util.List;

public class DeviceDao {
    public void save(Device d) {
        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(d);
        em.getTransaction().commit();
        em.close();
    }
    public List<Device> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        List<Device> list = em.createQuery("SELECT d FROM Device d", Device.class).getResultList();
        em.close();
        return list;
    }
    public Device findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        Device d = em.find(Device.class, id);
        em.close();
        return d;
    }
    public List<Device> findAllWithReadings() {
        EntityManager em = JpaUtil.getEntityManager();
        List<Device> list = em.createQuery("SELECT DISTINCT d FROM Device d LEFT JOIN FETCH d.readings", Device.class)
                .getResultList();
        em.close();
        return list;
    }
    public void delete(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        Device d = em.find(Device.class, id);
        if (d != null) {
            em.remove(d);
        }
        em.getTransaction().commit();
        em.close();
    }

}
