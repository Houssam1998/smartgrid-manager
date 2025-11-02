package org.smartgrid.smartgridmanager.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.smartgrid.smartgridmanager.model.Reading;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatsDao {

    private static final Logger logger = Logger.getLogger(StatsDao.class.getName());
    private static final String HINT_BYPASS_CACHE = "jakarta.persistence.cache.retrieveMode";

    private static final String ALERT_CRITERIA_JPQL =
            " (r.readingType = 'power' AND r.value > 5000) " +
                    " OR (r.readingType = 'temperature' AND r.value > 35) " +
                    " OR (r.readingType = 'voltage' AND r.value > 250) " +
                    " OR (r.readingType = 'current' AND r.value > 40) " +
                    " OR (r.readingType = 'humidity' AND r.value < 10) " + // Humidité basse
                    " OR (r.readingType = 'humidity' AND r.value > 90) " + // Humidité haute
                    " OR (r.readingType = 'co2' AND r.value > 1000) ";
    // Stats par type : avg, min, max
    public List<Object[]> getStats() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Object[]> stats = em.createQuery(
                            "SELECT r.readingType, AVG(r.value), MIN(r.value), MAX(r.value) " +
                                    "FROM Reading r GROUP BY r.readingType", Object[].class)
                    .getResultList();
            logger.info("Stats by type: " + stats.size() + " types found");
            return stats;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getStats", e);
            return new ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // Nombre de readings par device
    public List<Object[]> getCounts() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Object[]> counts = em.createQuery(
                            "SELECT d.name, COUNT(r.id) " +
                                    "FROM Device d LEFT JOIN d.readings r " +
                                    "GROUP BY d.name", Object[].class)
                    .getResultList();
            logger.info("Counts by device: " + counts.size() + " devices found");
            return counts;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getCounts", e);
            return new ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // 🔸 Moyenne de consommation par device (CORRIGÉE)
    public List<Object[]> getAveragePowerByDevice() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT d.name, AVG(r.value) " +
                    "FROM Reading r JOIN r.device d " +
                    "WHERE r.readingType = 'power' " +
                    "GROUP BY d.name " +
                    "ORDER BY AVG(r.value) DESC";

            logger.info("Executing JPQL: " + jpql);
            List<Object[]> result = em.createQuery(jpql, Object[].class)
                    .setMaxResults(10)
                    .setHint(HINT_BYPASS_CACHE, "BYPASS")
                    .getResultList();

            logger.info("Found " + result.size() + " power devices for chart");
            for (Object[] row : result) {
                logger.info("Device: " + row[0] + ", Avg Power: " + row[1]);
            }
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getAveragePowerByDevice", e);
            return new ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // Moyenne de température par device
    public List<Object[]> getAverageTemperatureByDevice() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Object[]> result = em.createQuery(
                            "SELECT d.name, AVG(r.value) " +
                                    "FROM Reading r JOIN r.device d " +
                                    "WHERE r.readingType = 'temperature' " +
                                    "GROUP BY d.name",
                            Object[].class)
                    .setMaxResults(10)
                    .setHint(HINT_BYPASS_CACHE, "BYPASS")
                    .getResultList();
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getAverageTemperatureByDevice", e);
            return new ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // Derniers readings anormaux
    public List<Object[]> getAlerts() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT d.name, r.readingType, r.value, r.timestamp " +
                    "FROM Reading r JOIN r.device d " +
                    "WHERE " + ALERT_CRITERIA_JPQL + // Utilise la définition
                    "ORDER BY r.timestamp DESC";

            List<Object[]> result = em.createQuery(jpql, Object[].class)
                    .setMaxResults(100) // Garder la limite pour l'affichage
                    .setHint(HINT_BYPASS_CACHE, "BYPASS")
                    .getResultList();
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getAlerts", e);
            return new ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // 🔸 Nombre total de devices
    public Long getDeviceCount() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(d) FROM Device d", Long.class).getSingleResult();
            logger.info("Device count: " + count);
            return count;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getDeviceCount", e);
            return 0L;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // 🔸 Nombre total de readings
    public Long getReadingCount() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(r) FROM Reading r", Long.class).getSingleResult();
            logger.info("Reading count: " + count);
            return count;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getReadingCount", e);
            return 0L;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // 🔸 Moyenne globale de la puissance
    public Double getGlobalAveragePower() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Double avg = em.createQuery(
                    "SELECT AVG(r.value) FROM Reading r WHERE r.readingType = 'power'",
                    Double.class).getSingleResult();
            Double result = (avg == null) ? 0.0 : Math.round(avg * 100.0) / 100.0;
            logger.info("Global average power: " + result);
            return result;
        } catch (NoResultException e) {
            logger.info("No power readings found, returning 0.0");
            return 0.0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getGlobalAveragePower", e);
            return 0.0;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // 🔸 Alertes récentes : dépassements de seuil (CORRIGÉE)
    public List<Object[]> getRecentAlerts(int limit) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT d.name, r.readingType, r.value, r.timestamp " +
                    "FROM Reading r JOIN r.device d " +
                    "WHERE " + ALERT_CRITERIA_JPQL + // Utilise la définition
                    "ORDER BY r.timestamp DESC";

            List<Object[]> res = em.createQuery(jpql, Object[].class)
                    .setMaxResults(limit)
                    .setHint(HINT_BYPASS_CACHE, "BYPASS")
                    .getResultList();

            logger.info("Found " + res.size() + " recent alerts");
            return res;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getRecentAlerts", e);
            return new ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public Long getTotalAlertCount() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(r) FROM Reading r JOIN r.device d WHERE " + ALERT_CRITERIA_JPQL;

            Long count = em.createQuery(jpql, Long.class).getSingleResult();

            logger.info("Total alert count: " + count);
            return (count != null) ? count : 0L;

        } catch (NoResultException e) {
            logger.info("No alerts found.");
            return 0L;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getTotalAlertCount", e);
            return 0L;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // 🔸 Dernières lectures (CORRIGÉE avec JOIN FETCH)
    public List<Reading> getLatestReadings(int limit) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            // Utilisation de JOIN FETCH pour éviter le problème N+1
            String jpql = "SELECT r FROM Reading r JOIN FETCH r.device d ORDER BY r.timestamp DESC";

            List<Reading> res = em.createQuery(jpql, Reading.class)
                    .setMaxResults(limit)
                    .setHint(HINT_BYPASS_CACHE, "BYPASS")
                    .getResultList();

            logger.info("Found " + res.size() + " latest readings");
            return res;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getLatestReadings", e);
            return new ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Reading> getReadingsForChart(Long deviceId, String type) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT r FROM Reading r " +
                    "WHERE r.device.id = :id AND r.readingType = :t " +
                    "ORDER BY r.timestamp ASC"; // Tri ASC pour un graphique chronologique

            List<Reading> readings = em.createQuery(jpql, Reading.class)
                    .setParameter("id", deviceId)
                    .setParameter("t", type)
                    .setMaxResults(1000) // Limiter à 1000 points pour la performance
                    .getResultList();

            logger.info("StatsDao: Found " + readings.size() + " readings for chart (Device: " + deviceId + ", Type: " + type + ")");
            return readings;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in getReadingsForChart", e);
            return new ArrayList<>();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
