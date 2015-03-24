package au.com.lukesleeman.trainwatch.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * Timetable direction from the broad next departures api
 */
public class TimetableDirection {

    @SerializedName("direction_name")
    private String directionName;

    public String getDirectionName() {
        return directionName;
    }
}
