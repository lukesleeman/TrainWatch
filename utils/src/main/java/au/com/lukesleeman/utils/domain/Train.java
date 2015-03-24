package au.com.lukesleeman.utils.domain;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a train leaving from the nearest train station.
 */
public class Train implements Serializable {

    private String destination;
    private String departingFrom;
    private boolean isExpress;
    private long time;


    public Train(String destination, String departingFrom, long time, boolean isExpress) {
        this.destination = destination;
        this.departingFrom = departingFrom;
        this.isExpress = isExpress;
        this.time = time;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartingFrom() {
        return departingFrom;
    }

    public boolean isExpress() {

        return isExpress;
    }

    public String getMinutesToArrive(){
        String minToArrive = DateUtils.getRelativeTimeSpanString(time,
                System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE).toString();

        // Strip the 'min' thing
        if(minToArrive.startsWith("in ")){
            minToArrive = minToArrive.substring(3);
        }

        return minToArrive;
    }

    public String getTime(Context context){
        return DateFormat.getTimeFormat(context).format(new Date(time));
    }
}
