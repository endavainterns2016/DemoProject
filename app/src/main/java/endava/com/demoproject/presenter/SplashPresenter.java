package endava.com.demoproject.presenter;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.view.SplashView;

public class SplashPresenter extends BasePresenter<SplashView> {

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    public void onSplashFinished() {
        getMvpView().startNextActivity(DbHelper.getInstance().getUser() == null);
    }
}
