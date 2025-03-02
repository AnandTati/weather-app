package com.example.weatherapp;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import com.example.weatherapp.configuration.AppConfiguration;
import com.example.weatherapp.models.Location;
import com.example.weatherapp.models.Weather;
import com.example.weatherapp.services.WeatherService;

@SpringBootTest
class WeatherServiceTests {

    @MockitoBean
    private RestTemplate restTemplate;

    @MockitoBean
    private AppConfiguration appConfiguration;  // Mock AppConfiguration or any other dependent services

    @Autowired
    private WeatherService weatherService;

    @BeforeEach
    public void setup() {
        weatherService.clearCache();
    }

    @Test
    void getWeather_success() throws IOException {
        // Sample Location
        Location location = new Location("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA",
        "94043", 37.4220041, -122.0833494);
        
        // Sample response from weather API
        String apiKey = "testApiKey";
        String responseJson = """
                              {
                                  "current": {
                                      "temp_c": 11.2,
                                      "temp_f": 52.2,
                                      "condition": {
                                          "text": "Sunny",
                                          "icon": "//cdn.weatherapi.com/weather/64x64/night/116.png",
                                          "code": 1003
                                      }
                                  },
                                  "forecast": {
                                      "forecastday": [
                                          {
                                              "day": {
                                                  "maxtemp_c": 12.5,
                                                  "maxtemp_f": 54.5,
                                                  "mintemp_c": 9.2,
                                                  "mintemp_f": 48.6,
                                                  "condition": {
                                                      "text": "Partly cloudy",
                                                      "icon": "//cdn.weatherapi.com/weather/64x64/night/116.png",
                                                      "code": 1003
                                                  }
                                              }
                                          }
                                      ]
                                  }
                              }""";

        // Mocking the configuration and RestTemplate to return a valid response
        Mockito.when(appConfiguration.getWeatherServiceApiKey()).thenReturn(apiKey);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class)))
                .thenReturn(responseJson);

        // Call the method under test
        Weather weather = weatherService.getWeather(location);

        // Assertions
        assertNotNull(weather);
        assertEquals(11.2, weather.getCurrent().getTemperatureC());
        assertEquals("Sunny", weather.getCurrent().getCondition().getText());
        assertEquals(9.2, weather.getForecast().getMinTempC());
        assertEquals("Partly cloudy", weather.getForecast().getCondition().getText());
        assertEquals("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", weather.getLocation().getFormattedAddress());
    }

    @Test
    void getWeather_missingApiKey() {
        Location location = new Location("1600 Amphitheatre Parkway", "94043", 37.422, -122.084);
        String errorResponse = """
                               {
                                 "code": "1002",
                                 "message": "API key not provided"
                               }""";

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class)))
               .thenReturn(errorResponse);

        IOException exception = assertThrows(IOException.class, () -> {
            weatherService.getWeather(location);
        });

        assertTrue(exception.getMessage().contains("API key not provided"));
    }

    @Test
    void getWeather_invalidApiKey() {
        Location location = new Location("1600 Amphitheatre Parkway", "94043", 37.422, -122.084);
        String errorResponse = """
                               {
                                 "code": "2006",
                                 "message": "API key is invalid"
                               }""";

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class)))
               .thenReturn(errorResponse);

        IOException exception = assertThrows(IOException.class, () -> {
            weatherService.getWeather(location);
        });

        assertTrue(exception.getMessage().contains("API key is invalid"));
    }

    @Test
    void getWeather_invalidRequestUrl() {
        Location location = new Location("1600 Amphitheatre Parkway", "94043", 37.422, -122.084);
        String errorResponse = """
                               {
                                 "code": "1005",
                                 "message": "API request URL is invalid"
                               }""";

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class)))
               .thenReturn(errorResponse);

        IOException exception = assertThrows(IOException.class, () -> {
            weatherService.getWeather(location);
        });

        assertTrue(exception.getMessage().contains("API request URL is invalid"));
    }

    @Test
    void getWeather_invalidLocation() {
        Location location = new Location("Invalid Location", "00000", 0.0, 0.0);
        String errorResponse = """
                               {
                                 "code": "1006",
                                 "message": "No location found matching parameter 'q'"
                               }""";

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class)))
               .thenReturn(errorResponse);

        IOException exception = assertThrows(IOException.class, () -> {
            weatherService.getWeather(location);
        });

        assertTrue(exception.getMessage().contains("No location found matching parameter 'q'"));
    }

    @Test
    void getWeather_apiKeyQuotaExceeded() {
        Location location = new Location("1600 Amphitheatre Parkway", "94043", 37.422, -122.084);
        String errorResponse = """
                               {
                                 "code": "2007",
                                 "message": "API key has exceeded calls per month quota"
                               }""";

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class)))
               .thenReturn(errorResponse);

        IOException exception = assertThrows(IOException.class, () -> {
            weatherService.getWeather(location);
        });

        assertTrue(exception.getMessage().contains("API key has exceeded calls per month quota"));
    }

    @Test
    void getWeather_internalServerError() {
        Location location = new Location("1600 Amphitheatre Parkway", "94043", 37.422, -122.084);
        String errorResponse = """
                               {
                                 "code": "9999",
                                 "message": "Internal application error"
                               }""";

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class)))
               .thenReturn(errorResponse);

        IOException exception = assertThrows(IOException.class, () -> {
            weatherService.getWeather(location);
        });

        assertTrue(exception.getMessage().contains("Internal application error"));
    }

    @Test
    void getWeather_apiKeyNotProvided() {
        Location location = new Location("1600 Amphitheatre Parkway", "94043", 37.422, -122.084);
        String errorResponse = """
                               {
                                 "code": "1002",
                                 "message": "API key not provided"
                               }""";

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class)))
               .thenReturn(errorResponse);

        IOException exception = assertThrows(IOException.class, () -> {
            weatherService.getWeather(location);
        });

        assertTrue(exception.getMessage().contains("API key not provided"));
    }

    @Test
    void getWeather_missingQParameter() {
        Location location = new Location("1600 Amphitheatre Parkway", "94043", 37.422, -122.084);
        String errorResponse = """
                               {
                                 "code": "1003",
                                 "message": "Parameter 'q' not provided"
                               }""";

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class)))
               .thenReturn(errorResponse);

        IOException exception = assertThrows(IOException.class, () -> {
            weatherService.getWeather(location);
        });

        assertTrue(exception.getMessage().contains("Parameter 'q' not provided"));
    }

    @Test
    void getWeather_bulkRequestInvalidJson() {
        Location location = new Location("1600 Amphitheatre Parkway", "94043", 37.422, -122.084);
        String errorResponse = """
                               {
                                 "code": "9000",
                                 "message": "Json body passed in bulk request is invalid. Please make sure it is valid json with utf-8 encoding."
                               }""";

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class)))
               .thenReturn(errorResponse);

        IOException exception = assertThrows(IOException.class, () -> {
            weatherService.getWeather(location);
        });

        assertTrue(exception.getMessage().contains("Json body passed in bulk request is invalid"));
    }
}
