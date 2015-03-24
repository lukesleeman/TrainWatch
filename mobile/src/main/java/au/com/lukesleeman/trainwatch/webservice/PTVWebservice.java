package au.com.lukesleeman.trainwatch.webservice;


import com.google.gson.Gson;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import au.com.lukesleeman.utils.domain.Train;

/**
 * Static methods for connecting to the PTV webservice.
 */
public class PTVWebservice {

    private static final String BASE_URL = "http://timetableapi.ptv.vic.gov.au/";
    private static final int DEV_ID = 1000390;
    private static final String KEY = "2bdb5b4c-cd01-11e4-913b-061817890ad2";


    public static HealthCheckResult healthCheck() throws IOException {

        String url = "/v2/healthcheck";
        url += "?timestamp=" + utcTimeStringFromCalendar(Calendar.getInstance());
        url = generateCompleteURLWithSignature(KEY, url, DEV_ID);

        Reader reader = new InputStreamReader(new URL(url).openConnection().getInputStream());

        Gson gson = new Gson();
        return gson.fromJson(reader, HealthCheckResult.class);
    }

    public static StopResult[] nearbyStations(double latitude, double longitude) throws IOException {
        String url = "/v2/nearme/latitude/" + latitude + "/longitude/" + longitude;
        url = generateCompleteURLWithSignature(KEY, url, DEV_ID);

        Reader reader = new InputStreamReader(new URL(url).openConnection().getInputStream());

        Gson gson = new Gson();
        return gson.fromJson(reader, StopResult[].class);
    }

    public static TimetableResult nextDepartures(int stopId) throws IOException {
        String url = "/v2/mode/0/stop/"+stopId +"/departures/by-destination/limit/10";
        url = generateCompleteURLWithSignature(KEY, url, DEV_ID);


        Reader reader = new InputStreamReader(new URL(url).openConnection().getInputStream());

        Gson gson = new Gson();
        return gson.fromJson(reader, TimetableResult.class);
    }


    /**
     * Given a lat/long find the next trains to depart from the nearest station
     *
     * @param latitude
     * @param longitude
     * @return
     * @throws IOException
     */
    public static List<Train> nextTrains(double latitude, double longitude) throws Exception {

        // First find our nearest station
        StopResult [] stops = nearbyStations(latitude, longitude);
        StopResult station = null;
        for(StopResult stop : stops){
            if(stop.getResult().getTransportType().equals("train")){
                station = stop;
                break;
            }
        }

        if(station == null){
            throw new Exception("No nearby stations");
        }

        // Get the upcoming departures
        TimetableResult departures = nextDepartures(station.getResult().getStopId());

        // Transform them into trains
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        List<Train> trains = new ArrayList<>();
        for(TimetableValue departure : departures.getValues()){
            trains.add(new Train(
                    departure.getPlatform().getDirection().getDirectionName(),
                    station.getResult().getLocationName(),
                    localTimeFromUtcString(departure.getTimeUtc()),
                    departure.getRun().getNumSkipped() > 0));
        }

        return trains;
    }


    /**
     * Generates a signature using the HMAC-SHA1 algorithm
     *
     * @param privateKey - Developer Key supplied by PTV
     * @param uri - request uri (Example :/v2/HealthCheck)
     * @param developerId - Developer ID supplied by PTV
     * @return Unique Signature Value
     */
    private static String generateSignature(final String privateKey, final String uri, final int developerId)
    {
        String encoding = "UTF-8";
        String HMAC_SHA1_ALGORITHM = "HmacSHA1";
        String signature;
        StringBuffer uriWithDeveloperID = new StringBuffer();
        uriWithDeveloperID.append(uri).append(uri.contains("?") ? "&" : "?").append("devid="+developerId);
        try
        {
            byte[] keyBytes = privateKey.getBytes(encoding);
            byte[] uriBytes = uriWithDeveloperID.toString().getBytes(encoding);
            Key signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] signatureBytes = mac.doFinal(uriBytes);
            StringBuffer buf = new StringBuffer(signatureBytes.length * 2);
            for (byte signatureByte : signatureBytes)
            {
                int intVal = signatureByte & 0xff;
                if (intVal < 0x10)
                {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(intVal));
            }
            signature = buf.toString();
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
        catch (InvalidKeyException e)
        {
            throw new RuntimeException(e);
        }
        return signature.toString().toUpperCase();
    }

    /**
     * Generate full URL using generateSignature() method
     *
     * @param privateKey - Developer Key supplied by PTV (Example :  "92dknhh31-6a30-4cac-8d8b-8a1970834799");
     * @param uri - request uri (Example :"/v2/mode/2/line/787/stops-for-line)
     * @param developerId - Developer ID supplied by PTV( int developerId )
     * @return - Full URL with Signature
     */
    private static String generateCompleteURLWithSignature(final String privateKey, final String uri, final int developerId)
    {
        String baseURL="http://timetableapi.ptv.vic.gov.au";
        StringBuffer url = new StringBuffer(baseURL).append(uri).append(uri.contains("?") ? "&" : "?").append("devid="+developerId).append("&signature="+generateSignature(privateKey, uri, developerId));
        return url.toString();
    }

    private static final SimpleDateFormat UTC_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /**
     * Transform Calendar to ISO 8601 UTC format string.
     */
    private static String utcTimeStringFromCalendar(final Calendar calendar) {

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
        return UTC_DATE_FORMAT.format(gmtCal.getTime());
    }

    private static long localTimeFromUtcString(String dateString) throws ParseException {
        // Get a date in UTC timezone
        Date date = UTC_DATE_FORMAT.parse(dateString);

        // Convert from UTC to local time
        TimeZone tz = TimeZone.getDefault();

        // Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
        long msFromEpochGmt = date.getTime();

        // Gives you the current offset in ms from GMT at the current date
        int offsetFromUTC = tz.getOffset(msFromEpochGmt);

        // Create a new calendar in GMT timezone, set to this date and add the offset
        Calendar localCal = Calendar.getInstance();
        localCal.setTime(date);
        localCal.add(Calendar.MILLISECOND, offsetFromUTC);

        return localCal.getTimeInMillis();
    }
}
