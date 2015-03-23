package au.com.lukesleeman.trainwatch.webservice;

import com.google.gson.annotations.SerializedName;

/**
 * The timetable values object which contains information about one train, returnd as part
 * of the broad next departures API.
 */
public class TimetableValues {

    @SerializedName("time_timetable_utc")
    private String timeUtc;

    private TimetableRun run;

    public String getTimeUtc() {
        return timeUtc;
    }

    public TimetableRun getRun() {
        return run;
    }
}
