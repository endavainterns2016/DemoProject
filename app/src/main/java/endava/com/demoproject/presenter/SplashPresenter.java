package endava.com.demoproject.presenter;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.view.SplashView;

public class SplashPresenter extends BasePresenter<SplashView> {

    public boolean userIsAvailable() {
        return DbHelper.getInstance().getUser() != null;
    }

    @Override
    public void initView() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }
}
