package endava.com.demoproject.presenter;

import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.view.SplashView;

public class SplashPresenter extends BasePresenter<SplashView> {

    @Override
    public void attachView(SplashView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public boolean userIsAvailable() {
        return DbHelper.getInstance().getUser() != null;
    }

}
