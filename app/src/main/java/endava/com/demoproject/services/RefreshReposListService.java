package endava.com.demoproject.services;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;

import endava.com.demoproject.DemoProjectApplication;
import endava.com.demoproject.cacheableObserver.Subject;
import endava.com.demoproject.events.RefreshRepoListEvent;

public class RefreshReposListService extends Service {
    private Handler mHandler = new Handler();
    @Inject
    public Subject subject;
    @Inject
    public Resources resources;
    private RefreshRepoListEvent refreshEvent = new RefreshRepoListEvent(resources);
    private int refreshPeriod;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        DemoProjectApplication.getApplicationComponent().inject(this);
        super.onCreate();
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
        mHandler.postDelayed(refreshTask, refreshPeriod);
    }
    private Runnable refreshTask = new Runnable() {
        public void run() {
            Log.d("refreshService", "in service refreshed");
            subject.onNewEvent(refreshEvent);
            mHandler.postDelayed(this, refreshPeriod);
        }
    };
}
