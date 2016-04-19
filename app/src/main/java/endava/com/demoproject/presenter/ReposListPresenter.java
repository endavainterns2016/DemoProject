package endava.com.demoproject.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import endava.com.demoproject.cacheableObserver.Event;
import endava.com.demoproject.cacheableObserver.EventContext;
import endava.com.demoproject.cacheableObserver.Observer;
import endava.com.demoproject.cacheableObserver.Subject;
import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.helpers.SharedPreferencesHelper;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.model.User;
import endava.com.demoproject.retrofit.ServiceFactory;
import endava.com.demoproject.view.ReposListView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReposListPresenter extends BasePresenter<ReposListView> implements Observer, Callback<List<Repo>> {

    private User user;
    private ReposListView reposListView;
    private Subject subject = Subject.newInstance();
    private Subscription subscription;

    @Override
    public void attachView(ReposListView mvpView) {
        super.attachView(mvpView);
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

    @Override
    public void onResume() {
        subject.registerObserver(this);
    }

    @Override
    public void onPause() {
        subject.unregisterObservers(this);
    }

    public void loadUser(){
        Log.d("rxjava", "loadUser");
        subscription = Observable.just(getUser()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<User>() {
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
                handleReposListRequest();
            }
        });
    }

    public void populateView() {
        Log.d("rxjava", "populateView");
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
        Log.d("rxjava", "handleReposListRequest");
        ServiceFactory.getInstance().getReposList("Basic " + user.getHashedCredentials()).enqueue(this);
    }


    @Override
    public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
        if (response.body() != null) {
            DbHelper.getInstance().createRepos(response.body());
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
        Log.d("rxjava", "getUser");
        return DbHelper.getInstance().getUser();
    }

    @Override
    public void onEvent(Event e) {
        populateView();
        Log.d("NetworkChangedReceiver", "onEvent");
    }

    @Override
    public List<EventContext> getObserverKeys() {
        EventContext refreshEvent = new EventContext("refreshList", null);
        List<EventContext> list = new ArrayList<>();
        list.add(refreshEvent);
        return list;
    }

    @Override
    public boolean isMainObserverForKey(EventContext key) {
        return true;
    }

    public boolean getAutoSyncEnabled() {
        return SharedPreferencesHelper.getInstance().getAutoSyncStatus();
    }

    public int getAutoSyncInterval() {
        return SharedPreferencesHelper.getInstance().getAutoSyncInterval();
    }
}
