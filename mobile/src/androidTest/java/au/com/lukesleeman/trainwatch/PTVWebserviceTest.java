package au.com.lukesleeman.trainwatch;


import android.test.AndroidTestCase;

import au.com.lukesleeman.trainwatch.webservice.HealthCheckResult;
import au.com.lukesleeman.trainwatch.webservice.PTVWebservice;
import au.com.lukesleeman.trainwatch.webservice.StopResult;
import au.com.lukesleeman.trainwatch.webservice.TimetableResult;
import au.com.lukesleeman.trainwatch.webservice.TimetableValues;

/**
 * Tests for connecting to the PTV webservice
 */
public class PTVWebserviceTest extends AndroidTestCase {

    public void testHealthCheck() throws Exception {
        HealthCheckResult result = PTVWebservice.healthCheck();

        assertNotNull(result);

        assertTrue(result.isDatabaseOK());
        assertTrue(result.isMemcacheOK());
        assertTrue(result.isSecurityTokenOK());
        assertTrue(result.isClientClockOK());
    }

    public void testStopsNearby() throws Exception {
        // Locaiton of my house!
        double latitude = -37.8605974;
        double longitude = 144.743913;

        StopResult[] stops = PTVWebservice.nearbyStations(latitude, longitude);
        assertNotNull(stops);
        assertEquals(30, stops.length);

        // Check the first stop
        StopResult palmersRdBus = stops[0];
        assertEquals("Palmers Rd ", palmersRdBus.getResult().getLocationName());
        assertEquals("bus", palmersRdBus.getResult().getTransportType());

        // Check the station
        StopResult station = stops[5];
        assertEquals("Williams Landing ", station.getResult().getLocationName());
        assertEquals("train", station.getResult().getTransportType());
        assertEquals(1225, station.getResult().getStopId());
    }

    public void testNextDepatures() throws Exception{

        TimetableResult departures = PTVWebservice.nextDepartures(1225);
        assertNotNull(departures);
        assertEquals(30, departures.getValues().length);
    }
}