package android.endava.com.demoproject.retrofit;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {
    private static HashMap<String,DemoProjectAPI> serviceMap = new HashMap<>();
    public static final String DEMO_PROJECT_API_TAG = "DemoProjectAPI";

    private static final String BASE_URL = "https://api.github.com";

    public static DemoProjectAPI getInstance() {
        if (!serviceMap.containsKey(DEMO_PROJECT_API_TAG)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DemoProjectAPI demoProjectAPI = retrofit.create(DemoProjectAPI.class);
            serviceMap.put(DEMO_PROJECT_API_TAG,demoProjectAPI);
            return demoProjectAPI;
        } else {
            return serviceMap.get(DEMO_PROJECT_API_TAG);
        }
    }
}
