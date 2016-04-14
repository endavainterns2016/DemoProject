package endava.com.demoproject.db.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import endava.com.demoproject.model.Owner;

public class OwnerDAO extends BaseDaoImpl<Owner, Integer> {

    public OwnerDAO(ConnectionSource connectionSource,
                    Class<Owner> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
