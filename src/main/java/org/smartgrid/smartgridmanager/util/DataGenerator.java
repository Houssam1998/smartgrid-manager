//package org.smartgrid.smartgridmanager.util;
//
//import org.smartgrid.smartgridmanager.dao.JpaUtil;
//import org.smartgrid.smartgridmanager.model.Device;
//import org.smartgrid.smartgridmanager.model.Reading;
//import jakarta.persistence.EntityManager;
//import java.time.LocalDateTime;
//import java.util.Random;
//
//public class DataGenerator {
//
//    // Liste de devices et leurs types de readings possibles
//    private static final String[][] DEVICES = {
//            {"SmartMeter", "power"},
//            {"Thermometer", "temperature"},
//            {"Camera", "motion"},
//            {"LightSensor", "luminosity"}
//    };
//
//    public static void main(String[] args) {
//        EntityManager em = JpaUtil.getEntityManager();
//        em.getTransaction().begin();
//        long t0 = System.currentTimeMillis();
//
//        Random rnd = new Random();
//        int readingsPerDevice = 2500; // total 4 devices * 2500 = 10000 readings
//
//        for (int dIndex = 0; dIndex < DEVICES.length; dIndex++) {
//            String deviceType = DEVICES[dIndex][0];
//            String readingType = DEVICES[dIndex][1];
//            Device d = new Device(deviceType + String.format("%04d", dIndex + 1), deviceType, "Building " + (char)('A' + dIndex));
//            em.persist(d);
//
//            for (int i = 0; i < readingsPerDevice; i++) {
//                double value;
//                switch (readingType) {
//                    case "power": value = 0.1 + rnd.nextDouble() * 10.0; break;
//                    case "temperature": value = 15 + rnd.nextDouble() * 15.0; break; // 15°C à 30°C
//                    case "motion": value = rnd.nextInt(2); break; // 0 ou 1
//                    case "luminosity": value = rnd.nextDouble() * 1000; break; // 0 à 1000 lux
//                    default: value = rnd.nextDouble() * 10; break;
//                }
//
//                Reading r = new Reading(
//                        LocalDateTime.now().minusMinutes(rnd.nextInt(10000)),
//                        value,
//                        readingType,
//                        d
//                );
//                em.persist(r);
//
//                if (i % 50 == 0) {
//                    em.flush();
//                    em.clear();
//                    // ré-attacher le device
//                    d = em.find(Device.class, d.getId());
//                }
//            }
//        }
//
//        em.getTransaction().commit();
//        em.close();
//        long t1 = System.currentTimeMillis();
//        System.out.println("✅ " + (DEVICES.length * readingsPerDevice) + " readings inserted, ms = " + (t1 - t0));
//        JpaUtil.close();
//    }
//}
package org.smartgrid.smartgridmanager.util;

import org.smartgrid.smartgridmanager.dao.JpaUtil;
import org.smartgrid.smartgridmanager.model.Device;
import org.smartgrid.smartgridmanager.model.Reading;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;

public class DataGenerator {

    // 🏠 Définition des zones de la grande maison
    private static final String[] LOCATIONS = {
            "Living Room", "Kitchen", "Master Bedroom", "Guest Bedroom",
            "Bathroom", "Garage", "Garden", "Basement", "Attic", "Office"
    };

