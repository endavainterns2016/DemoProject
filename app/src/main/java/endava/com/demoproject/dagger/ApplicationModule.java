package endava.com.demoproject.dagger;

import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.helpers.LoginHelper;
import endava.com.demoproject.helpers.ResourcesHelper;
import endava.com.demoproject.helpers.SharedPreferencesHelper;
import endava.com.demoproject.presenter.RepoCommitsPresenter;
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
    LoginHelper provideLoginHelper(){
        return new LoginHelper();
    }

    @Provides
    @Singleton
    ResourcesHelper provideResourcesHelper(){
        return new ResourcesHelper();
    }

    @Provides
    @Singleton
    SharedPreferencesHelper provideSharedPreferencesHelper(){
        return new SharedPreferencesHelper();
    }

    @Provides
    @Singleton
    UserAPI provideUserAPI(){
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(UserAPI.class);
    }

    @Provides
    RepoCommitsPresenter provideRepoCommitsPresenter(DbHelper dbHelper, UserAPI userAPI){
        return new RepoCommitsPresenter(dbHelper, userAPI);
    }

}
