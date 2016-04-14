package endava.com.demoproject.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import endava.com.demoproject.db.dao.AppDAO;
import endava.com.demoproject.db.dao.OwnerDAO;
import endava.com.demoproject.db.dao.RepoDAO;
import endava.com.demoproject.db.dao.UserDAO;
import endava.com.demoproject.model.App;
import endava.com.demoproject.model.Owner;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.model.User;

public class DataBaseManager extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "demoProject.db";
    private static final int DATABASE_VERSION = 24;

    private UserDAO userDAO = null;
    private RepoDAO repoDAO = null;
    private OwnerDAO ownerDAO = null;
    private AppDAO appDAO = null;

    public DataBaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, Owner.class);
            TableUtils.createTableIfNotExists(connectionSource, Repo.class);
            TableUtils.createTableIfNotExists(connectionSource, App.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Owner.class, true);
            TableUtils.dropTable(connectionSource, Repo.class, true);
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

    public RepoDAO getRepoDAO() throws SQLException{
        if(repoDAO == null){
            repoDAO = new RepoDAO(getConnectionSource(), Repo.class);
        }
        return repoDAO;
    }

    public OwnerDAO getOwnerDAO() throws SQLException{
        if(ownerDAO == null){
            ownerDAO = new OwnerDAO(getConnectionSource(), Owner.class);
        }
        return ownerDAO;
    }

    public AppDAO getAppDAO() throws SQLException{
        if(appDAO == null){
            appDAO = new AppDAO(getConnectionSource(), App.class);
        }
        return appDAO;
    }
}
