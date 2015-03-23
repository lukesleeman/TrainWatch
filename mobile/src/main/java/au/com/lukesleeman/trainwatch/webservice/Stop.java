package au.com.lukesleeman.trainwatch.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a stop from the neaby stops API
 */
public class Stop {

    private String suburb;

    @SerializedName("transport_type")
    private String transportType;

    @SerializedName("stop_id")
    private int stopId;

    @SerializedName("location_name")
    private String locationName;

    private double lat;
    private double lon;
    private double distance;

    public String getSuburb() {
        return suburb;
    }

    public String getTransportType() {
        return transportType;
    }

    public int getStopId() {
        return stopId;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getDistance() {
        return distance;
    }
}
