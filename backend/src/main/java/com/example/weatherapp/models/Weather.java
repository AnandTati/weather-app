package com.example.weatherapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {

    // Location information associated with the weather data.
    public Location location;

    // Current weather conditions.
    public Current current;

    // Forecasted weather conditions.
    public Forecast forecast;

    // Flag for cache.
    @JsonProperty("from_cache")
    public boolean fromCache = false;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Current {
        // Epoch time of the last weather update.
        @JsonProperty("last_updated_epoch")
        private int lastUpdated;

        // Temperature in Celsius.
        @JsonProperty("temp_c")
        private double temperatureC;

        // Temperature in Fahrenheit.
        @JsonProperty("temp_f")
        private double temperatureF;

        // Current weather condition details.
        private Condition condition;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Forecast {
        // Minimum temperature in Celsius for the forecast period.
        @JsonProperty("mintemp_c")
        private double minTempC;

        // Minimum temperature in Fahrenheit for the forecast period.
        @JsonProperty("mintemp_f")
        private double minTempF;

        // Maximum temperature in Celsius for the forecast period.
        @JsonProperty("maxtemp_c")
        private double maxTempC;

        // Maximum temperature in Fahrenheit for the forecast period.
        @JsonProperty("maxtemp_f")
        private double maxTempF;

        // Forecasted weather condition details.
        private Condition condition;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Condition {
        // Text description of the weather condition (e.g., "Sunny", "Cloudy").
        public String text;

        // URL or path to the weather condition icon.
        public String icon;

        // Weather condition code.
        public int code;
    }

}
