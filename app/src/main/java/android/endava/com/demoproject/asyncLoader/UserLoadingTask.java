package android.endava.com.demoproject.asyncLoader;

import android.content.Context;
import android.endava.com.demoproject.db.ClientDataBaseHelper;
import android.endava.com.demoproject.model.User;

import java.util.concurrent.TimeUnit;

public class UserLoadingTask extends DemoLoader<User> {
    private ClientDataBaseHelper dbHelper = ClientDataBaseHelper.getInstance();

    public UserLoadingTask(Context context) {
        super(context);
    }

    @Override
    public User loadInBackground() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dbHelper.getUser();
    }
}


