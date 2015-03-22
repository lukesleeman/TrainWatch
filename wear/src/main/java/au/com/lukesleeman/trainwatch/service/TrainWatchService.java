package au.com.lukesleeman.trainwatch.service;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import au.com.lukesleeman.trainwatch.domain.Train;

/**
 * Wraps up all backend code for accessing the phone to get trains.
 */
@EBean
public class TrainWatchService {

    /**
     * Does everything - connects to the watch, gets the location, fetches the trains, and so on
     *
     * @return A list of the next trains to depart from the nearest train station.
     */
    public List<Train> getNextTrains(){

        // Pretend to do some webserice stuff
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Build some fake trains!

        List<Train> trains = new ArrayList<>();
        trains.add(new Train("Flinder st", "Platform 1", false));
        trains.add(new Train("Flinder st", "Platform 1", true));
        trains.add(new Train("Werribee", "Platform 2", false));
        trains.add(new Train("Williamstown", "Platform 2", false));

        return trains;
    }
}
