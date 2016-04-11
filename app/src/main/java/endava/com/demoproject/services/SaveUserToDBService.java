package endava.com.demoproject.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import endava.com.demoproject.SaveUserToDB;
import endava.com.demoproject.cacheableObserver.Subject;
import endava.com.demoproject.events.UserWasSavedToDbEvent;


public class SaveUserToDBService extends Service {
    private Subject subject = Subject.newInstance();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SaveUserToDB command = intent.getParcelableExtra("command");
                command.execute();
                UserWasSavedToDbEvent event = new UserWasSavedToDbEvent();
                subject.onNewEvent(event);
            }
        }).start();
        return Service.START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }
}
