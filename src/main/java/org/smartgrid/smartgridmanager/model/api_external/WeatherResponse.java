package org.smartgrid.smartgridmanager.model.api_external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    @JsonProperty("current_weather")
    private CurrentWeather currentWeather;

    // 🔹 AJOUTEZ CE CHAMP 🔹
    private DailyData daily;

    // Getters et Setters
    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }
    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    // 🔹 AJOUTEZ CES GETTERS/SETTERS 🔹
    public DailyData getDaily() {
        return daily;
    }
    public void setDaily(DailyData daily) {
        this.daily = daily;
    }
}