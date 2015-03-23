package au.com.lukesleeman.trainwatch.webservice;

import com.google.gson.annotations.SerializedName;

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
