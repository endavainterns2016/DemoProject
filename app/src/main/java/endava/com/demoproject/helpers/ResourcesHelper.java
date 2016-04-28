package endava.com.demoproject.helpers;


import android.content.res.Resources;

import javax.inject.Inject;

public class ResourcesHelper {
//    private static ResourcesHelper resourcesHelper;
    private Resources resources;

//    public static ResourcesHelper getInstance() {
//        if (resourcesHelper == null) {
//            resourcesHelper = new ResourcesHelper();
//        }
//        resourcesHelper.injectResources();
//        return resourcesHelper;
//    }
@Inject
    public ResourcesHelper(Resources resources){
        this.resources = resources;
    }

    public Resources provideResources() {
        return resources;
    }

//    private void injectResources() {
//        DemoProjectApplication.getApplicationComponent().inject(this);
//    }
}
