package endava.com.demoproject.helpers;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.inject.Inject;

import endava.com.demoproject.DemoProjectApplication;
import endava.com.demoproject.R;
import endava.com.demoproject.SaveUserToDB;
import endava.com.demoproject.model.Avatar;
import endava.com.demoproject.model.User;
import endava.com.demoproject.retrofit.ServiceFactory;
import endava.com.demoproject.services.SaveUserToDBService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginHelper implements Callback<List<User>> {
    public static LoginHelper helper;
    @Inject
    Context context;
    private String credentials;
    private Callback<Avatar> avatarCallback;
    private User user;
    private LoginHelperResponse helperResponse;
    private Intent intent;

    public static LoginHelper getInstance(LoginHelperResponse loginHelperResponse) {
        if (helper == null) {
            helper = new LoginHelper();
        }
        helper.prepareHelper(loginHelperResponse); // here we refresh reference for our response,presenter and all other stuff
        return helper;
    }

    public void prepareHelper(LoginHelperResponse helperResponse) {
        DemoProjectApplication.getApplicationComponent().inject(this);
        this.helperResponse = helperResponse;
    }

    public void doLogin(String username, String password) {
        try {
            credentials = android.util.Base64.encodeToString(
                    (username + ":" + password).getBytes("UTF-8"),
                    android.util.Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding ", e.toString());
        }

        ServiceFactory.getInstance().auth("Basic " + credentials).enqueue(this);
    }

    @Override
    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
        if (response.body() != null) {
            helperResponse.registerObserver();
            user = response.body().get(0);
            user.setHashedCredentials(credentials);
            initAvatarCallBack();
            ServiceFactory.getInstance().getUserAvatar("Basic " + user.getHashedCredentials()).enqueue(avatarCallback);
        } else {
            helperResponse.setError(context.getString(R.string.credentials_error));
        }
    }

    @Override
    public void onFailure(Call<List<User>> call, Throwable t) {
        helperResponse.setConnectionError();
    }


    public void stopService() {
        context.stopService(intent);
    }

    public void initAvatarCallBack() {
        avatarCallback = new Callback<Avatar>() {
            @Override
            public void onResponse(Call<Avatar> call, Response<Avatar> response) {
                user.setAvatarUrl(response.body().getAvatarUrl());
                SaveUserToDB command = new SaveUserToDB(user);
                intent = new Intent(context, SaveUserToDBService.class);
                intent.putExtra("command", command);
                context.startService(intent);
            }

            @Override
            public void onFailure(Call<Avatar> call, Throwable t) {
                helperResponse.setConnectionError();
            }
        };
    }
}
