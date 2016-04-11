package endava.com.demoproject.helpers;


import android.content.Context;
import android.content.res.Resources;

import javax.inject.Inject;

public class ResourceHelper {
    @Inject
    Context context;
    private static Resources resources;

    public static Resources getResources() {
        if (resources == null) {
         //   resources = context.getResources();
            return resources;
        } else {
            return resources;
        }
    }
}
