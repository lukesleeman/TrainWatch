package au.com.lukesleeman.trainwatch;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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

            try {
                // Get our lat/long
                Location location = getLastLocation();

                // Use that to try and get the nearest stations



                // Build a list of trains
                List<Train> trains = new ArrayList<>();
                trains.add(new Train("Flinders st", "Platform 1", false));
                trains.add(new Train("Flinders st", "Platform 1", true));
                trains.add(new Train("Werribee", "Platform 2", false));
                trains.add(new Train("Williamstown", "Platform 2", false));

                // Send a message back ...
                GoogleApiClient client = WearUtils.getConnectedWearClient(getApplicationContext());
                WearUtils.sendMessage("got-trains", WearUtils.trainsToBytes(trains), client);
                client.disconnect();
            }
            catch (Exception e) {
                Log.e(LogTags.APP, "Error sending trains", e);
            }
        }
    }

    private Location getLastLocation() throws Exception {
        GoogleApiClient googleApiClient =
            new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .build();

        ConnectionResult result = googleApiClient.blockingConnect();

        if(result.isSuccess()){
            return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
        else {
            throw new Exception("Error getting play services client - error code " + result.getErrorCode());
        }
    }

//    private void get

}
