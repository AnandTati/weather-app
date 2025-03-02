package com.example.weatherapp;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import com.example.weatherapp.configuration.AppConfiguration;
import com.example.weatherapp.models.Location;
import com.example.weatherapp.services.AddressService;

@SpringBootTest
class AddressServiceTests {

    @MockitoBean
    private RestTemplate restTemplate;

    @MockitoBean
    private AppConfiguration appConfiguration;  // Mock AppConfiguration or any other dependent services

    @Autowired
    private AddressService addressService;

    @Test
    void getLocation_success() throws IOException {
        String address = "1600 Amphitheatre Parkway, Mountain View, CA";
        String responseJson = """
            {
                "status":"OK",
                "results":[
                    {
                        "formatted_address":"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA",
                        "geometry":{
                            "location": {
                                "lat":37.4220041,
                                "lng":-122.0833494
                            }
                        },
                        "address_components":[
                            {
                                "long_name":"94043",
                                "types":["postal_code"]
                            }
                        ]
                    }
                ]
            }""";

        Mockito.when(appConfiguration.getAddressServiceApiKey()).thenReturn("testApiKey");
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(responseJson);

        Location location = addressService.getLocation(address);

        assertNotNull(location);
        assertEquals("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA", location.getFormattedAddress());
    }

    @Test
    void getLocation_missing_zip() throws IOException {
        String address = "1600 Amphitheatre Parkway, Mountain View, CA";
        String responseJson = """
            {
                "status":"OK",
                "results":[
                    {
                        "formatted_address":"1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA",
                        "geometry":{
                            "location": {
                                "lat":37.4220041,
                                "lng":-122.0833494
                            }
                        },
                        "address_components":[]
                    }
                ]
            }""";

        Mockito.when(appConfiguration.getAddressServiceApiKey()).thenReturn("testApiKey");
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(responseJson);

        Location location = addressService.getLocation(address);

        assertEquals(null, location);
    }

    @Test
    void getLocation_failure_apiError() throws IOException {
        String address = "invalid address";
        String responseJson = "{\"status\":\"ZERO_RESULTS\"}";

        Mockito.when(appConfiguration.getAddressServiceApiKey()).thenReturn("testApiKey");
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(responseJson);

        Location location = addressService.getLocation(address);

        assertEquals(null, location);
    }

    @Test
    void getLocation_failure_noResults() throws IOException {
        String address = "some address";
        String responseJson = "{\"status\":\"OK\",\"results\":[]}";

        Mockito.when(appConfiguration.getAddressServiceApiKey()).thenReturn("testApiKey");
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(responseJson);

        Location location = addressService.getLocation(address);

        assertEquals(null, location);
    }
}
