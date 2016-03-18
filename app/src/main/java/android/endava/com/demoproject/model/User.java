package android.endava.com.demoproject.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "User")
public class User {

    public final static String GIT_HUB_ID_FIELD_NAME = "GIT_HUB_ID";
    public final static String APP_FIELD_NAME = "APP";
    public final static String CREATED_AT_FIELD_NAME = "CREATED_AT";
    public final static String UPDATED_AT_FIELD_NAME = "UPDATED_AT";
    public final static String TOKEN_FIELD_NAME = "TOKEN";
    public final static String USERNAME_FIELD_NAME = "USERNAME";
    public final static String PASSWORD_FIELD_NAME = "SHOULD_SAVE_USERNAME";

    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField(dataType = DataType.LONG_OBJ, columnName = GIT_HUB_ID_FIELD_NAME)
    @SerializedName("id")
    private Long gitHubId;

    @DatabaseField(columnName = APP_FIELD_NAME, foreign = true, foreignAutoRefresh = true)
    private App app;

    @DatabaseField(dataType = DataType.STRING, columnName = CREATED_AT_FIELD_NAME)
    @SerializedName("created_at")
    private String createdAt;

    @DatabaseField(dataType = DataType.STRING, columnName = UPDATED_AT_FIELD_NAME)
    @SerializedName("updated_at")
    private String updatedAt;

    @DatabaseField(dataType = DataType.STRING, columnName = TOKEN_FIELD_NAME)
    @SerializedName("hashed_token")
    private String token;

    @DatabaseField(dataType = DataType.STRING, columnName = USERNAME_FIELD_NAME)
    private String userName;

    @DatabaseField(dataType = DataType.BOOLEAN_OBJ, columnName = PASSWORD_FIELD_NAME)
    private Boolean shouldSaveUserName;

    public User() {
        //empty constructor is needed by ormLite
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public Long getGitHubId() {
        return gitHubId;
    }

    public void setGitHubId(Long id) {
        this.gitHubId = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Boolean getShouldSaveUserName() {
        return shouldSaveUserName;
    }

    public void setShouldSaveUserName(Boolean shouldSaveUserName) {
        this.shouldSaveUserName = shouldSaveUserName;
    }
}
