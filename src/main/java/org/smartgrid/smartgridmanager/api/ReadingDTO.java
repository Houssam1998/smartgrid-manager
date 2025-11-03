package org.smartgrid.smartgridmanager.api;

import org.smartgrid.smartgridmanager.model.Reading;
import java.time.LocalDateTime;

// Simple POJO pour le transfert de données. Pas d'annotations JPA.
public class ReadingDTO {

    private Long id;
    private LocalDateTime timestamp;
    private double value;
    private String readingType;

    // 🔹 Important : Nous "aplatissons" la relation.
    // Au lieu de l'objet Device entier, nous ne prenons que ce dont nous avons besoin.
    private Long deviceId;
    private String deviceName;

    // Constructeur vide (obligatoire pour le convertisseur JSON)
    public ReadingDTO() { }

    // Constructeur de conversion (pour transformer l'Entité en DTO)
    public ReadingDTO(Reading reading) {
        this.id = reading.getId();
        this.timestamp = reading.getTimestamp();
        this.value = reading.getValue();
        this.readingType = reading.getReadingType();

        // C'est ici qu'on évite la boucle infinie et le LazyLoading
        if (reading.getDevice() != null) {
            this.deviceId = reading.getDevice().getId();
            this.deviceName = reading.getDevice().getName();
        }
    }

    // --- Ajoutez les Getters et Setters pour TOUS les champs ---
    // (id, timestamp, value, readingType, deviceId, deviceName)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public String getReadingType() { return readingType; }
    public void setReadingType(String readingType) { this.readingType = readingType; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
}