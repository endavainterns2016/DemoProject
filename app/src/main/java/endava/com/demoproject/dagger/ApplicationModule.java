package endava.com.demoproject.dagger;

import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import endava.com.demoproject.cacheableObserver.Subject;
import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.helpers.LoginHelper;
import endava.com.demoproject.helpers.SharedPreferencesHelper;
import endava.com.demoproject.presenter.LoginPresenter;
import endava.com.demoproject.presenter.MainPresenter;
import endava.com.demoproject.presenter.RepoCommitsPresenter;
import endava.com.demoproject.presenter.RepoListDetailPresenter;
import endava.com.demoproject.presenter.ReposListPresenter;
import endava.com.demoproject.presenter.ReposSyncPresenter;
import endava.com.demoproject.presenter.SplashPresenter;
import endava.com.demoproject.retrofit.UserAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {

    private Context appContext;

    public ApplicationModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return appContext;
    }


    //HELPERS
    @Provides
    @Singleton
    Resources provideResources() {
        return appContext.getResources();
    }

    @Provides
    @Singleton
    DbHelper provideDbHelper(){
        return new DbHelper();
    }

    @Provides
    @Singleton
    LoginHelper provideLoginHelper(Context context){
        return new LoginHelper(context);
    }


    @Provides
    @Singleton
    SharedPreferencesHelper provideSharedPreferencesHelper(Context context){
        return new SharedPreferencesHelper(context);
    }

    @Provides
    @Singleton
    Subject provideSubject(){
        return new Subject();
    }

    @Provides
    @Singleton
    UserAPI provideUserAPI(){
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(UserAPI.class);
    }


    //PRESENTERS
    @Provides
    RepoCommitsPresenter provideRepoCommitsPresenter(DbHelper dbHelper, UserAPI userAPI){
        return new RepoCommitsPresenter(dbHelper, userAPI);
    }

    @Provides
    ReposListPresenter provideReposListPresenter(SharedPreferencesHelper sharedPreferencesHelper, DbHelper dbHelper, UserAPI userAPI, Subject subject, Resources resources){
        return new ReposListPresenter(sharedPreferencesHelper, dbHelper, userAPI, subject, resources);
    }

    @Provides
    LoginPresenter provideLoginPresenter (Resources resources, SharedPreferencesHelper sharedPrefHelper, LoginHelper loginHelper, Subject subject) {
        return new LoginPresenter(resources, sharedPrefHelper, loginHelper, subject);
    }

    @Provides
    MainPresenter provideMainPresenter(DbHelper dbHelper){
        return new MainPresenter(dbHelper);
    }

    @Provides
    RepoListDetailPresenter provideRepoListDetailPresenter(DbHelper dbHelper, UserAPI userAPI){
        return new RepoListDetailPresenter(dbHelper, userAPI);
    }

    @Provides
    ReposSyncPresenter provideReposSyncPresenter(UserAPI userAPI, DbHelper dbHelper){
        return new ReposSyncPresenter(userAPI, dbHelper);
    }

    @Provides
    SplashPresenter provideSplashPresenter(DbHelper dbHelper){
        return new SplashPresenter(dbHelper);
    }
}
