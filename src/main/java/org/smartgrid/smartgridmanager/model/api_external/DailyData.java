package org.smartgrid.smartgridmanager.model.api_external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyData {

    // L'API renvoie des tableaux de 7 jours
    private String[] time; // Les dates

    @JsonProperty("temperature_2m_max")
    private double[] temperatureMax;

    @JsonProperty("temperature_2m_min")
    private double[] temperatureMin;

    // Getters et Setters
    public String[] getTime() {
        return time;
    }
    public void setTime(String[] time) {
        this.time = time;
    }
    public double[] getTemperatureMax() {
        return temperatureMax;
    }
    public void setTemperatureMax(double[] temperatureMax) {
        this.temperatureMax = temperatureMax;
    }
    public double[] getTemperatureMin() {
        return temperatureMin;
    }
    public void setTemperatureMin(double[] temperatureMin) {
        this.temperatureMin = temperatureMin;
    }
}