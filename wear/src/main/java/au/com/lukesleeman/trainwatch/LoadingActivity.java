package au.com.lukesleeman.trainwatch;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.io.IOException;
import java.util.List;

import au.com.lukesleeman.utils.LogTags;
import au.com.lukesleeman.utils.WearUtils;
import au.com.lukesleeman.utils.domain.Train;

/**
 * Displays a loading screen while we do all the stuff - get a location, fetch trains, etc
 */
@EActivity(R.layout.activity_loading)
public class LoadingActivity extends Activity {

    @AfterViews
    protected void setupUi(){
        loadStuff();
    }

    private GoogleApiClient client;

    @Background
    protected void loadStuff(){

        client = new GoogleApiClient.Builder(this)
                // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();

        if(client.blockingConnect().isSuccess()) {

            Log.i(LogTags.UTILS, "Successfully connected, adding listener");

            Wearable.MessageApi.addListener(client, new MessageApi.MessageListener() {
                @Override
                public void onMessageReceived(MessageEvent messageEvent) {
                    Log.i(LogTags.WEAR, "Got response message" + messageEvent.getPath());

                    if(messageEvent.getPath().equals("got-trains")){
                        try {
                            List<Train> trains = WearUtils.bytesToTrains(messageEvent.getData());
                            Log.i(LogTags.WEAR, "Message includes " + trains.size() + " trains");
                            showResults(trains);
                        }
                        catch (Exception e) {
                            Log.e(LogTags.WEAR, "Error receiving trains message", e);
                            showError("Error receiving trains from phone");
                        }
                    }
                    else if(messageEvent.getPath().equals("got-error")){
                        String error = new String(messageEvent.getData());
                        showError(error);
                    }
                }
            });

            try{
                WearUtils.sendMessage("/get-trains", new byte[0], client);
            }
            catch (IOException e){
                Log.e(LogTags.WEAR, "Error sending train request message", e);
                showError("Couldn't connect to phone");
            }
        }
        else{
            showError("Couldn't connect to phone");
        }
    }

    @Override
    protected void onDestroy() {
        if(client != null){
            client.disconnect();
        }

        super.onDestroy();
    }

    @UiThread
    protected void showResults(List<Train> trains){
        TrainListActivity_.intent(this).trainList(trains).start();
        finish();
    }

    @UiThread
    protected void showError(String error){
        ErrorActivity_.intent(this).error(error).start();
        finish();
    }
}
