package endava.com.demoproject.presenter;

import javax.inject.Inject;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.view.SplashView;

public class SplashPresenter extends BasePresenter<SplashView> {

    private DbHelper dbHelper;

    @Inject
    public SplashPresenter(DbHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    public void onSplashFinished() {
        getMvpView().startNextActivity(dbHelper.getUser() == null);
    }
}
