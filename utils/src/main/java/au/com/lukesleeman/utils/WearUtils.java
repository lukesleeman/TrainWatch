package au.com.lukesleeman.utils;

import android.content.Context;
import android.util.Log;

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
