package endava.com.demoproject.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import endava.com.demoproject.R;
import endava.com.demoproject.cacheableObserver.Event;
import endava.com.demoproject.cacheableObserver.EventContext;
import endava.com.demoproject.cacheableObserver.Observer;
import endava.com.demoproject.cacheableObserver.Subject;
import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.helpers.ResourcesHelper;
import endava.com.demoproject.helpers.SharedPreferencesHelper;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.model.User;
import endava.com.demoproject.retrofit.UserAPI;
import endava.com.demoproject.view.ReposListView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReposListPresenter extends BasePresenter<ReposListView> implements Observer, Callback<List<Repo>>, rx.Observer<User> {

    private User user;
    private ReposListView reposListView;
    private Subject subject;
    private Subscription subscription;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private DbHelper dbHelper;
    private UserAPI userAPI;

    public ReposListPresenter(SharedPreferencesHelper sharedPreferencesHelper, DbHelper dbHelper, UserAPI userAPI, Subject subject){
        this.sharedPreferencesHelper = sharedPreferencesHelper;
        this.dbHelper = dbHelper;
        this.userAPI = userAPI;
        this.subject = subject;
    }

    @Override
    public void attachView(ReposListView mvpView) {
        super.attachView(mvpView);
        Log.d("repolistpresenter_init", "onCompleted");
        reposListView = mvpView;
        reposListView.initView();
        populateView();
    }


    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public Subscription getSubscription() {
        return subscription;
    }

    @Override
    public void onResume() {
        subject.registerObserver(this);
    }

    @Override
    public void onPause() {
        subject.unregisterObservers(this);
    }

    public void loadUser(){
        Log.d("Rxjava", "loadUser");
        subscription = Observable.just(getUser()).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(this);
    }

    @Override
    public void onCompleted() {
        Log.d("Rxjava", "onCompleted");
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(User userResult) {
        Log.d("Rxjava", "onNext");
        user = userResult;
        handleReposListRequest();
    }

    public void populateView() {
        Log.d("Rxjava", "populateView");
        reposListView.showProgress();
        if (user == null) {
            loadUser();
        } else {
            handleReposListRequest();
        }
    }

    public void onRefresh() {
        populateView();
    }

    public void handleReposListRequest() {
        Log.d("Rxjava", "handleReposListRequest");
        userAPI.getReposList(user.getHashedCredentials()).enqueue(this);
    }


    @Override
    public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
        if (response.body() != null) {
            dbHelper.createRepos(response.body());
            reposListView.populateList(response.body());
            reposListView.hideProgress();
        } else {
            reposListView.hideProgress();
            reposListView.showError();
        }
    }


    @Override
    public void onFailure(Call<List<Repo>> call, Throwable t) {
        reposListView.hideProgress();
        reposListView.showError();
    }

    public User getUser() {
        Log.d("Rxjava", "getUser");
        return dbHelper.getUser();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLoadedUser() {
        return user;
    }

    @Override
    public void onEvent(Event e) {
        populateView();
        Log.d("NetworkChangedReceiver", "onEvent");
    }

    @Override
    public List<EventContext> getObserverKeys() {
        EventContext refreshEvent = new EventContext(ResourcesHelper.getInstance().provideResources().getString(R.string.refreshList), null);
        List<EventContext> list = new ArrayList<>();
        list.add(refreshEvent);
        return list;
    }

    @Override
    public boolean isMainObserverForKey(EventContext key) {
        return true;
    }

    public boolean getAutoSyncEnabled() {
        return sharedPreferencesHelper.getAutoSyncStatus();
    }

    public int getAutoSyncInterval() {
        return sharedPreferencesHelper.getAutoSyncInterval();
    }
}
