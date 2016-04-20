package endava.com.demoproject.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.CommitModel;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.retrofit.ServiceFactory;
import endava.com.demoproject.view.RepoCommitsView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lbuzmacov on 20-04-16.
 */
public class RepoCommitsPresenter extends BasePresenter<RepoCommitsView> implements Callback<List<CommitModel>> {

    private ArrayList<CommitModel> commitsList = new ArrayList<>();
    private RepoCommitsView repoCommitsView;
    private Subscription subscription;

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
        subscription = Observable.just(getRepo()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Repo>() {
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
                repoCommitsView.setToolbarTitle(repo.getName());
                handleCommitsListRequest(repo);
            }
        });
    }

    private Repo getRepo() {
      return DbHelper.getInstance().getRepoById(repoCommitsView.getRepoID());
    }

    public void handleCommitsListRequest(Repo repo) {
        Log.d("Commitsrxjava", "handleReposListRequest");
        ServiceFactory.getInstance().getCommitsList(DbHelper.getInstance().getUser().getHashedCredentials(), repo.getOwner().getLogin(), repo.getName()).enqueue(this);
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
