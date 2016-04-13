package endava.com.demoproject.retrofit;

import java.util.List;

import endava.com.demoproject.model.Avatar;
import endava.com.demoproject.model.Repo;
import endava.com.demoproject.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface UserAPI {

    @Headers("Accept: application/vnd.github.v3+json")

    @GET("/authorizations")
    Call<List<User>> auth(@Header("authorization") String username);

    @GET("/user/repos")
    Call<List<Repo>> getReposList(@Header("authorization") String token);

    @GET("/user")
    Call<Avatar> getUserAvatar(@Header("authorization") String token);


    @GET("/user/{owner}/{repo}")
    Call<Repo> updateRepo(@Header("authorization") String token, @Path("owner") String owner, @Path("repo") String repo);
}