    // 📊 Types de devices avec leurs mesures possibles
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
        static final double POWER_ALERT = 5000.0;      // > 5000W
        static final double TEMP_ALERT = 35.0;         // > 35°C
        static final double VOLTAGE_ALERT = 250.0;     // > 250V
        static final double CURRENT_ALERT = 40.0;      // > 40A
        static final double HUMIDITY_LOW = 10.0;       // < 10%
        static final double HUMIDITY_HIGH = 90.0;      // > 90%
        static final double CO2_ALERT = 1000.0;        // > 1000ppm
    }

    private static final Random RND = new Random();

    /**
     * 🎲 Génération de données aléatoires complètes
     */
    public static void generateRandomData(int devicesCount, int readingsPerDevice) {
        generateData(devicesCount, readingsPerDevice, null, null, null, null);
    }

    /**
     * 🎯 Génération de données avec paramètres fixés
     *
     * @param devicesCount Nombre de devices à créer
     * @param readingsPerDevice Nombre de lectures par device
     * @param fixedDeviceType Type de device fixé (null = aléatoire)
     * @param fixedLocation Localisation fixée (null = aléatoire)
     * @param fixedReadingType Type de lecture fixé (null = aléatoire)
     * @param alertProbability Probabilité de générer une alerte (0.0 à 1.0, null = 0.05)
     */
    public static void generateData(
            int devicesCount,
            int readingsPerDevice,
            String fixedDeviceType,
            String fixedLocation,
            String fixedReadingType,
            Double alertProbability
    ) {
        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        long t0 = System.currentTimeMillis();

        double alertProb = (alertProbability != null) ? alertProbability : 0.05;
        int totalReadings = 0;
        int alertReadings = 0;

        System.out.println("🏠 Génération de données pour la Smart Home...");
        System.out.println("📊 Paramètres:");
        System.out.println("   - Devices: " + devicesCount);
        System.out.println("   - Readings/Device: " + readingsPerDevice);
        System.out.println("   - Device Type: " + (fixedDeviceType != null ? fixedDeviceType : "RANDOM"));
        System.out.println("   - Location: " + (fixedLocation != null ? fixedLocation : "RANDOM"));
        System.out.println("   - Reading Type: " + (fixedReadingType != null ? fixedReadingType : "RANDOM"));
        System.out.println("   - Alert Probability: " + (alertProb * 100) + "%\n");

        for (int deviceIndex = 0; deviceIndex < devicesCount; deviceIndex++) {
            // Sélection du template de device
            DeviceTemplate template;
            if (fixedDeviceType != null) {
                template = findTemplateByType(fixedDeviceType);
            } else {
                template = DeviceTemplate.values()[RND.nextInt(DeviceTemplate.values().length)];
            }

            // Création du device
            String location = (fixedLocation != null) ? fixedLocation : LOCATIONS[RND.nextInt(LOCATIONS.length)];
            String deviceName = template.type + "_" + String.format("%03d", deviceIndex + 1);
            Device device = new Device(deviceName, template.type, location);
            em.persist(device);

            System.out.println("✅ Device créé: " + deviceName + " (" + location + ")");

            // Génération des readings
            LocalDateTime baseTime = LocalDateTime.now().minusDays(30); // Démarrer il y a 30 jours

            for (int i = 0; i < readingsPerDevice; i++) {
                // Incrémenter le temps (toutes les 5 à 60 minutes)
                int minutesIncrement = 5 + RND.nextInt(56);
                LocalDateTime timestamp = baseTime.plusMinutes((long) i * minutesIncrement);

                // Sélection du type de reading
                String readingType;
                if (fixedReadingType != null && Arrays.asList(template.readingTypes).contains(fixedReadingType)) {
                    readingType = fixedReadingType;
                } else {
                    readingType = template.readingTypes[RND.nextInt(template.readingTypes.length)];
                }

                // Génération de la valeur (normale ou alerte)
                boolean shouldAlert = RND.nextDouble() < alertProb;
                double value = generateValue(readingType, shouldAlert, timestamp);

                if (shouldAlert) {
                    alertReadings++;
                }

                Reading reading = new Reading(timestamp, value, readingType, device);
                em.persist(reading);
                totalReadings++;

                // Flush périodique pour optimiser
                if (i % 100 == 0) {
                    em.flush();
                    em.clear();
                    device = em.find(Device.class, device.getId());
                }
            }
        }

        em.getTransaction().commit();
        em.close();
        long t1 = System.currentTimeMillis();

        System.out.println("\n🎉 Génération terminée!");
        System.out.println("   ⏱️  Temps: " + (t1 - t0) + " ms");
        System.out.println("   📊 Total Readings: " + totalReadings);
        System.out.println("   ⚠️  Alertes générées: " + alertReadings + " (" + String.format("%.2f", (alertReadings * 100.0 / totalReadings)) + "%)");

        JpaUtil.close();
    }

    /**
     * 🎲 Génère une valeur réaliste selon le type de mesure
     */
    private static double generateValue(String readingType, boolean shouldAlert, LocalDateTime timestamp) {
        int hour = timestamp.getHour();

        switch (readingType) {
            case "power":
                if (shouldAlert) return Thresholds.POWER_ALERT + RND.nextDouble() * 2000; // 5000-7000W
                // Variation jour/nuit (plus de conso le jour)
                double basePower = (hour >= 7 && hour <= 22) ? 800 : 200;
                return basePower + RND.nextDouble() * 2000; // 200-2800W jour, 200-2200W nuit

            case "voltage":
                if (shouldAlert) return Thresholds.VOLTAGE_ALERT + RND.nextDouble() * 20; // 250-270V
                return 220 + RND.nextDouble() * 10; // 220-230V (Europe)

            case "current":
                if (shouldAlert) return Thresholds.CURRENT_ALERT + RND.nextDouble() * 20; // 40-60A
                return 5 + RND.nextDouble() * 20; // 5-25A

            case "temperature":
                if (shouldAlert) return Thresholds.TEMP_ALERT + RND.nextDouble() * 15; // 35-50°C
                // Variation jour/nuit
                double baseTemp = (hour >= 6 && hour <= 20) ? 22 : 18;
                return baseTemp + RND.nextDouble() * 6 - 3; // ±3°C

            case "humidity":
                if (shouldAlert) {
                    return RND.nextBoolean()
                            ? Thresholds.HUMIDITY_LOW - RND.nextDouble() * 8  // 2-10%
                            : Thresholds.HUMIDITY_HIGH + RND.nextDouble() * 8; // 90-98%
                }
                return 40 + RND.nextDouble() * 30; // 40-70%

            case "co2":
                if (shouldAlert) return Thresholds.CO2_ALERT + RND.nextDouble() * 500; // 1000-1500ppm
                return 400 + RND.nextDouble() * 400; // 400-800ppm

            case "motion":
                // Plus de mouvement le jour
                double motionProb = (hour >= 7 && hour <= 23) ? 0.3 : 0.05;
                return RND.nextDouble() < motionProb ? 1 : 0;

            case "luminosity":
                // Lumière naturelle selon l'heure
                if (hour >= 6 && hour <= 8) return 200 + RND.nextDouble() * 300;   // Aube
                if (hour >= 9 && hour <= 17) return 500 + RND.nextDouble() * 500;  // Jour
                if (hour >= 18 && hour <= 20) return 200 + RND.nextDouble() * 300; // Crépuscule
                return RND.nextDouble() * 50; // Nuit

            default:
                return RND.nextDouble() * 100;
        }
    }

    /**
     * 🔍 Trouve un template de device par type
     */
    private static DeviceTemplate findTemplateByType(String type) {
        for (DeviceTemplate t : DeviceTemplate.values()) {
            if (t.type.equalsIgnoreCase(type)) {
                return t;
            }
        }
        return DeviceTemplate.SMART_METER; // Par défaut
    }

    /**
     * 🚀 Point d'entrée principal
     */
    public static void main(String[] args) {
        // Exemple 1: Génération complètement aléatoire
        // generateRandomData(20, 500); // 20 devices, 500 readings chacun

        // Exemple 2: Tous les SmartMeters dans la Kitchen
        // generateData(5, 1000, "SmartMeter", "Kitchen", null, 0.08);

        // Exemple 3: Thermostats partout, uniquement température, peu d'alertes
        // generateData(10, 800, "Thermostat", null, "temperature", 0.03);

        // Exemple 4: Génération massive pour tests de performance
        generateRandomData(30, 1000); // 30 devices × 1000 readings = 30,000 readings

        // Exemple 5: Simulation de problème électrique (beaucoup d'alertes power)
        // generateData(8, 500, "SmartMeter", null, "power", 0.15);
    }
}