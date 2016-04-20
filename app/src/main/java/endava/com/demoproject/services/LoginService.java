package endava.com.demoproject.services;

import android.app.IntentService;
import android.content.Intent;

import endava.com.demoproject.others.LoginCommand;


public class LoginService extends IntentService{

    public LoginService() {
        super("Login");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        LoginCommand command = intent.getParcelableExtra("command");
        command.execute();
    }
}

