package au.com.lukesleeman.trainwatch;


import android.test.AndroidTestCase;

import au.com.lukesleeman.trainwatch.webservice.HealthCheckResult;
import au.com.lukesleeman.trainwatch.webservice.PTVWebservice;

/**
 * Tests for connecting to the PTV webservice
 */
public class PTVWebserviceTest extends AndroidTestCase {

    public void testHealthCheck() throws Exception {
        HealthCheckResult result = PTVWebservice.healthCheck();

        assertNotNull(result);

        assertTrue(result.isDatabaseOK());
        assertTrue(result.isMemcacheOK());
        assertTrue(result.isClientClockOK());
        assertTrue(result.isSecurityTokenOK());
    }
}