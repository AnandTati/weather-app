package com.example.weatherapp.services;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.weatherapp.configuration.AppConfiguration;
import com.example.weatherapp.models.Location;
import com.example.weatherapp.models.Weather;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * WeatherService is responsible for retrieving weather data from the WeatherAPI.
 * It takes a Location object as input and returns a Weather object containing
 * current and forecasted weather information.
 */
@Service
public class WeatherService {

    // Base URL for the WeatherAPI forecast endpoint.
    private static final String BASE_URL = "https://api.weatherapi.com/v1/forecast.json";

    // AppConfiguration to access API key.
    private final AppConfiguration appConfiguration;

    // RestTemplate to call weather APIs
    private final RestTemplate restTemplate;

    // Cache with zip code as key and Weather object as value.
    private final Cache<String, Weather> weatherCache;
            
    @Autowired
    public WeatherService(AppConfiguration appConfiguration, RestTemplate restTemplate) {
        this.appConfiguration = appConfiguration;
        this.restTemplate = restTemplate;

        // Create a cache to store weather data.
        weatherCache = CacheBuilder.newBuilder()
            .expireAfterWrite(appConfiguration.getCacheExpiryInSeconds(), TimeUnit.SECONDS)  // Auto-evicts after 30 mins
            .maximumSize(1000)  // Limit to prevent memory overuse (adjust as needed)
            .build();
    }

    /**
     * Invalidates all cache entries.
     */
    public void clearCache() {
        weatherCache.invalidateAll();
    }

    /**
     * Retrieves weather data for the given location.
     *
     * @param location The Location object for which to retrieve weather data.
     * @return Weather object containing current and forecasted weather information.
     * @throws Exception If an error occurs during API request or JSON parsing.
     */
    public Weather getWeather(Location location) throws IOException {
        // Check if cache has the weather data for the provided zip code.
        var weatheObejct = weatherCache.getIfPresent(location.getZipCode());
        if (weatheObejct != null) {
            weatheObejct.setFromCache(true);
            return weatheObejct;
        }

        // Constructs the URL for the WeatherAPI request, including the API key and location coordinates.
        String url = BASE_URL +
            "?key=" + appConfiguration.getWeatherServiceApiKey() +
            "&q=" + location.getLatitude() + "," + location.getLongitude();

        String response;
        try {
            // Sends a GET request to the WeatherAPI and retrieves the response as a JSON string.
            response = restTemplate.getForObject(url, String.class);

            // If the response is null or empty, throw an exception.
            if (response == null || response.isEmpty()) {
                throw new IOException("Empty response from Weather API.");
            }
        } catch (RestClientException e) {
            // Handle network-related or API connection issues.
            throw new IOException("Error while calling Weather API: " + e.getMessage(), e);
        }

        // Creates an ObjectMapper to parse the JSON response.
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;

        try {
            // Parses the JSON response into a JsonNode object.
            root = mapper.readTree(response);

            // Handle errors based on the API's error response format
            if (root.has("code") && root.has("message")) {
                String errorCode = root.get("code").asText();
                String errorMessage = root.get("message").asText();

                // Error handling based on the error code
                switch (errorCode) {
                    case "1002" -> throw new IOException("API key not provided: " + errorMessage);
                    case "1003" -> throw new IOException("Parameter 'q' not provided: " + errorMessage);
                    case "1005" -> throw new IOException("API request URL is invalid: " + errorMessage);
                    case "1006" -> throw new IOException("No location found matching parameter 'q': " + errorMessage);
                    case "9000" -> throw new IOException("Invalid JSON body in bulk request: " + errorMessage);
                    case "9001" -> throw new IOException("Too many locations in bulk request: " + errorMessage);
                    case "9999" -> throw new IOException("Internal application error: " + errorMessage);
                    case "2006" -> throw new IOException("API key is invalid: " + errorMessage);
                    case "2007" -> throw new IOException("API key has exceeded calls per month quota: " + errorMessage);
                    case "2008" -> throw new IOException("API key has been disabled: " + errorMessage);
                    case "2009" -> throw new IOException("API key does not have access to the resource: " + errorMessage);
                    default -> throw new IOException("API error: " + errorMessage);
                }
            }

            // Continue with the normal processing of the response...
        } catch (JsonProcessingException e) {
            // Handle JSON parsing errors
            throw new IOException("Error parsing response JSON: " + e.getMessage(), e);
        }

        // Extracts the current weather node from the JSON response.
        JsonNode currentNode = root.path("current");
        // Extracts the current weather condition node from the JSON response.
        JsonNode conditionNode1 = currentNode.path("condition");

        // Creates a new Current object to store current weather conditions.
        Weather.Current current = new Weather.Current(
            // Extracts and sets the last updated epoch time.
            currentNode.path("last_updated_epoch").asInt(),
            // Extracts and sets the temperature in Celsius.
            currentNode.path("temp_c").asDouble(),
            // Extracts and sets the temperature in Fahrenheit.
            currentNode.path("temp_f").asDouble(),

            // Creates a new Condition object to store current weather condition details.
            new Weather.Condition(
                // Extracts and sets the condition text.
                conditionNode1.path("text").asText(),
                // Extracts and sets the condition icon URL.
                conditionNode1.path("icon").asText(),
                // Extracts and sets the condition code.
                conditionNode1.path("code").asInt()
            )
        );

        // Extract Min/Max Temperature from Forecast
        // Extracts the forecast day node from the JSON response.
        JsonNode forecastNode = root.path("forecast").path("forecastday").get(0).path("day");

        // Extracts the forecast weather condition node from the JSON response.
        JsonNode forecastConditionNode = forecastNode.path("condition");

        // Creates a new Forecast object to store forecasted weather conditions.
        Weather.Forecast forecast = new Weather.Forecast(
            // Extracts and sets the minimum temperature in Celsius.
            forecastNode.path("mintemp_c").asDouble(),
            // Extracts and sets the minimum temperature in Fahrenheit.
            forecastNode.path("mintemp_f").asDouble(),
            // Extracts and sets the maximum temperature in Celsius.
            forecastNode.path("maxtemp_c").asDouble(),
            // Extracts and sets the maximum temperature in Fahrenheit.
            forecastNode.path("maxtemp_f").asDouble(),

            // Creates a new Condition object to store forecasted weather condition details.
            new Weather.Condition(
                // Extracts and sets the forecast condition text.
                forecastConditionNode.path("text").asText(),
                // Extracts and sets the forecast condition icon URL.
                forecastConditionNode.path("icon").asText(),
                // Extracts and sets the forecast condition code.
                forecastConditionNode.path("code").asInt()
            )
        );

        // Creates a new Weather object to store the weather data.
        Weather weather = new Weather(
            // Sets the location of the weather data.
            location,

            // Sets the current object in the weather object.
            current,

            // Sets the forecast object in the weather object.
            forecast,

            // Set the from Cache as false.
            false
        );

        // Insert the weather object in cache.
        weatherCache.put(location.getZipCode(), weather);

        // Returns the weather object.
        return weather;
    }
}