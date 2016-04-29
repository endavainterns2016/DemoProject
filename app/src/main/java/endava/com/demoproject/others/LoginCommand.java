package endava.com.demoproject.others;


import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import javax.inject.Inject;

import endava.com.demoproject.DemoProjectApplication;
import endava.com.demoproject.R;
import endava.com.demoproject.cacheableObserver.Event;
import endava.com.demoproject.cacheableObserver.EventContext;
import endava.com.demoproject.cacheableObserver.Subject;
import endava.com.demoproject.events.ConnectionErrorEvent;
import endava.com.demoproject.events.CredentialsErrorEvent;
import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.User;
import endava.com.demoproject.retrofit.UserAPI;

public class LoginCommand implements Command, Parcelable, Event {

    public static final Creator<LoginCommand> CREATOR = new Creator<LoginCommand>() {
        @Override
        public LoginCommand createFromParcel(Parcel in) {

            LoginCommand loginCommand = new LoginCommand(in);
            DemoProjectApplication.getApplicationComponent().inject(loginCommand);
            return loginCommand;
        }

        @Override
        public LoginCommand[] newArray(int size) {
            return new LoginCommand[size];
        }
    };
    @Inject
    public DbHelper dbHelper;
    @Inject
    Resources resources;
    private List<User> userList;
    @Inject
    public Subject subject;

    @Inject
    public UserAPI userAPI;

    private String credentials;
    private String userName;

    public LoginCommand(String credentials, String userName) {
        DemoProjectApplication.getApplicationComponent().inject(this);
        this.credentials = credentials;
        this.userName = userName;
    }

    protected LoginCommand(Parcel in) {
        credentials = in.readString();
        userName = in.readString();
    }

    @Override
    public void execute() {
        try {
            userList = userAPI.auth(credentials).execute().body();
        } catch (Exception e) {
            subject.onNewEvent(new ConnectionErrorEvent(resources));
            return;
        }
        if (userList != null) {
            User user = userList.get(0);
            try {
                user.setAvatarUrl(userAPI.getUserAvatar(credentials).execute().body().getAvatarUrl());
            } catch (Exception e) {
                subject.onNewEvent(new ConnectionErrorEvent(resources));
                return;
            }
            user.setHashedCredentials(credentials);
            user.setUserName(userName);
            dbHelper.createUser(user);
            subject.onNewEvent(this);
        } else {
            subject.onNewEvent(new CredentialsErrorEvent(resources));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(credentials);
        dest.writeString(userName);
    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public EventContext getEventKey() {
        return new EventContext(resources.getString(R.string.successful_login_tag), null);
    }
}
