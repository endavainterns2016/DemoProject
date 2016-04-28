package endava.com.demoproject.helpers;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import endava.com.demoproject.others.LoginCommand;
import endava.com.demoproject.services.LoginService;

public class LoginHelper {
//    public static LoginHelper helper;
    private Context context;
    private String credentials;

    @Inject
    public LoginHelper(Context context) {
        this.context = context;
    }

//    public static LoginHelper getInstance() {
//        if (helper == null) {
//            helper = new LoginHelper();
//        }
//        helper.injectContext();
//        return helper;
//    }
//
//    public void injectContext() {
//        DemoProjectApplication.getApplicationComponent().inject(this);
//    }

    public void doLogin(String username, String password) {
        try {
            credentials = android.util.Base64.encodeToString(
                    (username + ":" + password).getBytes("UTF-8"),
                    android.util.Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding ", e.toString());
        }
        LoginCommand command = new LoginCommand("Basic " + credentials, username);
        Intent intent = new Intent(context, LoginService.class);
        intent.putExtra("command", command);
        context.startService(intent);
    }
}
