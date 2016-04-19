package endava.com.demoproject.presenter;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.retrofit.ServiceFactory;
import endava.com.demoproject.view.RepoDetailsView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoListDetailPresenter extends BasePresenter<RepoDetailsView> implements Callback<Repo> {
    private RepoDetailsView repoDetailsView;
    private DbHelper dbHelper;
    private Repo repo;

    public RepoListDetailPresenter(RepoDetailsView repoDetailsView) {
        this.repoDetailsView = repoDetailsView;
        dbHelper = DbHelper.getInstance();
    }

    public void updateRepo() {
        ServiceFactory.getInstance().updateRepo("Basic " + dbHelper.getUser().getHashedCredentials(), repo.getOwner().getLogin(), repo.getName()).enqueue(this);
    }

    public void setRepo(Repo repo) {
        this.repo = repo;
    }

    @Override
    public void attachView(RepoDetailsView mvpView) {
        super.attachView(mvpView);
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
        repoDetailsView.populateView(response.body());
        updateRepo(response.body());
    }

    @Override
    public void onFailure(Call<Repo> call, Throwable t) {
        repoDetailsView.networkError();
    }
}
