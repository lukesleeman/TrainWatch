package au.com.lukesleeman.trainwatch.domain;

import java.io.Serializable;

/**
 * Represents a train leaving from the nearest train station.
 */
public class Train implements Serializable {

    private String destination;
    private String paltform;
    private boolean isExpress;
    private long time;


    public Train(String destination, String paltform, boolean isExpress) {
        this.destination = destination;
        this.paltform = paltform;
        this.isExpress = isExpress;
    }

    public String getDestination() {
        return destination;
    }

    public String getPaltform() {
        return paltform;
    }

    public boolean isExpress() {

        return isExpress;
    }

    public String getMinutesToArrive(){
        return "5 min";
    }

    public String getTime(){
        return "9:15 am";
    }
}
