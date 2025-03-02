package com.example.weatherapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    // Formatted address of the location (e.g., street address, city, state).
    public String formattedAddress;

    // Zip code of the location.
    public String zipCode;

    // Latitude coordinate of the location.
    public Double latitude;

    // Longitude coordinate of the location.
    public Double longitude;
}
