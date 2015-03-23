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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Static methods for connecting to the PTV webservice.
 */
public class PTVWebservice {

    private static final String BASE_URL = "http://timetableapi.ptv.vic.gov.au/";
    private static final int DEV_ID = 1000390;
    private static final String KEY = "2bdb5b4c-cd01-11e4-913b-061817890ad2";


    public static HealthCheckResult healthCheck() throws IOException {

        String url = "/v2/healthcheck";
        url += "?timestamp=" +fromCalendar(Calendar.getInstance());
        url = generateCompleteURLWithSignature(KEY, url, DEV_ID);

        Reader reader = new InputStreamReader(new URL(url).openConnection().getInputStream());

        Gson gson = new Gson();
        return gson.fromJson(reader, HealthCheckResult.class);
    }

    public static void nearbyStations(double latitude, double longitude){

    }

    public static void nextDepartures(){

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


    /**
     * Transform Calendar to ISO 8601 UTC format string.
     */
    private static String fromCalendar(final Calendar calendar) {

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
