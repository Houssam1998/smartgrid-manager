package org.smartgrid.smartgridmanager.api;

import org.smartgrid.smartgridmanager.model.Device;

// C'est un simple POJO (Plain Old Java Object). Pas d'annotations JPA !
public class DeviceDTO {

    // Les champs que nous voulons exposer
    private Long id;
    private String name;
    private String deviceType;
    private String location;

    // Constructeur vide (obligatoire pour Jackson)
    public DeviceDTO() { }

    // Un constructeur pour convertir facilement l'Entité en DTO
    public DeviceDTO(Device device) {
        this.id = device.getId();
        this.name = device.getName();
        this.deviceType = device.getDeviceType();
        this.location = device.getLocation();
    }

    // --- Getters et Setters pour tous les champs ---

    public Long getId() {
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
}