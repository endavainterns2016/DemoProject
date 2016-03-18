package android.endava.com.demoproject.db;

import android.endava.com.demoproject.model.User;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;


public class UserDAO extends BaseDaoImpl<User,Integer>{

    protected UserDAO(ConnectionSource connectionSource,
                      Class<User> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<User> getAllUsers() throws SQLException{
        return this.queryForAll();
    }
}

