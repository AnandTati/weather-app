package com.example.weatherapp.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.example.weatherapp.models.Location;
import com.example.weatherapp.models.Weather;
import com.example.weatherapp.services.AddressService;
import com.example.weatherapp.services.WeatherService;


/**
 * WeatherController is a REST controller that handles requests for weather information.
 * It uses AddressService to geocode addresses and WeatherService to retrieve weather data.
 * It provides an endpoint to get weather information based on a given address.
 */
@RestController
@RequestMapping("/weather")
public class WeatherController {

    // AddressService to geocode addresses.
    @Autowired
    private AddressService addressService;

    // WeatherService to retrieve weather data.
    @Autowired
    private WeatherService weatherService;

    /**
     * Retrieves weather information for the given address.
     *
     * @param address The address for which to retrieve weather data.
     * @return ResponseEntity containing the Weather object or an error response.
     */
    @CrossOrigin(origins = "http://localhost:3000") // Adjust as necessary
    @GetMapping()
    public ResponseEntity<Weather> getWeather(@RequestParam(value = "address") String address) {

        // Validate the input address.
        if (address == null || address.trim().isEmpty()) {
            // Return 400 Bad Request if the address is null or empty.
            return ResponseEntity.badRequest().body(null);
        }

        try {
            // Geocode the address using AddressService.
            Location location = addressService.getLocation(address);

            // Check if the address was found.
            if (location == null) {
                // Return 404 Not Found if the address was not found.
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(null);
            }

            // Retrieve weather data for the location using WeatherService.
            Weather weather = weatherService.getWeather(location);
            // Check if weather data was retrieved successfully.
            if (weather == null || weather.getCurrent() == null || weather.getForecast() == null) {
                // Return 503 Service Unavailable if weather data is unavailable.
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                     .body(null);
            }
            // Return 200 OK with the weather data.
            return ResponseEntity.ok(weather);
        } catch (HttpClientErrorException e) {
            // Handle specific HTTP client errors (e.g., 401 Unauthorized, 403 Forbidden, 404 Not Found)
            // that might occur when calling external APIs.
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (RestClientException e) {
            // Handle general REST client errors (e.g., connection issues, API failures)
            // that might occur when calling external APIs.
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                 .body(null);
        } catch (IOException e) {
            // Handle any other unexpected exceptions (e.g., internal server errors).
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null);
        }
    }
}
