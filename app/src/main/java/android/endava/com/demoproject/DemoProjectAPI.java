package android.endava.com.demoproject;

import android.endava.com.demoproject.Model.AuthResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by agrebennicov on 3/15/2016.
 */
public interface DemoProjectAPI {

    String BASE_URL = "https://api.github.com";

    @Headers("Accept: application/vnd.github.v3+json")

    @GET("/authorizations")
    Call<List<AuthResponse>> auth(@Header("authorization") String username);

    class Factory {
        private static DemoProjectAPI service;

        public static DemoProjectAPI getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                service = retrofit.create(DemoProjectAPI.class);
                return service;
            } else {
                return service;
            }
        }
    }
}
