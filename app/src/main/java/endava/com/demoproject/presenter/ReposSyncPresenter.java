package endava.com.demoproject.presenter;

import android.util.Log;

import java.util.List;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.model.User;
import endava.com.demoproject.retrofit.ServiceFactory;
import endava.com.demoproject.retrofit.UserAPI;
import endava.com.demoproject.view.ReposSyncView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReposSyncPresenter extends BasePresenter<ReposSyncView> implements Callback<List<Repo>> {

    private User user;
    private ReposSyncView reposSyncView;
    private Subscription subscription;
    private UserAPI userAPI;
    private DbHelper dbHelper;

    public ReposSyncPresenter(UserAPI userAPI,DbHelper dbHelper) {
        this.userAPI = userAPI;
        this.dbHelper = dbHelper;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    @Override
    public void attachView(ReposSyncView mvpView) {
        super.attachView(mvpView);
        reposSyncView = mvpView;
        reposSyncView.initView();
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

    public void loadUser() {
        Log.d("rxjava", "loadUser");
        subscription = Observable.just(getUser()).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {
                Log.d("rxjava", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(User userResult) {
                Log.d("rxjava", "onNext");
                user = userResult;
                handleReposListRequest(userResult);
            }
        });
    }

    public void populateView() {
        Log.d("rxjava", "populateView");
        reposSyncView.showProgress();
        if (user == null) {
            loadUser();
        } else {
            handleReposListRequest(user);
        }
    }

    public void onRefresh() {
        populateView();
    }

    public void handleReposListRequest(User user) {
        Log.d("rxjava", "handleReposListRequest");
        userAPI.getReposList(user.getHashedCredentials()).enqueue(this);
    }


    @Override
    public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
        if (response.body() != null) {
            dbHelper.createRepos(response.body());
            reposSyncView.populateList(response.body());
            reposSyncView.hideProgress();
        } else {
            reposSyncView.hideProgress();
            reposSyncView.showError();
        }
    }


    @Override
    public void onFailure(Call<List<Repo>> call, Throwable t) {
        reposSyncView.hideProgress();
        reposSyncView.showError();
    }

    public User getUser() {
        Log.d("rxjava", "getUser");
        return dbHelper.getUser();
    }
}
