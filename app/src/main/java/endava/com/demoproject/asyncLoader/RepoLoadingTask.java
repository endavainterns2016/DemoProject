package endava.com.demoproject.asyncLoader;

import android.content.Context;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.Repo;

public class RepoLoadingTask extends DemoLoader<Repo> {
    private DbHelper dbHelper = DbHelper.getInstance();
    private Integer id;

    public RepoLoadingTask(Context context, Integer id) {
        super(context);
        this.id = id;
    }

    @Override
    public Repo loadInBackground() {
        return dbHelper.getRepoById(id);
    }
}
