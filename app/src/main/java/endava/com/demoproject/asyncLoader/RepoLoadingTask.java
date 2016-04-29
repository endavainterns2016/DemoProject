package endava.com.demoproject.asyncLoader;

import android.content.Context;

import javax.inject.Inject;

import endava.com.demoproject.DemoProjectApplication;
import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.Repo;

public class RepoLoadingTask extends DemoLoader<Repo> {
    @Inject
    public DbHelper dbHelper;
    private Integer id;

    public RepoLoadingTask(Context context, Integer id) {
        super(context);
        DemoProjectApplication.getApplicationComponent().inject(this);
        this.id = id;
    }

    @Override
    public Repo loadInBackground() {
        return dbHelper.getRepoById(id);
    }
}
