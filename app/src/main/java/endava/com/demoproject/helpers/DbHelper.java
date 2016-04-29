package endava.com.demoproject.helpers;

import android.util.Log;

import java.sql.SQLException;
import java.util.List;

import endava.com.demoproject.db.DataBaseManager;
import endava.com.demoproject.db.HelperProvider;
import endava.com.demoproject.model.Owner;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.model.User;


public class DbHelper {
    public static final Integer FIRST_ITEM = 0;

    private static DbHelper helper;
    private DataBaseManager dbHelper = HelperProvider.getHelper();

//    public static DbHelper getInstance() {
//        if (helper == null) {
//            helper = new DbHelper();
//            return helper;
//        } else {
//            return helper;
//        }
//    }

    public User getUser() {
        try {
            return dbHelper.getUserDAO().queryForAll().get(FIRST_ITEM);
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), e.toString());
            return null;
        }
    }

    public void createUser(User user) {
        try {
            dbHelper.getUserDAO().createOrUpdate(user);
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), e.toString());
        }
    }

    public void deleteUser(User user) {
        try {
            dbHelper.getUserDAO().delete(user);
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), e.toString());
        }
    }

    public void updateUser(User user) {
        try {
            dbHelper.getUserDAO().update(user);
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), e.toString());
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

    public void updateRepo(Repo repo) {
        try {
            dbHelper.getRepoDAO().update(repo);
        } catch (SQLException e) {
            Log.d("SQLException", e.toString());
        }
    }

    public void createRepos(final List<Repo> list) {
        List<Repo> repoToDeleteList;
        List<Owner> ownerToDeleteList;
        try {
            repoToDeleteList = dbHelper.getRepoDAO().queryForAll();
            ownerToDeleteList = dbHelper.getOwnerDAO().queryForAll();

            for (Owner owner : ownerToDeleteList) {
                dbHelper.getOwnerDAO().delete(owner);
            }
            for (Repo repo : repoToDeleteList) {
                dbHelper.getRepoDAO().delete(repo);
            }
            for (Repo repo : list) {
                dbHelper.getOwnerDAO().createOrUpdate(repo.getOwner());
                dbHelper.getRepoDAO().createOrUpdate(repo);
            }
        } catch (SQLException e) {
            Log.d("SQLException ", e.toString());
        }
    }
}
