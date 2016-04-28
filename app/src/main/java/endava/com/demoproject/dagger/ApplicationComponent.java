package endava.com.demoproject.dagger;

import javax.inject.Singleton;

import dagger.Component;
import endava.com.demoproject.fragments.RepoCommitsFragment;
import endava.com.demoproject.helpers.LoginHelper;
import endava.com.demoproject.helpers.ResourcesHelper;
import endava.com.demoproject.helpers.SharedPreferencesHelper;

@Component(modules = {ApplicationModule.class})
@Singleton
public interface ApplicationComponent {
    void inject(LoginHelper loginHelper);

    void inject(ResourcesHelper resourcesHelper);

    void inject(SharedPreferencesHelper sharedPreferencesHelper);

    void inject(RepoCommitsFragment repoCommitsFragment);

}
