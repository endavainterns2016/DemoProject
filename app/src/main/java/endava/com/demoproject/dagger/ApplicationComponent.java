package endava.com.demoproject.dagger;

import javax.inject.Singleton;

import dagger.Component;
import endava.com.demoproject.helpers.LoginHelper;
import endava.com.demoproject.helpers.SharedPreferencesHelper;
import endava.com.demoproject.presenter.LoginPresenter;

@Component(modules = {ApplicationModule.class})
@Singleton
public interface ApplicationComponent {
    void inject(LoginHelper loginHelper);
    void inject(LoginPresenter loginPresenter);
    void inject(SharedPreferencesHelper sharedPreferencesHelper);

}
