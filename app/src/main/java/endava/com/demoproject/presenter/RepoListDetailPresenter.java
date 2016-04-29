package endava.com.demoproject.presenter;

import javax.inject.Inject;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.retrofit.UserAPI;
import endava.com.demoproject.view.RepoDetailsView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoListDetailPresenter extends BasePresenter<RepoDetailsView> implements Callback<Repo> {
    private RepoDetailsView repoDetailsView;
    private DbHelper dbHelper;
    private Repo repo;
    private UserAPI userAPI;

    @Inject
    public RepoListDetailPresenter(DbHelper dbHelper,UserAPI userAPI) {
        this.dbHelper = dbHelper;
        this.userAPI = userAPI;
    }

    public void updateRepo() {
        userAPI.updateRepo(dbHelper.getUser().getHashedCredentials(), repo.getOwner().getLogin(), repo.getName()).enqueue(this);
    }

    public void setRepo(Repo repo) {
        this.repo = repo;
    }

    @Override
    public void attachView(RepoDetailsView mvpView) {
        super.attachView(mvpView);
        repoDetailsView = mvpView;
    }

    public void populateView(Repo repo) {
        repoDetailsView.populateView(repo);
    }

    public void initView() {
        repoDetailsView.initView();
    }

    public void updateRepo(Repo repo) {
        dbHelper.updateRepo(repo);
    }

    @Override
    public void detachView() {
        super.detachView();
        repoDetailsView = null;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    public void onDestroy() {
        detachView();
    }

    @Override
    public void onResponse(Call<Repo> call, Response<Repo> response) {
        if (response != null) {
            repoDetailsView.populateView(response.body());
            updateRepo(response.body());
        }
    }

    @Override
    public void onFailure(Call<Repo> call, Throwable t) {
        repoDetailsView.networkError();
    }
}
