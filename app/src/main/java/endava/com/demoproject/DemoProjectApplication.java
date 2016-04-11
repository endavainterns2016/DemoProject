package endava.com.demoproject;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import endava.com.demoproject.db.HelperProvider;

public class DemoProjectApplication extends Application {

    private static Application app;

    public static Context getApplication() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Log.d("helper", "start DemoProjectApplication");
        HelperProvider.setHelper(this);
    }
}
