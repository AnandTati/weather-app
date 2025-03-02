package com.example.weatherapp.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.weatherapp.configuration.AppConfiguration;
import com.example.weatherapp.models.Location;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AddressService is responsible for geocoding addresses using the Google Maps Geocoding API.
 * It takes an address string as input and returns a Location object containing
 * formatted address, latitude, longitude, and zip code.
 */
@Service
public class AddressService {

    // Base URL for the Google Maps Geocoding API.
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    // Autowired AppConfiguration to access API key.
    @Autowired
    private AppConfiguration appConfiguration;

    // RestTemplate to call weather APIs
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Retrieves the Location object for the given address.
     *
     * @param address The address string to geocode.
     * @return Location object containing geocoded information, or null if geocoding fails.
     */
    public Location getLocation(String address) throws IOException {
        // Constructs the URL for the Geocoding API request, including the API key and address.
        String url = BASE_URL +
            "?key=" + appConfiguration.getAddressServiceApiKey() +
            "&address=" + address.replace(" ", "+"); // Replace spaces with '+' for URL encoding.

        // Sends a GET request to the Geocoding API and retrieves the response as a JSON string.
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(response);

        // Create ObjectMapper instance to parse JSON
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the response into a JsonNode
        JsonNode rootNode = objectMapper.readTree(response);

        // Checks the status of the Geocoding API response.
        if (!rootNode.path("status").asText().equals("OK")) {
            return null;  // If the status is not "OK", geocoding failed, return null.
        }

        // Extracts the "results" array from the JSON response.
        JsonNode resultsArray = rootNode.path("results");
        if (resultsArray.isEmpty()) {
            return null;  // If the array is empty, no results were found, return null.
        }

        // Gets the first result object from the results array.
        JsonNode firstObject = resultsArray.get(0);

        // Extracts the latitude and longitude from the "location" object within the "geometry" object.
        JsonNode locationNode = firstObject.path("geometry").path("location");
        double latitude = locationNode.path("lat").asDouble();
        double longitude = locationNode.path("lng").asDouble();

        // Extracts the address components array from the first result object.
        JsonNode addressComponents = firstObject.path("address_components");

        // Initializes zipCode to null.
        String zipCode = null;
        // Iterates through the address components to find the postal code.
        for (JsonNode component : addressComponents) {
            JsonNode types = component.path("types");
            for (JsonNode type : types) {
                if (type.asText().equals("postal_code")) {
                    zipCode = component.path("long_name").asText();
                    break;
                }
            }
            if (zipCode != null) {
                break;  // Exit the loop if postal_code is found.
            }
        }
        if (zipCode == null) {
            return null;
        }

        // Creates a new Location object and sets its properties.
        Location location = new Location();
        location.setFormattedAddress(firstObject.path("formatted_address").asText());
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setZipCode(zipCode);

        // Returns the Location object.
        return location;
    }
}
