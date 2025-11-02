package org.smartgrid.smartgridmanager.web;

import org.smartgrid.smartgridmanager.dao.JpaUtil;
import org.smartgrid.smartgridmanager.model.Device;
import org.smartgrid.smartgridmanager.model.Reading;
import jakarta.persistence.EntityManager;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

@WebServlet("/generate")
public class GenerateDataServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(GenerateDataServlet.class.getName());
    private static final Random RND = new Random();

    // 🏠 Configuration des devices et localisations
    private static final String[] LOCATIONS = {
            "Living Room", "Kitchen", "Master Bedroom", "Guest Bedroom",
            "Bathroom", "Garage", "Garden", "Basement", "Attic", "Office"
    };

    // 📊 Templates de devices
    private static enum DeviceTemplate {
        SMART_METER("SmartMeter", new String[]{"power", "voltage", "current"}),
        THERMOSTAT("Thermostat", new String[]{"temperature", "humidity"}),
        AC_UNIT("AC_Unit", new String[]{"power", "temperature"}),
        LIGHT_SENSOR("LightSensor", new String[]{"luminosity"}),
        MOTION_SENSOR("MotionSensor", new String[]{"motion"}),
        WATER_HEATER("WaterHeater", new String[]{"power", "temperature"}),
        REFRIGERATOR("Refrigerator", new String[]{"power", "temperature"}),
        WASHING_MACHINE("WashingMachine", new String[]{"power", "current"}),
        SOLAR_PANEL("SolarPanel", new String[]{"power", "voltage"}),
        SECURITY_CAMERA("SecurityCamera", new String[]{"motion", "luminosity"}),
        SMOKE_DETECTOR("SmokeDetector", new String[]{"co2", "temperature"}),
        HVAC_SYSTEM("HVAC_System", new String[]{"power", "temperature", "humidity"});

        final String type;
        final String[] readingTypes;

        DeviceTemplate(String type, String[] readingTypes) {
            this.type = type;
            this.readingTypes = readingTypes;
        }
    }

    // 🎯 Seuils d'alerte
    private static class Thresholds {
        static final double POWER_ALERT = 5000.0;
        static final double TEMP_ALERT = 35.0;
        static final double VOLTAGE_ALERT = 250.0;
        static final double CURRENT_ALERT = 40.0;
        static final double HUMIDITY_LOW = 10.0;
        static final double HUMIDITY_HIGH = 90.0;
        static final double CO2_ALERT = 1000.0;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        EntityManager em = null;
        boolean generationFailed = false;
        try {
            // 📊 Récupération des paramètres
            int deviceCount = getIntParam(req, "deviceCount", 10);
            int readingsPerDevice = getIntParam(req, "readingsPerDevice", 500);

            String fixedDeviceType = getStringParam(req, "deviceType");
            String fixedLocation = getStringParam(req, "location");
            String fixedReadingType = getStringParam(req, "readingType");

            double alertProbability = getIntParam(req, "alertProbability", 5) / 100.0;

            // 🔍 Validation
            if (deviceCount < 1 || deviceCount > 100) {
                req.setAttribute("error", "Device count must be between 1 and 100");
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }

            if (readingsPerDevice < 10 || readingsPerDevice > 5000) {
                req.setAttribute("error", "Readings per device must be between 10 and 5000");
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }

            // 📝 Log des paramètres
            logger.info("🏠 Starting Smart Home data generation:");
            logger.info("   📊 Devices: " + deviceCount);
            logger.info("   📈 Readings/Device: " + readingsPerDevice);
            logger.info("   🔧 Device Type: " + (fixedDeviceType != null ? fixedDeviceType : "RANDOM"));
            logger.info("   📍 Location: " + (fixedLocation != null ? fixedLocation : "RANDOM"));
            logger.info("   📡 Reading Type: " + (fixedReadingType != null ? fixedReadingType : "RANDOM"));
            logger.info("   ⚠️  Alert Probability: " + (alertProbability * 100) + "%");

            // 🚀 Génération des données
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();

            int totalReadings = 0;
            int alertReadings = 0;

            for (int deviceIndex = 0; deviceIndex < deviceCount; deviceIndex++) {
                // 🔧 Sélection du template de device
                DeviceTemplate template;
                if (fixedDeviceType != null) {
                    template = findTemplateByType(fixedDeviceType);
                } else {
                    template = DeviceTemplate.values()[RND.nextInt(DeviceTemplate.values().length)];
                }

                //  Sélection de la localisation
                String location = (fixedLocation != null)
                        ? fixedLocation
                        : LOCATIONS[RND.nextInt(LOCATIONS.length)];

                // 🆕 Création du device
                String deviceName = template.type + "_" + String.format("%03d", deviceIndex + 1);
                Device device = new Device(deviceName, template.type, location);
                em.persist(device);

                logger.info("✅ Device created: " + deviceName + " (" + location + ")");

                // Génération des readings
                LocalDateTime baseTime = LocalDateTime.now();

                for (int i = 0; i < readingsPerDevice; i++) {
                    // ⏰ Incrémenter le temps
                    int minutesIncrement = 5 + RND.nextInt(56);


                    // Aller en ARRIÈRE, pour que i=0 soit le plus récent
                    LocalDateTime timestamp = baseTime.minusMinutes((long) i * minutesIncrement);
                    // =================================================================


                    // ... (Sélection du type et génération de la valeur, c'est correct) ...
                    String readingType;
                    if (fixedReadingType != null && Arrays.asList(template.readingTypes).contains(fixedReadingType)) {
                        readingType = fixedReadingType;
                    } else {
                        readingType = template.readingTypes[RND.nextInt(template.readingTypes.length)];
                    }
                    boolean shouldAlert = RND.nextDouble() < alertProbability;
                    double value = generateValue(readingType, shouldAlert, timestamp);
                    if (shouldAlert) {
                        alertReadings++;
                    }

                    Reading reading = new Reading(timestamp, value, readingType, device);
                    em.persist(reading);
                    totalReadings++;

                    // 💾 Flush périodique
                    if (i % 100 == 0) {
                        em.flush();
                        em.clear();
                        // Il faut recharger le device après un clear()
                        device = em.find(Device.class, device.getId());
                        if (device == null) {
                            throw new IllegalStateException("Device not found after flush/clear. ID: " + device.getId());
                        }
                    }
                }
            }

            em.getTransaction().commit();
            long endTime = System.currentTimeMillis();


            //  Message de succès
            String successMessage = String.format(
                    "✅ Generated %,d readings from %d devices in %,d ms | Alerts: %d (%.1f%%)",
                    totalReadings, deviceCount, (endTime - startTime),
                    alertReadings, (alertReadings * 100.0 / totalReadings)
            );

            logger.info("🎉 " + successMessage);
            req.setAttribute("message", successMessage);
            // 🔹 IMPORTANT : Utiliser la Session pour le message de succès
            // req.setAttribute ne survit pas à une redirection
            HttpSession session = req.getSession();
            session.setAttribute("message", successMessage);

        } catch (NumberFormatException e) {
            logger.severe("❌ Invalid number format: " + e.getMessage());
            req.setAttribute("error", "Invalid number format in parameters");
        } catch (Exception e) {
            // 🔹 CORRECTION : Marquer l'échec
            generationFailed = true;
            logger.severe("❌ Error during data generation: " + e.getMessage());
            e.printStackTrace(); // REGARDEZ VOS LOGS SERVEUR (catalina.out) !
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // 🔹 Transmettre l'erreur
            req.setAttribute("error", "Error generating data: " + e.getMessage());

        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        //  Redirection vers la page d'accueil
        if (generationFailed) {
            // Échec : Transférer la requête (pour garder le message d'erreur)
            logger.warning("Generation failed, forwarding to JSP to show error.");
            RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/generator.jsp");
            dispatcher.forward(req, resp);
        } else {
            // Succès : Rediriger (pour éviter la re-soumission du formulaire)
            logger.info("Generation successful, redirecting to generator page.");
            resp.sendRedirect(req.getContextPath() + "/generator");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 🔹 CORRECTION : Nous devons gérer le message de succès de la Session ici
        HttpSession session = req.getSession(false); // Ne pas créer de nouvelle session
        if (session != null) {
            String message = (String) session.getAttribute("message");
            if (message != null) {
                req.setAttribute("message", message);
                // Retirer le message pour qu'il n'apparaisse qu'une fois
                session.removeAttribute("message");
            }
        }

        // Afficher la page du générateur
        req.getRequestDispatcher("/pages/generator.jsp").forward(req, resp);
    }

    /**
     * Génère une valeur réaliste selon le type de mesure
     */
    private double generateValue(String readingType, boolean shouldAlert, LocalDateTime timestamp) {
        int hour = timestamp.getHour();

        switch (readingType) {
            case "power":
                if (shouldAlert) return Thresholds.POWER_ALERT + RND.nextDouble() * 2000;
                double basePower = (hour >= 7 && hour <= 22) ? 800 : 200;
                return basePower + RND.nextDouble() * 2000;

            case "voltage":
                if (shouldAlert) return Thresholds.VOLTAGE_ALERT + RND.nextDouble() * 20;
                return 220 + RND.nextDouble() * 10;

            case "current":
                if (shouldAlert) return Thresholds.CURRENT_ALERT + RND.nextDouble() * 20;
                return 5 + RND.nextDouble() * 20;

            case "temperature":
                if (shouldAlert) return Thresholds.TEMP_ALERT + RND.nextDouble() * 15;
                double baseTemp = (hour >= 6 && hour <= 20) ? 22 : 18;
                return baseTemp + RND.nextDouble() * 6 - 3;

            case "humidity":
                if (shouldAlert) {
                    return RND.nextBoolean()
                            ? Thresholds.HUMIDITY_LOW - RND.nextDouble() * 8
                            : Thresholds.HUMIDITY_HIGH + RND.nextDouble() * 8;
                }
                return 40 + RND.nextDouble() * 30;

            case "co2":
                if (shouldAlert) return Thresholds.CO2_ALERT + RND.nextDouble() * 500;
                return 400 + RND.nextDouble() * 400;

            case "motion":
                double motionProb = (hour >= 7 && hour <= 23) ? 0.3 : 0.05;
                return RND.nextDouble() < motionProb ? 1 : 0;

            case "luminosity":
                if (hour >= 6 && hour <= 8) return 200 + RND.nextDouble() * 300;
                if (hour >= 9 && hour <= 17) return 500 + RND.nextDouble() * 500;
                if (hour >= 18 && hour <= 20) return 200 + RND.nextDouble() * 300;
                return RND.nextDouble() * 50;

            default:
                return RND.nextDouble() * 100;
        }
    }

    /**
     *  Trouve un template de device par type
     */
    private DeviceTemplate findTemplateByType(String type) {
        for (DeviceTemplate t : DeviceTemplate.values()) {
            if (t.type.equalsIgnoreCase(type)) {
                return t;
            }
        }
        logger.warning("⚠️  Device type not found: " + type + ", using SmartMeter");
        return DeviceTemplate.SMART_METER;
    }

    /**
     * Récupère un paramètre entier
     */
    private int getIntParam(HttpServletRequest req, String paramName, int defaultValue) {
        String value = req.getParameter(paramName);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.warning("⚠️  Invalid integer for " + paramName + ": " + value);
            return defaultValue;
        }
    }

    /**
     * 📝 Récupère un paramètre String (null si vide)
     */
    private String getStringParam(HttpServletRequest req, String paramName) {
        String value = req.getParameter(paramName);
        if (value == null || value.trim().isEmpty() || value.equals("")) {
            return null;
        }
        return value.trim();
    }
}