package endava.com.demoproject.presenter;

import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import endava.com.demoproject.DemoProjectApplication;
import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.retrofit.ServiceFactory;
import endava.com.demoproject.view.RepoDetailsView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoListDetailPresenter extends BasePresenter<RepoDetailsView> implements Callback<Repo> {
    @Inject
    Context context;
    private RepoDetailsView repoDetailsView;
    private DbHelper dbHelper;
    private Integer repoId;

    public RepoListDetailPresenter(RepoDetailsView repoDetailsView, Integer repoId) {
        this.repoDetailsView = repoDetailsView;
        this.repoId = repoId;
        DemoProjectApplication.getApplicationComponent().inject(this);
        dbHelper = DbHelper.getInstance();
    }

    public void updateRepo(Repo repo) {
        ServiceFactory.getInstance().updateRepo("Basic " + dbHelper.getUser().getHashedCredentials(), repo.getOwner(), repo.getName()).enqueue(this);
    }

    @Override
    public void attachView(RepoDetailsView mvpView) {
        super.attachView(mvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
        repoDetailsView = null;
    }

    public void onDestroy() {
        detachView();
    }

    @Override
    public void onResponse(Call<Repo> call, Response<Repo> response) {
        Log.d("","");
    }

    @Override
    public void onFailure(Call<Repo> call, Throwable t) {
        Log.d("","");

    }
}
