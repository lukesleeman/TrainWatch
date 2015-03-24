package au.com.lukesleeman.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;

import au.com.lukesleeman.utils.domain.Train;

/**
 * Util functions used by both the phone app and the watch app to communicate.
 */
public class WearUtils {

    public static GoogleApiClient getConnectedWearClient(Context context) throws Exception {
        Log.d(LogTags.UTILS, "Starting to build client");
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();

        ConnectionResult result = googleApiClient.blockingConnect();
        if(result.isSuccess()) {
            return googleApiClient;
        }
        else {
            throw new Exception("Error getting play services client - error code "
                    + result.getErrorCode());
        }
    }

    public static void sendMessage(String path, byte [] messageConents, GoogleApiClient googleApiClient)
            throws IOException{
        Log.i(LogTags.UTILS, "Getting nodes");

        // Get the nodes
        List<Node> nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await().getNodes();

        if(nodes.isEmpty()){
            throw new IOException("Couldn't connect to device");
        }

        Log.i(LogTags.UTILS, "Found " + nodes.size() + " nodes");
        String firstNode = nodes.get(0).getId();

        // Send the message
        MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                googleApiClient, firstNode, path, messageConents).await();

        if (!result.getStatus().isSuccess()) {
            Log.e(LogTags.UTILS, "Failed to send message with status code: "
                    + result.getStatus().getStatusCode());
            throw new IOException("Error sending message to device");
        }
        else {
            Log.e(LogTags.UTILS, "Success sending message " + path + " with status code: "
                    + result.getStatus().getStatusCode());
        }
    }

    public static byte [] trainsToBytes(List<Train> trains) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(trains);
            byte[] yourBytes = bos.toByteArray();
            return yourBytes;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    public static List<Train> bytesToTrains(byte [] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();

            return (List<Train>)o;
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

}
