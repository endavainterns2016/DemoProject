package android.endava.com.demoproject;


import android.endava.com.demoproject.db.ClientDataBaseHelper;
import android.endava.com.demoproject.model.User;
import android.os.Parcel;
import android.os.Parcelable;

public class SaveUserToDB implements Command, Parcelable {

    User user;
    ClientDataBaseHelper dbHelper = ClientDataBaseHelper.getInstance();

    public SaveUserToDB(User user) {
        this.user = user;
    }

    @Override
    public void execute() {
            dbHelper.createUser(user);
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
