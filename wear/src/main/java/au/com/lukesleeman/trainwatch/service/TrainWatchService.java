package au.com.lukesleeman.trainwatch.service;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import au.com.lukesleeman.trainwatch.LogTags;
import au.com.lukesleeman.trainwatch.domain.Train;

/**
 * Wraps up all backend code for accessing the phone to get trains.
 */
@EBean
public class TrainWatchService {

    @RootContext
    protected Context context;

    private GoogleApiClient googleApiClient;

    public void sendGetTrainsMessage(){
        Log.d(LogTags.WEAR, "Starting to build client");

        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(LogTags.WEAR, "onConnected: " + connectionHint);
                        // Now you can use the Data Layer API
                        getNodes();
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(LogTags.WEAR, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(LogTags.WEAR, "onConnectionFailed: " + result);
                    }
                })
                // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();

        googleApiClient.connect();
    }

    private void getNodes() {
        Wearable.NodeApi.getConnectedNodes(googleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                List<Node> node = getConnectedNodesResult.getNodes();

                Log.i(LogTags.WEAR, "Found " + node.size() + " nodes");
                String firstNode = node.get(0).getId();

                sendMessage(firstNode);
            }
        });
    }

    private void sendMessage(String node){

        Log.i(LogTags.WEAR, "Sending message to " + node);

        Wearable.MessageApi.sendMessage(
                googleApiClient, node, "/huh", new byte[0]).setResultCallback(
                new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.e(LogTags.WEAR, "Failed to send message with status code: "
                                    + sendMessageResult.getStatus().getStatusCode());
                        }
                        else{
                            Log.e(LogTags.WEAR, "Success sending message with status code: "
                                    + sendMessageResult.getStatus().getStatusCode());
                        }
                    }
                }
        );
    }

    /**
     * Does everything - connects to the watch, gets the location, fetches the trains, and so on
     *
     * @return A list of the next trains to depart from the nearest train station.
     */
    public List<Train> getNextTrains(){

        /*
        // Pretend to do some webserice stuff
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */




        // Build some fake trains!

        List<Train> trains = new ArrayList<>();
        trains.add(new Train("Flinder st", "Platform 1", false));
        trains.add(new Train("Flinder st", "Platform 1", true));
        trains.add(new Train("Werribee", "Platform 2", false));
        trains.add(new Train("Williamstown", "Platform 2", false));

        return trains;
    }
}
