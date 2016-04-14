package endava.com.demoproject.db.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import endava.com.demoproject.model.App;
import endava.com.demoproject.model.Repo;

public class RepoDAO extends BaseDaoImpl<Repo,Integer> {

    public RepoDAO(ConnectionSource connectionSource,
                   Class<Repo> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
