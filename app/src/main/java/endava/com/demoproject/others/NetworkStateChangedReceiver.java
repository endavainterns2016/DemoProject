package endava.com.demoproject.others;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import endava.com.demoproject.cacheableObserver.Subject;
import endava.com.demoproject.events.RefreshRepoListEvent;

public class NetworkStateChangedReceiver extends BroadcastReceiver {

    private static boolean firstConnect = true;
    private Subject subject = Subject.newInstance();
    private RefreshRepoListEvent refreshEvent = new RefreshRepoListEvent();

    @Override
    public void onReceive(Context context, Intent intent) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null) {
            if (firstConnect) {
                Log.d("NetworkChangedReceiver", "network connected");
                subject.onNewEvent(refreshEvent);
                firstConnect = false;
            }
        } else {
            firstConnect = true;
        }
    }
}