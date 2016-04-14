package endava.com.demoproject.presenter;

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

public class ReposListPresenter extends BasePresenter<ReposListView> implements Observer, Callback<List<Repo>> {

    private User user = DbHelper.getInstance().getUser();
    private ReposListView reposListView;
    private Subject subject = Subject.newInstance();

    @Override
    public void attachView(ReposListView mvpView) {
        super.attachView(mvpView);
        reposListView = mvpView;
        subject.registerObserver(this);
    }

    @Override
    public void initView() {
        super.initView();
        reposListView.initView();
    }

    public void populateView() {
        reposListView.showProgress();
        ServiceFactory.getInstance().getReposList("Basic " + user.getHashedCredentials()).enqueue(this);
    }


    @Override
    public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
        if (response.body() != null) {
            DbHelper.getInstance().createRepos(response.body());
            reposListView.populateList(response.body());
        } else {
            reposListView.handleError();
        }
    }

    @Override
    public void onFailure(Call<List<Repo>> call, Throwable t) {
        reposListView.handleOnRequestFailure();
    }

    @Override
    public void detachView() {
        super.detachView();
        subject.unregisterObservers(this);
    }

    @Override
    public void onEvent(Event e) {
        populateView();
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
