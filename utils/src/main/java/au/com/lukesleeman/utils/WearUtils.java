package au.com.lukesleeman.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

/**
 * Created by luke on 23/03/15.
 */
public class WearUtils {

    public static void sendMessage(String path, byte [] messageConents, Context context){
        Log.d(LogTags.UTILS, "Starting to build client");

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();

        if(googleApiClient.blockingConnect().isSuccess()) {

            Log.i(LogTags.UTILS, "Successfully connected, getting nodes");

            // Get the nodes
            List<Node> nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await().getNodes();

            Log.i(LogTags.UTILS, "Found " + nodes.size() + " nodes");
            String firstNode = nodes.get(0).getId();

            // Send the message
            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(googleApiClient, firstNode, path, messageConents).await();
            if (!result.getStatus().isSuccess()) {
                Log.e(LogTags.UTILS, "Failed to send message with status code: "
                        + result.getStatus().getStatusCode());
            }
            else {
                Log.e(LogTags.UTILS, "Success sending message " + path + " with status code: "
                        + result.getStatus().getStatusCode());
            }

            googleApiClient.disconnect();
        }
        else {
            Log.i(LogTags.UTILS, "Error connecting to google apis");
        }
    }
}
