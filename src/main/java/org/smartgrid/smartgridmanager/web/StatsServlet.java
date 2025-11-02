package org.smartgrid.smartgridmanager.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

import org.smartgrid.smartgridmanager.dao.DeviceDao; // Importer DeviceDao
import org.smartgrid.smartgridmanager.dao.StatsDao;
import org.smartgrid.smartgridmanager.model.Device; // Importer Device
import org.smartgrid.smartgridmanager.model.Reading; // Importer Reading

@WebServlet("/stats")
public class StatsServlet extends HttpServlet {

    private final StatsDao statsDao = new StatsDao();
    private final DeviceDao deviceDao = new DeviceDao(); // Ajouter le DAO des devices

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // --- 1. Récupération des statistiques existantes ---
        List<Object[]> stats = statsDao.getStats();
        List<Object[]> counts = statsDao.getCounts();
        List<Object[]> avgPower = statsDao.getAveragePowerByDevice();
        List<Object[]> avgTemp = statsDao.getAverageTemperatureByDevice();
        List<Object[]> alerts = statsDao.getAlerts(); // (Sera trié à l'étape 2)

        req.setAttribute("stats", stats);
        req.setAttribute("counts", counts);
        req.setAttribute("avgPower", avgPower);
        req.setAttribute("avgTemp", avgTemp);
        req.setAttribute("alerts", alerts);

        // --- 2. Ajout des KPI (Corrigé) ---
        Long deviceCount = statsDao.getDeviceCount();
        Long readingCount = statsDao.getReadingCount();
        Double avgPowerGlobal = statsDao.getGlobalAveragePower();

        Long totalAlertCount = statsDao.getTotalAlertCount();
        req.setAttribute("alertCount", totalAlertCount);

        req.setAttribute("deviceCount", deviceCount);
        req.setAttribute("readingCount", readingCount);
        req.setAttribute("avgPowerGlobal", String.format("%.2f", avgPowerGlobal));

        // --- 3. Données pour le nouveau graphique (Étape 4) ---
        List<Device> devices = deviceDao.findAll();
        req.setAttribute("devices", devices);

        String deviceIdParam = req.getParameter("chartDeviceId");
        String typeParam = req.getParameter("chartReadingType");

        if (deviceIdParam != null && !deviceIdParam.isEmpty() && typeParam != null && !typeParam.isEmpty()) {
            try {
                Long deviceId = Long.parseLong(deviceIdParam);
                // (Nous allons créer cette méthode "getReadingsForChart" à l'étape 4)
                List<Reading> chartReadings = statsDao.getReadingsForChart(deviceId, typeParam);

                req.setAttribute("chartReadings", chartReadings);
                req.setAttribute("selectedChartDeviceId", deviceId);
                req.setAttribute("selectedChartReadingType", typeParam);
            } catch (NumberFormatException e) {
                // Gérer l'erreur si l'ID n'est pas un nombre
                e.printStackTrace();
            }
        }

        req.getRequestDispatcher("pages/stats.jsp").forward(req, resp);
    }
}