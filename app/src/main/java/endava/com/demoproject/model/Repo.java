package endava.com.demoproject.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "Repo")
public class Repo {

    public final static String NAME = "NAME";
    public final static String GIT_ID_FIELD_NAME = "GIT_ID";
    public final static String DESCRIPTION = "DESCRIPTION";
    public final static String HOME_URL = "HOME_URL";
    public final static String DEF_BRANCH = "DEFAULT_BRANCH";
    public final static String CODE_LANG = "CODE_LANGUAGE";
    public final static String SIZE = "SIZE";
    public final static String LAST_PUSH = "LAST_PUSH";
    public static final String OPEN_ISSUES = "OPEN_ISSUES";

    @DatabaseField(generatedId = true)
    private int dbId;

    @DatabaseField(dataType = DataType.LONG_OBJ, columnName = GIT_ID_FIELD_NAME)
    @SerializedName("id")
    private Long gitId;

    @DatabaseField(dataType = DataType.STRING, columnName = NAME)
    private String name;

    @DatabaseField(dataType = DataType.STRING, columnName = DESCRIPTION)
    private String description;

    @DatabaseField(dataType = DataType.STRING, columnName = HOME_URL)
    @SerializedName("html_url")
    private String homeUrl;

    @DatabaseField(dataType = DataType.STRING, columnName = DEF_BRANCH)
    @SerializedName("default_branch")
    private String defaultBranch;

    @DatabaseField(dataType = DataType.STRING, columnName = CODE_LANG)
    @SerializedName("language")
    private String codeLanguage;

    @DatabaseField(dataType = DataType.DOUBLE_OBJ, columnName = SIZE)
    private Double size;

    @DatabaseField(dataType = DataType.STRING, columnName = LAST_PUSH)
    @SerializedName("pushed_at")
    private String lastPush;

    @DatabaseField(dataType = DataType.INTEGER_OBJ, columnName = OPEN_ISSUES)
    @SerializedName("open_issues")
    private Integer openIssues;

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int id) {
        this.dbId = id;
    }

    public Long getGitId() {
        return gitId;
    }

    public void setGitId(Long gitId) {
        this.gitId = gitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public String getCodeLanguage() {
        return codeLanguage;
    }

    public void setCodeLanguage(String codeLanguage) {
        this.codeLanguage = codeLanguage;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getLastPush() {
        return lastPush;
    }

    public void setLastPush(String lastPush) {
        this.lastPush = lastPush;
    }

    public int getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(int openIssues) {
        this.openIssues = openIssues;
    }
}
