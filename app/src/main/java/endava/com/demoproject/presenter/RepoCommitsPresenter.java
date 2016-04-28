package endava.com.demoproject.presenter;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.CommitModel;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.retrofit.UserAPI;
import endava.com.demoproject.view.RepoCommitsView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lbuzmacov on 20-04-16.
 */
public class RepoCommitsPresenter extends BasePresenter<RepoCommitsView> implements Callback<List<CommitModel>>, Observer<Repo> {

    private RepoCommitsView repoCommitsView;
    private Subscription subscription;
    private DbHelper dbHelper;
    private UserAPI userAPI;

    @Inject
    public RepoCommitsPresenter(DbHelper dbHelper, UserAPI userAPI){
        this.dbHelper = dbHelper;
        this.userAPI = userAPI;
    }

    @Override
    public void attachView(RepoCommitsView mvpView) {
        super.attachView(mvpView);
        repoCommitsView = mvpView;
        repoCommitsView.initView();
        populateView();
    }


    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onResume() {

    }

    public Subscription getSubscription() {
        return subscription;
    }

    @Override
    public void onPause() {

    }


    public void populateView() {
        Log.d("Commitsrxjava", "populateView");
        repoCommitsView.showProgress();
            loadRepo();
    }

    public void loadRepo(){
        Log.d("Commitsrxjava", "loadUser");
        subscription = Observable.just(getRepo()).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(this);
    }

    @Override
    public void onCompleted() {
        Log.d("Commitsrxjava", "onCompleted");
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Repo repo) {
        Log.d("Commitsrxjava", "onNext");
        repoCommitsView.setRepoName(repo.getName());
        handleCommitsListRequest(repo);
    }

    private Repo getRepo() {
      return dbHelper.getRepoById(repoCommitsView.getRepoID());
    }

    public void handleCommitsListRequest(Repo repo) {
        Log.d("Commitsrxjava", "handleReposListRequest");
        userAPI.getCommitsList(dbHelper.getUser().getHashedCredentials(), repo.getOwner().getLogin(), repo.getName()).enqueue(this);
    }


    @Override
    public void onResponse(Call<List<CommitModel>> call, Response<List<CommitModel>> response) {
        if (response.body() != null) {
            repoCommitsView.populateList(response.body());
            repoCommitsView.hideProgress();
        } else {
            repoCommitsView.hideProgress();
            repoCommitsView.showError();
        }
    }

    @Override
    public void onFailure(Call<List<CommitModel>> call, Throwable t) {
        repoCommitsView.hideProgress();
        repoCommitsView.showError();

    }
}
