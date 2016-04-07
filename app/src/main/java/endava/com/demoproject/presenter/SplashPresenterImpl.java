package endava.com.demoproject.presenter;

import android.content.Context;

import javax.inject.Inject;

import endava.com.demoproject.DemoProjectApplication;
import endava.com.demoproject.view.SplashView;

/**
 * Created by lbuzmacov on 06-04-16.
 */
public class SplashPresenterImpl implements SplashPresenter {

    @Inject
    Context context;

    private SplashView splashView;

    public SplashPresenterImpl(SplashView splashView) {
        this.splashView = splashView;
    }

    @Override
    public void onCreate() {
        DemoProjectApplication.getApplicationComponent().inject(this);
    }
}
