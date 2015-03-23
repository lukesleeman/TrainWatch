package au.com.lukesleeman.trainwatch.webservice;

/**
 * The timetable result returned as part of the broad next departures api
 */
public class TimetableResult {

    private TimetableValue[] values;

    public TimetableValue[] getValues() {
        return values;
    }
}
