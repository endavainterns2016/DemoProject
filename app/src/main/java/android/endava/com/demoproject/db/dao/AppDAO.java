package android.endava.com.demoproject.db.dao;

import android.endava.com.demoproject.model.App;
import android.endava.com.demoproject.model.User;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

public class AppDAO extends BaseDaoImpl<App,Integer> {

    public AppDAO(ConnectionSource connectionSource,
                  Class<App> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
