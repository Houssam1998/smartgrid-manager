package org.smartgrid.smartgridmanager.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.smartgrid.smartgridmanager.dao.StatsDao;
import org.smartgrid.smartgridmanager.dao.DeviceDao;
import org.smartgrid.smartgridmanager.dao.ReadingDao;
import org.smartgrid.smartgridmanager.model.Reading;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(HomeServlet.class.getName());
    private final StatsDao statsDao = new StatsDao();
    private final DeviceDao deviceDao = new DeviceDao();
    private final ReadingDao readingDao = new ReadingDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // 🔹 Compteurs globaux
            Long deviceCount = statsDao.getDeviceCount();
            Long readingCount = statsDao.getReadingCount();
            Double avgPowerGlobal = statsDao.getGlobalAveragePower();

            logger.info("Device count: " + deviceCount);
            logger.info("Reading count: " + readingCount);
            logger.info("Global avg power: " + avgPowerGlobal);

            req.setAttribute("deviceCount", deviceCount);
            req.setAttribute("readingCount", readingCount);
            req.setAttribute("avgPowerGlobal", String.format("%.2f", avgPowerGlobal));

            // 🔹 Données pour graphiques
            List<Object[]> avgPower = statsDao.getAveragePowerByDevice();
            logger.info("Avg power data size: " + (avgPower != null ? avgPower.size() : "null"));

            if (avgPower != null && !avgPower.isEmpty()) {
                for (Object[] row : avgPower) {
                    logger.info("Power data - Device: " + row[0] + ", Avg: " + row[1]);
                }
            }

            req.setAttribute("avgPower", avgPower);

            //  Nous appelons maintenant la nouvelle méthode pour la section spécialisée
            List<Object[]> securityAlerts = statsDao.getRecentSecurityAlerts(5);
            req.setAttribute("securityAlerts", securityAlerts);

            // 🔹 Alerts récentes
            List<Object[]> recentAlerts = statsDao.getRecentAlerts(5);
            req.setAttribute("recentAlerts", recentAlerts);
            //alertes totales
            Long totalAlertCount = statsDao.getTotalAlertCount();
            req.setAttribute("alertCount", totalAlertCount);

            // 🔹 Dernières lectures
            List<Reading> latestReadings = statsDao.getLatestReadings(5);
            req.setAttribute("latestReadings", latestReadings);

            // 🔹 Forward vers la page d'accueil
            req.getRequestDispatcher("/index.jsp").forward(req, resp);

        } catch (Exception e) {
            logger.severe("Error in HomeServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}