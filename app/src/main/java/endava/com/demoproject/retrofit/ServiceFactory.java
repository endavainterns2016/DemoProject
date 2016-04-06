package endava.com.demoproject.retrofit;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {
    private static HashMap<String,UserAPI> serviceMap = new HashMap<>();
    public static final String DEMO_PROJECT_API_TAG = "UserAPI";

    private static final String BASE_URL = "https://api.github.com";

    public static UserAPI getInstance() {
        if (!serviceMap.containsKey(DEMO_PROJECT_API_TAG)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UserAPI userAPI = retrofit.create(UserAPI.class);
            serviceMap.put(DEMO_PROJECT_API_TAG, userAPI);
            return userAPI;
        } else {
            return serviceMap.get(DEMO_PROJECT_API_TAG);
        }
    }
}
