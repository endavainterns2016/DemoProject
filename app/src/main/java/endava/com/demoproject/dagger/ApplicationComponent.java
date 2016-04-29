package endava.com.demoproject.dagger;

import javax.inject.Singleton;

import dagger.Component;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.asyncLoader.RepoLoadingTask;
import endava.com.demoproject.events.RefreshRepoListEvent;
import endava.com.demoproject.fragments.LoginFragment;
import endava.com.demoproject.fragments.RepoCommitsFragment;
import endava.com.demoproject.fragments.RepoDetailsFragment;
import endava.com.demoproject.fragments.ReposListFragment;
import endava.com.demoproject.fragments.ReposSyncFragment;
import endava.com.demoproject.fragments.SplashFragment;
import endava.com.demoproject.helpers.LoginHelper;
import endava.com.demoproject.helpers.SharedPreferencesHelper;
import endava.com.demoproject.others.LoginCommand;
import endava.com.demoproject.others.NetworkStateChangedReceiver;
import endava.com.demoproject.services.RefreshReposListService;

@Component(modules = {ApplicationModule.class})
@Singleton
public interface ApplicationComponent {

    //HELPERS
    void inject(LoginHelper loginHelper);

    void inject(SharedPreferencesHelper sharedPreferencesHelper);

    //FRAGMENTS
    void inject(RepoCommitsFragment repoCommitsFragment);

    void inject(ReposListFragment reposListFragment);

    void inject(LoginFragment loginFragment);

    void inject(RepoDetailsFragment repoDetailsFragment);

    void inject(ReposSyncFragment reposSyncFragment);

    void inject(SplashFragment splashFragment);



    void inject(LoginCommand loginCommand);

    void inject(RefreshRepoListEvent refreshRepoListEvent);

    void inject(MainActivity mainActivity);

    void inject(RepoLoadingTask repoLoadingTask);

    void inject(RefreshReposListService refreshReposListService);

    void inject(NetworkStateChangedReceiver networkStateChangedReceiver);
}
