package au.com.lukesleeman.trainwatch.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * The run object which forms part of the TimetableValues object returned from the broad next
 * departures API.
 */
public class TimetableRun {

    @SerializedName("num_skipped")
    private int numSkipped;

    @SerializedName("destination_name")
    private String destinationName;

    public int getNumSkipped() {
        return numSkipped;
    }

    public String getDestinationName() {
        return destinationName;
    }
}
