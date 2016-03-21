package android.endava.com.demoproject.db;


import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.endava.com.demoproject.model.App;
import android.endava.com.demoproject.model.User;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "demoProject.db";
    private static final int DATABASE_VERSION = 2;

    private UserDAO userDAO = null;
    private AppDAO appDAO = null;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, App.class);
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, App.class, true);
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
        onCreate(database, connectionSource);
    }

    public UserDAO getUserDAO() throws SQLException{
        if(userDAO == null){
            userDAO = new UserDAO(getConnectionSource(), User.class);
        }
        return userDAO;
    }

    public AppDAO getAppDAO() throws SQLException{
        if(appDAO == null){
            appDAO = new AppDAO(getConnectionSource(), App.class);
        }
        return appDAO;
    }
}
