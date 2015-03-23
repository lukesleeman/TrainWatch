package au.com.lukesleeman.trainwatch;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.androidannotations.annotations.EService;

import java.util.ArrayList;
import java.util.List;

import au.com.lukesleeman.utils.LogTags;
import au.com.lukesleeman.utils.WearUtils;
import au.com.lukesleeman.utils.domain.Train;

/**
 * A WearableListenerService which listens for messages from our watch
 */
@EService
public class TrainWatchMessageListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        Log.i(LogTags.APP, "Received message " + messageEvent.toString());

        if (messageEvent.getPath().equals("/get-trains")) {

            // Build a list of trains
            List<Train> trains = new ArrayList<>();
            trains.add(new Train("Flinder st", "Platform 1", false));
            trains.add(new Train("Flinder st", "Platform 1", true));
            trains.add(new Train("Werribee", "Platform 2", false));
            trains.add(new Train("Williamstown", "Platform 2", false));

            // Start the long and ardious process of sending the message back ...
            try {
                WearUtils.sendMessage("got-trains", WearUtils.trainsToBytes(trains), WearUtils.getConnectedWearClient(getApplicationContext()));
            }
            catch (Exception e) {
                Log.e(LogTags.APP, "Error sending trains", e);
            }
        }
    }
}
