package endava.com.demoproject.presenter;

import android.util.Log;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.User;
import endava.com.demoproject.view.MainView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lbuzmacov on 20-04-16.
 */
public class MainPresenter extends BasePresenter<MainView> {
    private MainView mainView;
    private Subscription subscription;
    private User user;

    @Override
    public void attachView(MainView mvpView) {
        super.attachView(mvpView);
        mainView = mvpView;
        mainView.initView();
        loadUser();
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null && !subscription.isUnsubscribed()) {
            Log.d("Mainrxjava", "unsubscribe");
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
        Log.d("Mainrxjava", "loadUser");
        subscription = Observable.just(getUser()).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {
                Log.d("Mainrxjava", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(User userResult) {
                Log.d("Mainrxjava", "onNext");
                user = userResult;
                setNavViewDetails(userResult);

            }
        });
    }

    public User getUser() {
        Log.d("Mainrxjava", "getUser");
        return DbHelper.getInstance().getUser();
    }

    public void setNavViewDetails(User user) {
        mainView.setNavViewDetails(user.getUserName(), user.getAvatarUrl());
    }

    public void logOut() {
        mainView.logOutDialogShow();
    }

    public void onLogOut() {
        DbHelper.getInstance().deleteUser(user);
        mainView.navigateToLoginView();
    }
}
