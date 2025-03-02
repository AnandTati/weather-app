package com.example.weatherapp;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.example.weatherapp.controllers.WeatherController;
import com.example.weatherapp.models.Location;
import com.example.weatherapp.models.Weather;
import com.example.weatherapp.services.AddressService;
import com.example.weatherapp.services.WeatherService;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;

    @MockitoBean
    private AddressService addressService;

    @InjectMocks
    @SuppressWarnings("unused")
    private WeatherController weatherController;

    @Test
    void getWeather_validAddress() throws Exception {
        Location location = new Location("1600 Amphitheatre Parkway", "94043", 37.422, -122.084);
        Weather weather = new Weather(
            location,
            new Weather.Current(1609459200, 11.2, 52.2, new Weather.Condition("Sunny", "//icon.url", 1003)),
            new Weather.Forecast(9.2, 48.6, 12.5, 54.5, new Weather.Condition("Partly cloudy", "//icon2.url", 1003)),
            false
        );

        when(addressService.getLocation("1600 Amphitheatre Parkway")).thenReturn(location);
        when(weatherService.getWeather(location)).thenReturn(weather);

        // Perform the request and capture the result
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/weather")
                .param("address", "1600 Amphitheatre Parkway")
                .contentType(MediaType.APPLICATION_JSON));

        // Check the HTTP status
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        // Check the current temperature
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.current.temp_c").value(11.2));
        // Check the max forecast temperature
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.forecast.maxtemp_c").value(12.5));
    }

    @Test
    void getWeather_invalidAddress() throws Exception {
        when(addressService.getLocation("Invalid Address")).thenReturn(null);

        mockMvc.perform(get("/weather")
                .param("address", "Invalid Address")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getWeather_emptyAddress() throws Exception {
        mockMvc.perform(get("/weather")
                .param("address", "")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getWeather_missingAddress() throws Exception {
        mockMvc.perform(get("/weather")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getWeather_weatherServiceUnavailable() throws Exception {
        Location location = new Location("1600 Amphitheatre Parkway", "94043", 37.422, -122.084);

        when(addressService.getLocation("1600 Amphitheatre Parkway")).thenReturn(location);
        when(weatherService.getWeather(location)).thenReturn(null);

        mockMvc.perform(get("/weather")
                .param("address", "1600 Amphitheatre Parkway")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void getWeather_internalServerError() throws Exception {
        when(addressService.getLocation("1600 Amphitheatre Parkway")).thenThrow(new IOException());

        mockMvc.perform(get("/weather")
                .param("address", "1600 Amphitheatre Parkway")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getWeather_clientError() throws Exception {
        when(addressService.getLocation("1600 Amphitheatre Parkway")).thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));

        mockMvc.perform(get("/weather")
                .param("address", "1600 Amphitheatre Parkway")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void getWeather_serviceUnavailable() throws Exception {
        when(addressService.getLocation("1600 Amphitheatre Parkway")).thenReturn(new Location());
        when(weatherService.getWeather(any(Location.class))).thenThrow(new RestClientException("Service unavailable"));

        mockMvc.perform(get("/weather")
                .param("address", "1600 Amphitheatre Parkway")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable());
    }
}
