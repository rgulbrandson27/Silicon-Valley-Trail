package com.raina.siliconvalleytrail.service.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RouteApiService {

    private static final String BASE_URL = "http://router.project-osrm.org/route/v1/driving/";
    private static final double METERS_TO_MILES = 0.000621371;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public RouteApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // returns distance in miles between two coordinates
    // lon1,lat1 → lon2,lat2 (note: longitude FIRST in OSRM)
    public int getDistanceInMiles(double lon1, double lat1, double lon2, double lat2) {
        try {
            String url = BASE_URL + lon1 + "," + lat1 + ";" + lon2 + "," + lat2
                    + "?overview=false";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString());

            JsonNode json = objectMapper.readTree(response.body());
            JsonNode routes = json.get("routes");

            if (routes != null && routes.isArray() && !routes.isEmpty()) {
                double meters = routes.get(0).get("distance").asDouble();
                return (int) Math.round(meters * METERS_TO_MILES);
            }

            System.out.println("⚠️  Route API returned no results — using fallback distance.");
            return -1;  // signals fallback needed

        } catch (Exception e) {
            System.out.println("⚠️  Route API unavailable — using fallback distance.");
            return -1;  // signals fallback needed
        }
    }
}