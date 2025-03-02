package com.example.weatherapp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;

@Configuration
@Getter
public class AppConfiguration {
    
    /**
     * addressServiceApiKey holds the API key for the address service (e.g., Google Maps Geocoding API).
     * The value is injected from the environment variables in the application.yaml.
     * The property name in the properties file or environment is "address-service.api-key".
     *
     * To set this environment variable, use:
     * `export ADDRESS_SERVICE_API_KEY="YOUR_ADDRESS_SERVICE_API_KEY"`
     */
    @Value("${address-service.api-key}")
    private String addressServiceApiKey;

    /**
     * weatherServiceApiKey holds the API key for the weather service (e.g., WeatherAPI).
     * The value is injected from the environment variables in the application.yaml.
     * The property name in the properties file or environment is "weather-service.api-key".
     *
     * To set this environment variable, use:
     * `export WEATHER_SERVICE_API_KEY="YOUR_WEATHER_SERVICE_API_KEY"`
     */
    @Value("${weather-service.api-key}")
    private String weatherServiceApiKey;

    /**
     * The value is used by the WeatherService cache to expire an entry after the defined number of seconds.
     */
    @Value("${weather-service.cache-expiry-seconds:1800}")
    private int cacheExpiryInSeconds;


    // @Bean
    // public AppConfiguration appConfiguration() {
    //     return new AppConfiguration();
    // }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
