package android.endava.com.demoproject.retrofit;

import android.endava.com.demoproject.model.Repo;
import android.endava.com.demoproject.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface UserAPI {

    @Headers("Accept: application/vnd.github.v3+json")

    @GET("/authorizations")
    Call<List<User>> auth(@Header("authorization") String username);

    @GET("/user/repos")
    Call<List<Repo>> getReposList(@Header("authorization") String token);
}
