package com.example.AppEcommerce.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/map")

public class GeocodingController {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;
    @GetMapping("/adresse")
    public ResponseEntity<String> obtenirAdresse(@RequestParam("lat") double latitude, @RequestParam("long") double longitude) {
        String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=" + googleMapsApiKey;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        return response;
    }
}