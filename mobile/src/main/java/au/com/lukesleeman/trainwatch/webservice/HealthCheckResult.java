package au.com.lukesleeman.trainwatch.webservice;

/**
 * Results of calling the healthcheck webservice;
 */
public class HealthCheckResult {

    private boolean securityTokenOK;
    private boolean clientClockOK;
    private boolean memcacheOK;
    private boolean databaseOK;

    public boolean isSecurityTokenOK() {
        return securityTokenOK;
    }

    public boolean isClientClockOK() {
        return clientClockOK;
    }

    public boolean isMemcacheOK() {
        return memcacheOK;
    }

    public boolean isDatabaseOK() {
        return databaseOK;
    }
}
