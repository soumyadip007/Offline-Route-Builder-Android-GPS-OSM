package MergerDTN;


import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Handles reading and writing of messages with socket buffers. Uses a Handler
 * to post messages to UI thread for UI updates.
 */
public class ChatManager implements Runnable {

    private static final String TAG = "adhocm-ChatHandler";
    private Socket socket = null;
    private Handler handler;
    private InputStream iStream;
    private OutputStream oStream;

    public ChatManager(Socket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    //    public static boolean isRunning = true;
    @Override
    public void run() {
        try {

            iStream = socket.getInputStream();
            oStream = socket.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytes;
            handler.obtainMessage(WiFiServiceDiscoveryActivity.MY_HANDLE, this)
                    .sendToTarget();

            while (!socket.isClosed()) {
                try {
                    // Read from the InputStream
                    if (socket.isClosed())
                        break;
                    Log.d(TAG, "ISCLOSED: " + socket.isClosed() + " - ISCONNECTED: " + socket.isConnected());
                    bytes = iStream.read(buffer);
                    if (bytes == -1) {
                        break;
                    }

                    // Send the obtained bytes to the UI Activity
                    Log.d(TAG, "Rec:" + buffer.toString());
                    handler.obtainMessage(WiFiServiceDiscoveryActivity.MESSAGE_READ,
                            bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(byte[] buffer) {
        try {
            oStream.write(buffer);
        } catch (SocketException se) {
            Log.e(TAG, "Exception during write - " + se.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Exception during write - " + e.getMessage());
        }
    }

}
