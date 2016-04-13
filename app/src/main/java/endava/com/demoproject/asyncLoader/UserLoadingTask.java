package endava.com.demoproject.asyncLoader;

import android.content.Context;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.User;

public class UserLoadingTask extends DemoLoader<User> {
    private DbHelper dbHelper = DbHelper.getInstance();

    public UserLoadingTask(Context context) {
        super(context);
    }

    @Override
    public User loadInBackground() {
        return dbHelper.getUser();
    }
}


