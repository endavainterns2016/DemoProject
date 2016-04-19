package endava.com.demoproject.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import endava.com.demoproject.cacheableObserver.Subject;
import endava.com.demoproject.events.refreshReposListEvent;

public class RefreshReposListService extends Service {
    private Handler mHandler = new Handler();
    private Subject subject = Subject.newInstance();
    private refreshReposListEvent  refreshEvent = new refreshReposListEvent();
    private int refreshPeriod;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        refreshPeriod = intent.getIntExtra("autoSyncInterval", 60000);
        startRefreshHandler();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(refreshTask);
        mHandler = null;
    }



    public void startRefreshHandler() {
        mHandler.postDelayed(refreshTask, 10000);
    }
    private Runnable refreshTask = new Runnable() {
        public void run() {
            Log.d("refreshService", "in service refreshed");
            subject.onNewEvent(refreshEvent);
            mHandler.postDelayed(this, 10000);
        }
    };
}
