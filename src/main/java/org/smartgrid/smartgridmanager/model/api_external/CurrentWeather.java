package org.smartgrid.smartgridmanager.model.api_external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// "Ignore tous les champs JSON que nous ne déclarons pas ici"
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentWeather {

    private double temperature;
    private int weathercode;

    // Getters et Setters
    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    public int getWeathercode() {
        return weathercode;
    }
    public void setWeathercode(int weathercode) {
        this.weathercode = weathercode;
    }
}