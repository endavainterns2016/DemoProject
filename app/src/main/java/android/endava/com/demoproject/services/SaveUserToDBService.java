package android.endava.com.demoproject.services;

import android.app.Service;
import android.content.Intent;
import android.endava.com.demoproject.SaveUserToDB;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class SaveUserToDBService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SaveUserToDB command = intent.getParcelableExtra("command");
                command.execute();
                Intent i = new Intent("user_was_saved_to_db");
                sendBroadcast(i);
            }
        }).start();
        return Service.START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }
}
