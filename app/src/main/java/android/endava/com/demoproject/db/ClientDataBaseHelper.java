package android.endava.com.demoproject.db;

import android.endava.com.demoproject.model.User;
import android.util.Log;

import java.util.HashMap;


public class ClientDataBaseHelper {
    public static final String HELPER_TAG = "HELPER";
    public static final Integer FIRST_ITEM = 0;

    private static HashMap<String, ClientDataBaseHelper> helperMap = new HashMap<>();
    private DataBaseHelper dbHelper = HelperFactory.getHelper();

    public static ClientDataBaseHelper getInstance() {
        if (!helperMap.containsKey(HELPER_TAG)) {
            ClientDataBaseHelper helper = new ClientDataBaseHelper();
            helperMap.put(HELPER_TAG, helper);
            return helper;
        } else {
            return helperMap.get(HELPER_TAG);
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
