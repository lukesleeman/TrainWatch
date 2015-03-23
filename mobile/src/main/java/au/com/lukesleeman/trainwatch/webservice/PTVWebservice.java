package au.com.lukesleeman.trainwatch.webservice;

import android.text.format.DateUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Static methods for connecting to the PTV webservice.
 */
public class PTVWebservice {

    private static final String BASE_URL = "http://timetableapi.ptv.vic.gov.au/";

    public static HealthCheckResult healthCheck() throws IOException {

        String url = BASE_URL + "v2/healthcheck?timestamp=" +fromCalendar(Calendar.getInstance());
        Reader reader = new InputStreamReader(new URL(url).openConnection().getInputStream());

        Gson gson = new Gson();
        return gson.fromJson(reader, HealthCheckResult.class);
    }

    public static void nearbyStations(double latitude, double longitude){

    }

    public static void nextDepartures(){

    }


    /**
     * Transform Calendar to ISO 8601 UTC format string.
     */
    public static String fromCalendar(final Calendar calendar) {

        // Convert to GMT (from http://stackoverflow.com/a/230383/1599946)
        Date date = calendar.getTime();
        TimeZone tz = calendar.getTimeZone();

        // Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
        long msFromEpochGmt = date.getTime();

        // gives you the current offset in ms from GMT at the current date
        int offsetFromUTC = tz.getOffset(msFromEpochGmt);

        //create a new calendar in GMT timezone, set to this date and add the offset
        Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        gmtCal.setTime(date);
        gmtCal.add(Calendar.MILLISECOND, -offsetFromUTC);

        // Transform to formatted string (from http://stackoverflow.com/a/10621553/1599946)
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(gmtCal.getTime());
        return formatted;
    }
}
