package org.smartgrid.smartgridmanager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private double value; // e.g., kW or kWh
    private String readingType; // e.g., "power", "voltage", "current"

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    public Reading() {}
    public Reading(LocalDateTime timestamp, double value, String readingType, Device device) {
        this.timestamp = timestamp;
        this.value = value;
        this.readingType = readingType;
        this.device = device;
    }
    // getters & setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getReadingType() {
        return readingType;
    }

    public void setReadingType(String readingType) {
        this.readingType = readingType;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
