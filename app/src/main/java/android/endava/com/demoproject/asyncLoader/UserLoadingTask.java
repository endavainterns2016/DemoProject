package android.endava.com.demoproject.asyncLoader;

import android.content.Context;
import android.endava.com.demoproject.db.ClientDataBaseHelper;
import android.endava.com.demoproject.model.User;

public class UserLoadingTask extends DemoLoader<User> {
    private ClientDataBaseHelper dbHelper = ClientDataBaseHelper.getInstance();

    public UserLoadingTask(Context context) {
        super(context);
    }

    @Override
    public User loadInBackground() {
        return dbHelper.getUser();
    }
}


