package endava.com.demoproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStateChangedReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "NetworkChangedReceiver";
    private boolean isConnected = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        isNetworkAvailable(context);
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        if (!isConnected) {
                            Log.d(LOG_TAG, "Now you are connected to Internet!");
                            isConnected = true;

                            context.sendBroadcast(new Intent("refreshReposListOnConnectionRestore"));
                        }
                        return true;
                    }
                }
            }
        }
        Log.d(LOG_TAG, "You are now diconnected from Internet!");
        isConnected = false;
        return false;
    }

    }