package au.com.lukesleeman.trainwatch.webservice;

/**
 * A stop results, returned as part of the nearby stops API.
 */
public class StopResult {

    private String type;
    private Stop result;

    public String getType() {
        return type;
    }

    public Stop getResult() {
        return result;
    }
}
