package android.endava.com.demoproject;


import android.endava.com.demoproject.db.DataBaseHelper;
import android.endava.com.demoproject.db.HelperFactory;
import android.endava.com.demoproject.model.User;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.SQLException;

public class SaveUserToDB implements Command, Parcelable {

    User user;
    DataBaseHelper dbHelper = HelperFactory.getHelper();

    public SaveUserToDB(User user) {
        this.user = user;
    }

    @Override
    public void execute() {
        try {
            dbHelper.getAppDAO().create(user.getApp());
            dbHelper.getUserDAO().create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SaveUserToDB> CREATOR = new Creator<SaveUserToDB>() {
        @Override
        public SaveUserToDB createFromParcel(Parcel in) {
            return new SaveUserToDB(in);
        }

        @Override
        public SaveUserToDB[] newArray(int size) {
            return new SaveUserToDB[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, flags);
    }
    protected SaveUserToDB(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
    }
}
