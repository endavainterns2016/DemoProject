package endava.com.demoproject;

import android.app.Application;
import android.util.Log;

import endava.com.demoproject.db.HelperProvider;

public class DemoProjectApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("helper", "start DemoProjectApplication");
        HelperProvider.setHelper(this);
    }
}
