package android.endava.com.demoproject.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class RefreshReposListService extends Service {
    private Handler mHandler = new Handler();
    private int refreshPeriod;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        refreshPeriod = intent.getIntExtra("autoSyncInterval", 1000);
        startRefreshHandler();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(refreshTask);
        mHandler = null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    public void startRefreshHandler() {
        mHandler.postDelayed(refreshTask, refreshPeriod);
    }
    private Runnable refreshTask = new Runnable() {
        public void run() {
            Log.d("refreshService", "in service refreshed");
            sendBroadcast(new Intent("refreshReposList"));
            mHandler.postDelayed(this, refreshPeriod);
        }
    };
}
