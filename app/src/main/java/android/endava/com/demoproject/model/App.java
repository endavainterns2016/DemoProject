package android.endava.com.demoproject.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "App")
public class App {

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
