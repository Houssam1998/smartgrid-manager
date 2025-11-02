package org.smartgrid.smartgridmanager.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String deviceType; // e.g., "SmartMeter", "SolarInverter"
    private String location;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reading> readings;

    public Device() {}
    public Device(String name, String deviceType, String location) {
        this.name = name;
        this.deviceType = deviceType;
        this.location = location;
    }


    // getters & setters

    public Long getId() {   // <-- celui-ci manquant cause ton erreur
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Reading> getReadings() {
        return readings;
    }

    public void setReadings(List<Reading> readings) {
        this.readings = readings;
    }
}
