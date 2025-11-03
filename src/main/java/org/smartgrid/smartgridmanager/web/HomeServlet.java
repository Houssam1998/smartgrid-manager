package org.smartgrid.smartgridmanager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Logger;

import org.smartgrid.smartgridmanager.dao.StatsDao;
import org.smartgrid.smartgridmanager.dao.DeviceDao;
import org.smartgrid.smartgridmanager.dao.ReadingDao;
import org.smartgrid.smartgridmanager.model.Reading;
import org.smartgrid.smartgridmanager.model.api_external.WeatherResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(HomeServlet.class.getName());
    private final StatsDao statsDao = new StatsDao();
    private final DeviceDao deviceDao = new DeviceDao();
    private final ReadingDao readingDao = new ReadingDao();

    private final HttpClient httpClient = HttpClient.newHttpClient();
    // 🔹 MODIFIÉ : Assurez-vous que le module JSR310 (pour les dates) est enregistré
    private final ObjectMapper jsonMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // ... (Votre code pour les KPI globaux reste le même) ...
            Long deviceCount = statsDao.getDeviceCount();
            Long readingCount = statsDao.getReadingCount();
            Double avgPowerGlobal = statsDao.getGlobalAveragePower();
            Long totalAlertCount = statsDao.getTotalAlertCount();

            req.setAttribute("deviceCount", deviceCount);
            req.setAttribute("readingCount", readingCount);
            req.setAttribute("avgPowerGlobal", String.format("%.2f", avgPowerGlobal));
            req.setAttribute("alertCount", totalAlertCount);

            // 🔹 SECTION API EXTERNE (MISE À JOUR) 🔹
            try {
                WeatherResponse weather = fetchExternalWeatherData();
                if (weather != null) {
                    if (weather.getCurrentWeather() != null) {
                        // 1. Envoyer la température actuelle
                        req.setAttribute("externalTemperature", weather.getCurrentWeather().getTemperature());
                    }
                    if (weather.getDaily() != null) {
                        // 2. Envoyer l'objet complet des prévisions
                        req.setAttribute("dailyForecast", weather.getDaily());
                    }
                }
            } catch (Exception e) {
                logger.warning("Erreur lors de la récupération de l'API météo: " + e.getMessage());
                req.setAttribute("externalTemperature", "N/A");
            }
            // 🔹 FIN DE LA SECTION 🔹

            // ... (Votre code pour les graphiques et les listes reste le même) ...
            List<Object[]> avgPower = statsDao.getAveragePowerByDevice();
            req.setAttribute("avgPower", avgPower);
            List<Object[]> securityAlerts = statsDao.getRecentSecurityAlerts(5);
            req.setAttribute("securityAlerts", securityAlerts);
            List<Object[]> recentAlerts = statsDao.getRecentAlerts(5);
            req.setAttribute("recentAlerts", recentAlerts);
            List<Reading> latestReadings = statsDao.getLatestReadings(5);
            req.setAttribute("latestReadings", latestReadings);

            req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);

        } catch (Exception e) {
            logger.severe("Error in HomeServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    /**
     * 🔹 MÉTHODE MISE À JOUR (URL pour Fès + prévisions 7 jours) 🔹
     */
    private WeatherResponse fetchExternalWeatherData() throws IOException, InterruptedException {
        // Latitude/Longitude pour Fès : 34.04, -5.00
        // Ajout de 'daily' pour les prévisions sur 7 jours
        String apiUrl = "https://api.open-meteo.com/v1/forecast" +
                "?latitude=34.04&longitude=-5.00" +
                "&current_weather=true" +
                "&daily=temperature_2m_max,temperature_2m_min" +
                "&timezone=auto"; // timezone=auto gère l'heure locale

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return jsonMapper.readValue(response.body(), WeatherResponse.class);
        } else {
            logger.warning("L'API météo a échoué avec le code: " + response.statusCode());
            return null;
        }
    }
}