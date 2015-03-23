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
import java.util.List;

import au.com.lukesleeman.trainwatch.LogTags;

/**
 * Wraps up all backend code for accessing the phone to get trains.
 */
@EBean
public class TrainWatchService {

    @RootContext
    protected Context context;

    public void sendGetTrainsMessage(){
        Log.d(LogTags.WEAR, "Starting to build client");

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();

        if(googleApiClient.blockingConnect().isSuccess()) {

            Log.i(LogTags.WEAR, "Sucessfully connected, getting nodes");

            // Get the nodes
            List<Node> nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await().getNodes();

            Log.i(LogTags.WEAR, "Found " + nodes.size() + " nodes");
            String firstNode = nodes.get(0).getId();

            // Send the message
            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(googleApiClient, firstNode, "/get-trains", new byte[0]).await();
            if (!result.getStatus().isSuccess()) {
                Log.e(LogTags.WEAR, "Failed to send message with status code: "
                        + result.getStatus().getStatusCode());
            }
            else {
                Log.e(LogTags.WEAR, "Success sending message with status code: "
                        + result.getStatus().getStatusCode());
            }

            googleApiClient.disconnect();
        }
        else {
            Log.i(LogTags.WEAR, "Error connecting to google apis");
        }
    }
}
