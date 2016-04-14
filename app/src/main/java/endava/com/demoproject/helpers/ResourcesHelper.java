package endava.com.demoproject.helpers;


import android.content.res.Resources;

import javax.inject.Inject;

import endava.com.demoproject.DemoProjectApplication;

public class ResourcesHelper {
    private static ResourcesHelper resourcesHelper;
    @Inject
    Resources resources;

    public static ResourcesHelper getInstance() {
        if (resourcesHelper == null) {
            resourcesHelper = new ResourcesHelper();
        }
        resourcesHelper.injectResources();
        return resourcesHelper;
    }

    public Resources provideResources() {
        return resourcesHelper.resources;
    }

    private void injectResources() {
        DemoProjectApplication.getApplicationComponent().inject(this);
    }
}
