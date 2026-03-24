package com.raina.siliconvalleytrail.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RouteApiServiceTest {

    private RouteApiService routeApiService;

    // coordinates for testing
    private static final double LINCOLN_LAT = 40.8136;
    private static final double LINCOLN_LON = -96.7026;
    private static final double OLES_LAT = 41.1156;
    private static final double OLES_LON = -101.3684;
    private static final double DENVER_LAT = 39.7392;
    private static final double DENVER_LON = -104.9903;
    private static final double SAN_FRANCISCO_LAT = 37.7749;
    private static final double SAN_FRANCISCO_LON = -122.4194;

    @BeforeEach
    void setUp() {
        routeApiService = new RouteApiService();
    }

    // ── live API tests ────────────────────────────────────────────

    @Test
    void getDistanceInMiles_lincolnToOles_shouldBeApproximately287Miles() {
        int miles = routeApiService.getDistanceInMiles(
                LINCOLN_LON, LINCOLN_LAT,
                OLES_LON, OLES_LAT);
        // API returns ~255 miles
        assertTrue(miles > 235 && miles < 275,
                "Lincoln to Ole's should be ~255 miles but was: " + miles);
    }

    @Test
    void getDistanceInMiles_shouldReturnPositiveNumber() {
        int miles = routeApiService.getDistanceInMiles(
                LINCOLN_LON, LINCOLN_LAT,
                DENVER_LON, DENVER_LAT);

        assertTrue(miles > 0,
                "Distance should be positive but was: " + miles);
    }

    @Test
    void getDistanceInMiles_lincolnToSanFrancisco_shouldBeRoughly1900Miles() {
        int miles = routeApiService.getDistanceInMiles(
                LINCOLN_LON, LINCOLN_LAT,
                SAN_FRANCISCO_LON, SAN_FRANCISCO_LAT);
        // API returns ~1615 miles
        assertTrue(miles > 1500 && miles < 1750,
                "Lincoln to SF should be ~1615 miles but was: " + miles);
    }

    // ── fallback tests ────────────────────────────────────────────

    @Test
    void getDistanceInMiles_invalidCoordinates_shouldReturnNegativeOne() {
        // passing obviously invalid coordinates
        int miles = routeApiService.getDistanceInMiles(
                0.0, 0.0,
                0.0, 0.0);

        // should return -1 signaling fallback needed
        assertEquals(-1, miles,
                "Invalid coordinates should return -1 for fallback");
    }

    // ── coordinate sanity tests ───────────────────────────────────

    @Test
    void lincolnCoordinates_longitudeShouldBeNegative() {
        assertTrue(LINCOLN_LON < 0,
                "Lincoln longitude should be negative (western hemisphere)");
    }

    @Test
    void sanFranciscoCoordinates_longitudeShouldBeNegative() {
        assertTrue(SAN_FRANCISCO_LON < 0,
                "SF longitude should be negative (western hemisphere)");
    }

    @Test
    void allWesternCoordinates_shouldBeNegativeLongitude() {
        double[] longitudes = {
                LINCOLN_LON, OLES_LON, DENVER_LON, SAN_FRANCISCO_LON
        };
        for (double lon : longitudes) {
            assertTrue(lon < 0,
                    "All US longitudes should be negative but found: " + lon);
        }
    }

    // ── conversion sanity test ────────────────────────────────────

    @Test
    void getDistanceInMiles_sameLocation_shouldBeZeroOrNearZero() {
        int miles = routeApiService.getDistanceInMiles(
                LINCOLN_LON, LINCOLN_LAT,
                LINCOLN_LON, LINCOLN_LAT);

        assertTrue(miles <= 1,
                "Same location should return 0 or near 0 but was: " + miles);
    }
}