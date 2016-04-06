package endava.com.demoproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "App")
public class App implements Parcelable {

    public final static String NAME_FIELD_NAME = "NAME";
    public final static String URL_FIELD_NAME = "URL";
    public final static String CLIENT_ID_FIELD_NAME = "CLIENT_ID";
    @DatabaseField(generatedId = true)
    private int Id;
    @DatabaseField(dataType = DataType.STRING, columnName = NAME_FIELD_NAME)
    private String name;
    @DatabaseField(dataType = DataType.STRING, columnName = URL_FIELD_NAME)
    private String url;
    @DatabaseField(dataType = DataType.STRING, columnName = CLIENT_ID_FIELD_NAME)
    @SerializedName("client_id")
    private String clientId;

    public App() {
    }

    protected App(Parcel in) {
        Id = in.readInt();
        name = in.readString();
        url = in.readString();
        clientId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(clientId);
    }

    public static final Creator<App> CREATOR = new Creator<App>() {
        @Override
        public App createFromParcel(Parcel in) {
            return new App(in);
        }

        @Override
        public App[] newArray(int size) {
            return new App[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

}
