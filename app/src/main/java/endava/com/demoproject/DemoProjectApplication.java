package endava.com.demoproject;

import android.app.Application;
import android.util.Log;

import endava.com.demoproject.dagger.ApplicationComponent;
import endava.com.demoproject.dagger.ApplicationModule;
import endava.com.demoproject.dagger.DaggerApplicationComponent;
import endava.com.demoproject.db.HelperProvider;

/**
 * Created by lbuzmacov on 06-04-16.
 */
public class DemoProjectApplication extends Application {

    private static ApplicationComponent applicationComponent;
    @Override
    public void onCreate() {
        applicationComponent = buildComponent();
        super.onCreate();
        Log.d("helper", "start DemoProjectApplication");
        HelperProvider.setHelper(this);
    }

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    protected ApplicationComponent buildComponent(){
        return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }
}
