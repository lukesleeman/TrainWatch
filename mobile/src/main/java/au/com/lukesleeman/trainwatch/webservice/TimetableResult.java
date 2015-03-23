package au.com.lukesleeman.trainwatch.webservice;

/**
 * The timetable result returned as part of the broad next departures api
 */
public class TimetableResult {

    private TimetableValues [] values;

    public TimetableValues[] getValues() {
        return values;
    }
}
