package endava.com.demoproject.db.dao;

import endava.com.demoproject.model.User;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;


public class UserDAO extends BaseDaoImpl<User,Integer>{

    public UserDAO(ConnectionSource connectionSource,
                   Class<User> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}

