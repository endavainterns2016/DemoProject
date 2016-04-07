package endava.com.demoproject.dagger;

import javax.inject.Singleton;

import dagger.Component;
import endava.com.demoproject.presenter.SplashPresenter;

@Component(modules = {ApplicationModule.class})
@Singleton
public interface ApplicationComponent {
    void inject(SplashPresenter splashPresenter);
}
