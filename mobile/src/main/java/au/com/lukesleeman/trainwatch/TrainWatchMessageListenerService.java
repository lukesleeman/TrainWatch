package au.com.lukesleeman.trainwatch;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.androidannotations.annotations.EService;

/**
 * A WearableListenerService which listens for messages from our watch
 */
@EService
public class TrainWatchMessageListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        Log.i(LogTags.APP, "Received message " + messageEvent.toString());

    }
}
