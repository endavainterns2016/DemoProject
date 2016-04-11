package endava.com.demoproject.helpers;


import android.content.res.Resources;

import endava.com.demoproject.DemoProjectApplication;

public class ResourceHelper {
    private static Resources resources;

    public static Resources getResources() {
        if (resources == null) {
            resources = DemoProjectApplication.getApplication().getResources();
            return resources;
        } else {
            return resources;
        }
    }
}
