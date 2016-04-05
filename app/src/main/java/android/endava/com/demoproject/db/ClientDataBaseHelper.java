package android.endava.com.demoproject.db;

import android.endava.com.demoproject.model.User;
import android.util.Log;


public class ClientDataBaseHelper {
    public static final Integer FIRST_ITEM = 0;

    private static ClientDataBaseHelper helper;
    private DataBaseHelper dbHelper = HelperFactory.getHelper();

    public static ClientDataBaseHelper getInstance() {
        if (helper == null) {
            helper = new ClientDataBaseHelper();
            return helper;
        } else {
            return helper;
        }
    }

    public User getUser() {
        try {
            return dbHelper.getUserDAO().queryForAll().get(FIRST_ITEM);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.toString());
            return null;
        }
    }

    public void createUser(User user) {
        try {
            dbHelper.getUserDAO().create(user);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.toString());
        }
    }

    public void deleteUser(User user) {
        try {
            dbHelper.getUserDAO().delete(user);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.toString());
        }
    }

    public void updateUser(User user) {
        try {
            dbHelper.getUserDAO().update(user);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.toString());
        }
    }
}
