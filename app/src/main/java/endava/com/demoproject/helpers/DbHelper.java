package endava.com.demoproject.helpers;

import android.util.Log;

import java.sql.SQLException;
import java.util.List;

import endava.com.demoproject.db.DataBaseManager;
import endava.com.demoproject.db.HelperProvider;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.model.User;


public class DbHelper {
    public static final Integer FIRST_ITEM = 0;

    private static DbHelper helper;
    private DataBaseManager dbHelper = HelperProvider.getHelper();

    public static DbHelper getInstance() {
        if (helper == null) {
            helper = new DbHelper();
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
            dbHelper.getUserDAO().createOrUpdate(user);
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

    public Repo getRepoById(Integer id) {
        try {
            return dbHelper.getRepoDAO().queryForId(id);
        } catch (SQLException e) {
            Log.d("SQLException ", e.toString());
            return null;
        }
    }

    public void createRepos(final List<Repo> list) {
        try {
            for (Repo repo : list) {
                dbHelper.getRepoDAO().createOrUpdate(repo);
            }
        } catch (SQLException e) {
            Log.d("SQLException ", e.toString());
        }
    }
}
