package endava.com.demoproject.db.dao;

import endava.com.demoproject.model.App;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class AppDAO extends BaseDaoImpl<App,Integer> {

    public AppDAO(ConnectionSource connectionSource,
                  Class<App> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
